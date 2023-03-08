package com.tk.skywalking.agent.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @Description: 静态方法的拦截点
 * @Date : 2023/03/06 12:27
 * @Auther : tiankun
 */
public interface StaticMethodsInterceptorPoint {
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
