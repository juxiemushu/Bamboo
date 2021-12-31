package cn.wsp.bamboo.core.extension.strategy;

import static cn.wsp.bamboo.common.lang.Prioritized.NORMAL_PRIORITY;

/**
 * @author WuWei
 * @date 2020/9/21 3:31 下午
 *
 * 扩展服务加载策略默认实现，默认加载 META-INF/bamboo 路径下的文件
 */

public class DefaultLoadingStrategy implements LoadingStrategy {

    private static final String DEFAULT_DIRECTORY = "META-INF/bamboo/";

    @Override
    public String directory() {
        return DEFAULT_DIRECTORY;
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY;
    }

}
