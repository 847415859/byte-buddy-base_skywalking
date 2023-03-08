package com.tk.skywalking.agent.mysql.intercepter;

import com.tk.skywalking.agent.plugin.interceptor.enhance.EnhancedInstance;
import com.tk.skywalking.agent.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Date : 2023/03/06 13:44
 * @Auther : tiankun
 */
@Slf4j
public class MysqlInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes) {
        // sql语句
        objInst.setSkyWalkingDynamicField("select * from user = ?");
        log.info("before mysql method name:{},args:{}",method.getName(), Arrays.toString(allArguments));
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Object ret) {
        log.info("mysql result:{}",ret);
        // sql语句发送到oap
        Object skyWalkingDynamicField = objInst.getSkyWalkingDynamicField();
        return ret;
    }

    @Override
    public void handleEx(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] parameterTypes, Throwable t) {
        log.error("mysql error",t);
    }

    // @RuntimeType
    // public Object intercept(
    //         @This Object targetObj,
    //         @Origin Method targetMethod,
    //         @AllArguments Object[] targetMethodArgs,
    //         @SuperCall Callable<?> zuper
    // ) {
    //     log.info("before mysql exec,methodName:{},args:{}",targetMethod.getName(), Arrays.toString(targetMethodArgs));
    //     long start = System.currentTimeMillis();
    //     Object call = null;
    //     try {
    //         call = zuper.call();
    //         log.info("after mysql exec,result:{}",call);
    //     }catch (Exception e) {
    //         log.error("mysql exec error",e);
    //     }finally {
    //         long end = System.currentTimeMillis();
    //         log.info("finally mysql exec in {} ms",end - start);
    //     }
    //     return call;
    // }
}
