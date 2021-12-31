package cn.wsp.bamboo.core.extension.strategy;

/**
 * @author WuWei
 * @date 2020/9/21 3:34 下午
 *
 */
public class ServiceLoadingStrategy implements LoadingStrategy {

    private static final String SERVICE_DIRECTORY = "META-INF/services/";

    @Override
    public String directory() {
        return SERVICE_DIRECTORY;
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
        return MIN_PRIORITY;
    }

}
