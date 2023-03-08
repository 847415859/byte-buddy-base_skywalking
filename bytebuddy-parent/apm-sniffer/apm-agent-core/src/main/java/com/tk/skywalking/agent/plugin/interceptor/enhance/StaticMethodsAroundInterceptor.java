package com.tk.skywalking.agent.plugin.interceptor.enhance;

import java.lang.reflect.Method;

/**
 * @Description: 静态方法的intercept必须实现这个接口
 * @Date : 2023/03/07 15:35
 * @Auther : tiankun
 */
public interface StaticMethodsAroundInterceptor {
    /**
     * 前置通知
     */
    void beforeMethod(Class clazz, Method method, Object[] allArguments, Class<?>[] parameterTypes);

    /**
     * 最终通知,不过原方法是否执行异常都会执行
     * @param ret 原方法的执行结果
     */
    Object afterMethod(Class clazz, Method method,Object[] allArguments,Class<?>[] parameterTypes,Object ret);

    /**
     * 异常通知
     */
    void handleEx(Class clazz, Method method,Object[] allArguments,Class<?>[] parameterTypes,Throwable t);
}
