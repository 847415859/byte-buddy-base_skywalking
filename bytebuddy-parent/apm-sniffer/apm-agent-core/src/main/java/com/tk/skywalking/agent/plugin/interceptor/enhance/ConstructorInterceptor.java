package com.tk.skywalking.agent.plugin.interceptor.enhance;

import java.lang.reflect.Method;

/**
 * 构造方法的intercept必须实现这个接口
 */
public interface ConstructorInterceptor {
    /**
     * 在构造器执行后调用
     * @param objInst 构造器new 出来的对象
     * @param allArguments
     */
    void  onConstruct(EnhancedInstance objInst,Object[] allArguments);
}
