package com.tk.skywalking.agent.plugin.interceptor.enhance;

import com.tk.skywalking.agent.plugin.loader.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Description: 静态方法bytebuddy拦截器
 * @Date : 2023/03/07 15:34
 * @Auther : tiankun
 */
@Slf4j
public class StaticMethodsInter {
    private StaticMethodsAroundInterceptor interceptor;

    /**
     *
     * @param methodsInterceptor 是StaticMethodsAroundInterceptor的实现类
     * @param classLoader
     */
    public StaticMethodsInter(String methodsInterceptor, ClassLoader classLoader) {
        try {
            interceptor = InterceptorInstanceLoader.load(methodsInterceptor,classLoader);
        }catch (Exception e){
            log.error("can not load ,interceptorName:{}",methodsInterceptor);
        }
    }

    @RuntimeType
    public Object intercept(
            @Origin Class<?> clazz,
            @Origin Method method,
            @AllArguments Object[] allArguments,
            @SuperCall Callable<?> zuper) throws Throwable{
        log.debug("class {} before exec static method {}, arguments :{}",clazz,method.getName(), allArguments);
        // 处理前置通知
        try {
            interceptor.beforeMethod(clazz,method,allArguments,method.getParameterTypes());
        }catch (Throwable e) {
            log.error("class {} before exec static method {} interceptor failure",clazz,method.getName(),e);
        }

        Object call = null;
        try {
            call = zuper.call();
        }catch (Throwable t) {
            // 异常处理
            try {
                interceptor.handleEx(clazz,method,allArguments,method.getParameterTypes(),t);
            }catch (Throwable e) {
                log.error("class {} hanle static method {} exception failure",clazz,method.getName(),e);
            }
            throw t;
        }finally {
            // 最终执行
            try {
                call = interceptor.afterMethod(clazz,method,allArguments,method.getParameterTypes(),call);
            }catch (Throwable t) {
                log.error("class {} after exec static method {} interceptor failure",clazz,method.getName(),t);
            }
        }
        return call;
    }
}
