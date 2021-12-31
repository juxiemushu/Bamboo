package cn.wsp.bamboo.core.extension;

import cn.wsp.bamboo.common.lang.Holder;
import cn.wsp.bamboo.common.utils.ClassUtils;
import cn.wsp.bamboo.core.extension.annotation.SPI;
import cn.wsp.bamboo.core.extension.annotation.SPIInstance;
import cn.wsp.bamboo.core.extension.strategy.LoadingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * @author WuWei
 * @date 2021/12/31 10:48 上午
 */

public class ExtensionLoader<T> {

    private static final Logger LOGGER = Logger.getLogger(ExtensionLoader.class);

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    private static final String LOG_PREFIX = ExtensionLoader.class.getSimpleName();

    /**
     * 普通扩展类缓存，不包括 Wrapper 扩展类和 Adaptive 扩展类
     * 扩展名与扩展类缓存
     */
    private final Holder<Map<String, Class<?>>> cachedClassesHolder = new Holder<>();

    /**
     * Wrapper 类缓存
     */
    private Set<Class<?>> cachedWrapperClasses;

    /**
     * 自适应扩展类，一个扩展点最多只能有一个自适应扩展类
     */
    private volatile Class<?> cachedAdaptiveClass = null;

    /**
     *
     */
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 扩展实现类对应的 ExtensionLoader 的本地缓存
     * 利用 ExtensionLoader 加载扩展实现类时，会创建一个 ExtensionLoader；为避免每次都创建新的 ExtensionLoader，将其本地缓存
     */
    private static final ConcurrentHashMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>(16);

    /**
     * 扩展实现类的实例本地缓存
     */
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>(64);

    /**
     * ExtensionLoader 要加载的扩展服务接口
     */
    private final Class<?> extensionInterfaceClass;

    /**
     * 默认扩展实现类别名，@SPI注解中的value属性值
     * {@link ExtensionLoader#cacheDefaultExtensionName}
     */
    private String cachedDefaultName;

    /**
     * 获取所有加载策略的实现
     */
    private static volatile LoadingStrategy[] loadingStrategies = loadLoadingStrategies();

    private ExtensionLoader(Class<?> extensionInterfaceClass) {
        this.extensionInterfaceClass = extensionInterfaceClass;
    }

    // TODO
    public static void addLoadingStrategy(LoadingStrategy strategy) {
        return;
    }

    /**
     * 获取扩展服务接口对应的 ExtensionLoader，先从本地缓存（Map）中获取，如果本地缓存没有，则新建一个 ExtensionLoader，并加入本地缓存
     *
     * @param clazz 扩展服务接口
     * @param <T>   接口类型
     * @return  扩展服务接口对应的 ExtensionLoader
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        if(clazz == null) {
            throw new IllegalArgumentException(LOG_PREFIX + "#getExtensionLoader param is null");
        }

        if(!clazz.isInterface()) {
            throw new IllegalArgumentException(LOG_PREFIX + "#getExtensionLoader clazz is not an interface");
        }

        if(!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException(LOG_PREFIX + "#getExtensionLoader clazz(" + clazz + ") is not " +
                    "annotated with @" + SPI.class.getSimpleName());
        }

        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(clazz);
        if(extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(clazz, new ExtensionLoader<>(clazz));
            extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(clazz);
        }

        return extensionLoader;
    }

    public T getExtension(String name) {
        return getExtension(name, Boolean.TRUE);
    }

    public T getExtension(String name, Boolean wrap) {
        if(StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(LOG_PREFIX + "#getExtension name is empty");
        }

        if("true".equals(name)) {
            return getDefaultExtension();
        }

        final Holder<Object> holder = getOrCreateHolder(name);
        Object instance = holder.get();
        if(instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if(instance == null) {
                    instance = createExtension(name, wrap);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * 获取默认的扩展实现
     *
     * @return  默认的扩展实现
     */
    public T getDefaultExtension() {
        getExtensionClasses();

        if(StringUtils.isBlank(cachedDefaultName) || "true".equals(cachedDefaultName)) {
            return null;
        }

        return getExtension(cachedDefaultName);
    }

    /**
     * 获取扩展服务实现类
     *
     * @return 返回扩展实现类
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClassesHolder.get();
        // 双重否定判断
        if(classes == null) {
            synchronized (cachedClassesHolder) {
                classes = cachedClassesHolder.get();
                if(classes == null) {
                    classes = loadExtensionClasses();
                    cachedClassesHolder.set(classes);
                }
            }
        }

        return classes;
    }

    /**
     * 同步加载扩展实现类信息
     *
     * @return  加载到的扩展实现类
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        cacheDefaultExtensionName();

        // 从 LoadingStrategy 配置的路径中加载扩展实现类信息
        Map<String, Class<?>> extensionClasses = new HashMap<>(16);
        Arrays.stream(loadingStrategies).forEach(item -> loadExtensionClassWithStrategy(extensionClasses, item));

        return extensionClasses;
    }

    /**
     * 根据加载策略从对应目录下加载配置文件中的扩展实现类信息，存放到本地缓存（Map）中
     *
     * @param extensionClassMap     存放扩展实现类的本地缓存
     * @param loadingStrategy   加载策略，指定了加载目录、是否覆盖低优先级加载策略等属性
     */
    private void loadExtensionClassWithStrategy(Map<String, Class<?>> extensionClassMap, LoadingStrategy loadingStrategy) {
        String fileName = loadingStrategy.directory() + extensionInterfaceClass.getName();

        try {
            Enumeration<URL> resourceUrls = null;
            ClassLoader classLoader = null;

            // 是否优先使用 ExtensionLoader 对应的类加载器加载扩展服务配置文件
            if(loadingStrategy.preferExtensionClassLoader()) {
                classLoader = ExtensionLoader.class.getClassLoader();
                if(ClassLoader.getSystemClassLoader() != classLoader) {
                    resourceUrls = classLoader.getResources(fileName);
                }
            }

            // 如果资源 URL 信息为空，则选择其他加载器加载资源
            if(resourceUrls == null || !resourceUrls.hasMoreElements()) {
                classLoader = findClassLoader();
                resourceUrls = (classLoader != null) ? classLoader.getResources(fileName) : ClassLoader.getSystemResources(fileName);
            }

            if(resourceUrls == null || !resourceUrls.hasMoreElements()) {
                return;
            }

            // 每个资源 URL 代表一个扩展实现类信息，使用类加载器加载对应资源 URL 代表的实现类，并实例化
            while (resourceUrls.hasMoreElements()) {
                URL resourceUrl = resourceUrls.nextElement();
                loadResource(extensionClassMap, loadingStrategy, classLoader, resourceUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据可扩展服务配置文件中的 URL，使用类加载器加载对应的扩展服务实现，放入本地缓存（Map）中
     *
     * @param extensionClassMap     放置扩展服务的实现类的容器
     * @param loadingStrategy   加载策略
     * @param classLoader   类加载器
     * @param resourceUrl   资源URL
     */
    private void loadResource(Map<String, Class<?>> extensionClassMap, LoadingStrategy loadingStrategy, ClassLoader classLoader, URL resourceUrl) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int charIndex = line.indexOf('#');
                if(charIndex > 0) {
                    line = line.substring(0, charIndex);
                }

                if(StringUtils.isBlank(line)) {
                    continue;
                }

                /* 配置文件中实现类的配置格式为：alisName=com.xxx.xxx.extensionClassName
                 *   或者为 com.xxx.xxx.extensionClassName，此时扩展实现类需要使用 @Extension 注解修饰，并在注解中设置实例别名
                 */
                String extensionClassAlisName = null, extensionClassPath = null;

                int index = line.indexOf("=");
                if (index > 0) {
                    extensionClassAlisName = line.substring(0, index).trim();
                    extensionClassPath = line.substring(index + 1).trim();
                }
                if(StringUtils.isBlank(extensionClassPath)) {
                    continue;
                }

                // 在加载策略中可以设置一个包路径（package）列表，如果扩展实现类在这些包中，则略过
                if(isInclude(extensionClassPath, loadingStrategy.excludedPackages())) {
                    continue;
                }

                // 使用类加载器根据扩展实现类的路径，加载实现类并获取实例，放到本地缓存（Map）中
                loadExtensionClass(extensionClassMap, resourceUrl, Class.forName(extensionClassPath, true, classLoader),
                        extensionClassAlisName, loadingStrategy.overridden());
            }
        }catch (Exception e) {
            LOGGER.error(LOG_PREFIX + "#loadResource, Exception occurred when loading extension class (interface: " +
                    extensionInterfaceClass + ", class file: " + resourceUrl + ") in " + resourceUrl, e);
        }
    }


    private void loadExtensionClass(Map<String, Class<?>> extensionClassMap, URL resourceUrl, Class<?> extensionClass,
                                    String extensionClassAlisName, boolean overridden) throws NoSuchMethodException {
        // 如果加载出来的扩展类不是服务接口的实现类，则报错
        if(!extensionInterfaceClass.isAssignableFrom(extensionClass)) {
            throw new IllegalStateException(LOG_PREFIX + "#loadExtensionClass, Error occurred when loading extension class (interface: " +
                    extensionInterfaceClass + ", class line: " + extensionClass.getName() + "), class "
                    + extensionClass.getName() + " is not subtype of interface.");
        }

        extensionClass.getConstructor();

        // 如果配置文件中没有配置实现类的别名，则从实现类自身上获取，如果仍旧获取失败，则抛出异常
        if(StringUtils.isBlank(extensionClassAlisName)) {
            extensionClassAlisName = findExtensionClassAlisName(extensionClass);
        }
        if(StringUtils.isBlank(extensionClassAlisName)) {
            throw new IllegalStateException("No such extension name for the class " + extensionInterfaceClass.getName() + " in the config " + resourceUrl);
        }

        saveInExtensionClassMap(extensionClassMap, extensionClass, extensionClassAlisName, overridden);
    }

    /**
     * 将扩展实现类和对应的别名一起存放到本地缓存（Map）中，如果加载策略 LoadingStrategy 中 overridden 属性为 true，则直接存入本地缓存中，无论对应的 key 是否已存在
     *
     * @param extensionClassMap 放置扩展服务的实现类的容器
     * @param extensionClass    扩展实现类
     * @param extensionClassAlisName    扩展实现类的别名
     * @param overridden    是否直接覆盖
     */
    private void saveInExtensionClassMap(Map<String, Class<?>> extensionClassMap, Class<?> extensionClass, String extensionClassAlisName, boolean overridden) {
        Class<?> clazz = extensionClassMap.get(extensionClassAlisName);
        if(clazz == null || overridden) {
            extensionClassMap.put(extensionClassAlisName, extensionClass);
            return;
        }

        if(!Objects.equals(clazz.getName(), extensionClass.getName())) {
            throw new IllegalStateException(LOG_PREFIX + "#saveInExtensionClassMap, Duplicate extension" +
                    this.extensionInterfaceClass.getName() + " name " + extensionClassAlisName + " on " + extensionClass.getName() + " and " + clazz.getName());
        }
    }

    /**
     * 从可扩展服务实现类上获取它的别名
     * 1：如果实现类上使用了 @SPIInstance 注解修饰，注解中的 alisName 属性值就是实现类的别名
     * 2：如果实现类的后缀和可扩展服务名一致，则截取前面部分作为实现类的别名
     *
     * @param clazz     扩展实现类
     * @return  扩展实现类对应的别名
     */
    private String findExtensionClassAlisName(Class<?> clazz) {
        SPIInstance spiInstance = clazz.getAnnotation(SPIInstance.class);
        if(spiInstance != null && StringUtils.isNotBlank(spiInstance.alisName())) {
            return spiInstance.alisName();
        }

        String extensionClassAlisName = null, extensionClassName = clazz.getSimpleName();
        if(extensionClassName.endsWith(this.extensionInterfaceClass.getSimpleName())
                && extensionClassName.length() > this.extensionInterfaceClass.getSimpleName().length()) {
            extensionClassAlisName = extensionClassName.substring(0, (extensionClassName.length() - this.extensionInterfaceClass.getSimpleName().length())).toLowerCase();
        }

        return extensionClassAlisName;
    }

    /**
     * 校验扩展实现类路径是否包含在指定的包（package）中
     *
     * @param classPath     扩展实现类路径
     * @param excludedPackages  指定的包列表
     * @return  类路径是否在包列表中的包路径下，true：是；false：否
     */
    private boolean isInclude(String classPath, String[] excludedPackages) {
        if(StringUtils.isBlank(classPath) || excludedPackages == null || excludedPackages.length == 0) {
            return false;
        }

        String excludePackage = Arrays.stream(excludedPackages).filter(classPath::startsWith).findFirst().orElse(null);
        return StringUtils.isNotBlank(excludePackage);
    }

    /**
     * 获取类加载器
     *
     * @return  根据 ExtensionLoader 获取的类加载器
     */
    private ClassLoader findClassLoader() {
        return ClassUtils.getClassLoader(ExtensionLoader.class);
    }

    /**
     * 获取 @SPI 注解修饰的可扩展服务接口名，并设置到缓存中,默认为空；
     */
    private void cacheDefaultExtensionName() {
        // 获取扩展服务接口上的 @SPI 注解，获取注解中的 value 属性，作为 cachedDefaultName
        SPI interfaceAnnotation = extensionInterfaceClass.getAnnotation(SPI.class);
        if(interfaceAnnotation == null) {
            return;
        }

        String value = interfaceAnnotation.value();
        if(StringUtils.isBlank(value)) {
            return;
        }

        String[] extensionNames = NAME_SEPARATOR.split(value);
        if (extensionNames.length > 1) {
            throw new IllegalStateException(LOG_PREFIX + "#cacheDefaultExtensionName, More than 1 default extension name on extension " + extensionInterfaceClass.getName()
                    + ": " + Arrays.toString(extensionNames));
        }

        cachedDefaultName = extensionNames[0];
    }

    /**
     * 使用 ServiceLoader 加载所有的加载策略
     *
     * @return  加载策略实现
     */
    private static LoadingStrategy[] loadLoadingStrategies() {
        return StreamSupport.stream(ServiceLoader.load(LoadingStrategy.class).spliterator(), false)
                .sorted()
                .toArray(LoadingStrategy[]::new);
    }

    /**
     * 对于要加载的每个扩展服务接口，都创建一个对应的 Holder 对象，并缓存到本地
     *
     * @param extensionInterfaceName    扩展服务接口名
     * @return  对应的 Holder 对象
     */
    private Holder<Object> getOrCreateHolder(String extensionInterfaceName) {
        Holder<Object> holder = cachedInstances.get(extensionInterfaceName);
        if(holder == null) {
            cachedInstances.putIfAbsent(extensionInterfaceName, new Holder<>());
            holder = cachedInstances.get(extensionInterfaceName);
        }

        return holder;
    }

    private T createExtension(String extensionInterfaceName, boolean wrap) {
        Class<?> clazz = getExtensionClasses().get(extensionInterfaceName);
        if(clazz == null) {
            throw new IllegalStateException(extensionInterfaceName + " extension class not found");
        }

        try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if(instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }

            injectExtension(instance);

            initExtension(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Extension instance (name: " + extensionInterfaceName + ", class: " +
                    extensionInterfaceClass + ") couldn't be instantiated: " + e.getMessage(), e);
        }
    }

    private T injectExtension(T instance) {
        return instance;
    }

    private void initExtension(T instance) {
//        if (instance instanceof Lifecycle) {
//            Lifecycle lifecycle = (Lifecycle) instance;
//            lifecycle.initialize();
//        }
    }

}