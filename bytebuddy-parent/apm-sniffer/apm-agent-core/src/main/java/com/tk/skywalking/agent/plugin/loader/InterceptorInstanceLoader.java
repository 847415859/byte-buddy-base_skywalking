package com.tk.skywalking.agent.plugin.loader;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 用于加载插件里面的拦截器
 * @Date : 2023/03/07 15:39
 * @Auther : tiankun
 */
@Slf4j
public class InterceptorInstanceLoader {
    /**
     *
     * @param interceptorName   插件中拦截器的全类名
     * @param targetClassLoader <strong>要想在插件拦截器中能够访问到被拦截的类,需要是同一个类加载器或子类类加载器</strong>
     *                          被拦截的类: A - C1
     * @return ConstructorInterceptor 或 InstanceMethodsAroundInterceptor 或
     *          StaticMethodsAroundInterceptor 的实例
     */
    public static <T> T load(String interceptorName,ClassLoader targetClassLoader)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        log.info("InterceptorInstanceLoader load :{}",interceptorName);
        if (targetClassLoader == null) {
            targetClassLoader = InterceptorInstanceLoader.class.getClassLoader();
        }
        AgentClassLoader classLoader = new AgentClassLoader(targetClassLoader);
        Object o = Class.forName(interceptorName, true, classLoader).newInstance();
        log.info("InterceptorInstanceLoader loaded :{}",o);
        return (T)o;
    }
}
