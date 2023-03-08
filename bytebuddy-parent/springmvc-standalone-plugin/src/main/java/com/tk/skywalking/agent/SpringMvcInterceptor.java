package com.tk.skywalking.agent;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;


/**
 * @Description:
 * @Date : 2023/03/05 13:59
 * @Auther : tiankun
 */
@Slf4j
public class SpringMvcInterceptor {

    @RuntimeType
    public Object intercept(
            @This Object targetObj,
            @Origin Method targetMethod,
            @AllArguments Object[] targetMethodArgs,
            @SuperCall Callable<?> zuper
    ) {
        log.info("before controller exec,methodName:{},args:{}",targetMethod.getName(), Arrays.toString(targetMethodArgs));
        long start = System.currentTimeMillis();
        Object call = null;
        try {
            call = zuper.call();
            log.info("after controller exec,result:{}",call);
        }catch (Exception e) {
            log.error("controller exec error",e);
        }finally {
            long end = System.currentTimeMillis();
            log.info("finally controller exec in {} ms",end - start);
        }
        return call;
    }
}
