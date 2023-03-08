package com.tk.skywalking.agent.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @Description: 构造方法拦截点
 * @Date : 2023/03/06 12:26
 * @Auther : tiankun
 */
public interface ConstructorMethodsInterceptorPoint {
    /**
     * 要拦截哪些方法
     * @return  作为 method() 方法的参数
     */
    ElementMatcher<MethodDescription> getMethodsMatcher();


    /**
     * 获取被增强方法对应的拦截器
     * @return
     */
    String getMethodsInterceptor();
}
