package cn.wsp.bamboo.common.utils;

import org.apache.log4j.Logger;

/**
 * @author WuWei
 * @date 2020/9/18 5:18 下午
 */

public class ClassUtils {

    private static final Logger LOGGER = Logger.getLogger(ClassUtils.class);

    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = null;

        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            LOGGER.error("current thread class loader initialization failed: " + e);
        }
        if(loader != null) {
            return loader;
        }

        loader = clazz.getClassLoader();
        if(loader != null) {
            return loader;
        }

        try {
            loader = ClassLoader.getSystemClassLoader();
        } catch (Exception e) {
            LOGGER.error("system class loader initialization failed: " + e);
        }
        return loader;
    }

}
