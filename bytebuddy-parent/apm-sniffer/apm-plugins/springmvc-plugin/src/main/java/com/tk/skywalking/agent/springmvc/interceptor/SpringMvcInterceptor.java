package com.tk.skywalking.agent.springmvc.interceptor;

import com.tk.skywalking.agent.plugin.interceptor.enhance.EnhancedInstance;
import com.tk.skywalking.agent.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Date : 2023/03/06 16:43
 * @Auther : tiankun
 */
@Slf4j
public class SpringMvcInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        log.info("[spring-mvc] objInst :{}", objInst);
        log.info("before springmvc method name:{},args:{}",method.getName(), Arrays.toString(allArguments));
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        log.info("[spring-mvc] objInst :{}", objInst);
        log.info("springmvc result:{}",ret);
        return ret;
    }

    @Override
    public void handleEx(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        log.error("springmvc error",t);
    }

    // @RuntimeType
    // public Object intercept(
    //         @This Object targetObj,
    //         @Origin Method targetMethod,
    //         @AllArguments Object[] targetMethodArgs,
    //         @SuperCall Callable<?> zuper
    // ) {
    //     log.info("before controller exec,methodName:{},args:{}",targetMethod.getName(), Arrays.toString(targetMethodArgs));
    //     long start = System.currentTimeMillis();
    //     Object call = null;
    //     try {
    //         call = zuper.call();
    //         log.info("after controller exec,result:{}",call);
    //     }catch (Exception e) {
    //         log.error("controller exec error",e);
    //     }finally {
    //         long end = System.currentTimeMillis();
    //         log.info("finally controller exec in {} ms",end - start);
    //     }
    //     return call;
    // }
}
