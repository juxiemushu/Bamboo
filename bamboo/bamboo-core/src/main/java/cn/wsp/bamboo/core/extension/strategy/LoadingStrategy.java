package cn.wsp.bamboo.core.extension.strategy;

import cn.wsp.bamboo.common.lang.Prioritized;

/**
 * @author WuWei
 * @date 2020/9/21 2:38 下午
 *
 * 加载策略接口，可定义扩展服务接口实现类从哪里加载
 */

public interface LoadingStrategy extends Prioritized {

    /**
     * 扩展服务实现类架子啊路径
     *
     * @return  返回要加载的文件路径
     */
    String directory();

    /**
     * 是否使用
     *
     * @return
     */
    default boolean preferExtensionClassLoader() {
        return false;
    }

    /**
     * 排除哪些路径
     *
     * @return  要排除的路径信息，在这些路径下的扩展实现不会被加载
     */
    default String[] excludedPackages() {
        return null;
    }

    /**
     * 决定是否用当前加载策略覆盖那些低优先级的加载策略
     *
     * @return  是否覆盖低优先级加载策略
     */
    default boolean overridden() {
        return false;
    }

}
