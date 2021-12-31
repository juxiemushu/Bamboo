package cn.wsp.bamboo.common.lang;

import static java.lang.Integer.compare;

/**
 * @author WuWei
 * @date 2021/12/31 10:59 上午
 */

public interface Prioritized extends Comparable<Prioritized> {

    /**
     * 最高
     */
    int MAX_PRIORITY = Integer.MIN_VALUE;

    /**
     * The minimum priority
     */
    int MIN_PRIORITY = Integer.MAX_VALUE;

    /**
     * Normal Priority
     */
    int NORMAL_PRIORITY = 0;

    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    default int compareTo(Prioritized that) {
        return compare(this.getPriority(), that.getPriority());
    }

}
