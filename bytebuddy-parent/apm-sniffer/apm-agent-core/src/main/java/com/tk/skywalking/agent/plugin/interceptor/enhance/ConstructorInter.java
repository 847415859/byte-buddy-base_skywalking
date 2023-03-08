package com.tk.skywalking.agent.plugin.interceptor.enhance;

import com.tk.skywalking.agent.plugin.loader.InterceptorInstanceLoader;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Description: 构造方法bytebuddy拦截器
 * @Date : 2023/03/07 15:43
 * @Auther : tiankun
 */
@Slf4j
public class ConstructorInter {
    private ConstructorInterceptor interceptor;

    /**
     *
     * @param methodsInterceptor 是ConstructorInterceptor的实现类
     * @param classLoader
     */
    public ConstructorInter(String methodsInterceptor, ClassLoader classLoader) {
        try {
            interceptor = InterceptorInstanceLoader.load(methodsInterceptor,classLoader);
        }catch (Exception e){
            log.error("can not load ,interceptorName:{}",methodsInterceptor);
        }
    }

    @RuntimeType
    public void intercept(@This Object obj
            ,@AllArguments Object[] allArguments) throws Throwable{
        try {
            EnhancedInstance instance = (EnhancedInstance) obj;
            interceptor.onConstruct(instance,allArguments);
        }catch (Throwable t) {
            log.error("ConstructorInter fail",t);
        }
    }
}
