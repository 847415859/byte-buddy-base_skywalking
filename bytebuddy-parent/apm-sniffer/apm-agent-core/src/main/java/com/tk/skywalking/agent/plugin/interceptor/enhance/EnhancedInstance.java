package com.tk.skywalking.agent.plugin.interceptor.enhance;

/**
 * 所有需要增强构造或实例方法的字节码都会实现这个接口
 * 在执行过程中传递上下文对象
 */
public interface EnhancedInstance {
    Object getSkyWalkingDynamicField();
    void setSkyWalkingDynamicField(Object value);
}
