package com.tk.skywalking.agent.plugin;

/**
 * 处理一个类的上下文状态
 * @author zhaodaowen
 * @see <a href="http://www.roadjava.com">乐之者java</a>
 */
public class EnhanceContext {
    /**
     * 是否被增强了
     */
    private boolean isEnhanced = false;

    /**
     * 是否新增了CONTEXT_ATTR_NAME
     */
    private boolean objectExtended = false;

    public boolean isEnhanced() {
        return isEnhanced;
    }

    public void initializationStageCompleted() {
        isEnhanced = true;
    }

    public boolean isObjectExtended() {
        return objectExtended;
    }

    public void objectExtendedCompleted() {
        this.objectExtended = true;
    }

}
