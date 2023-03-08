package com.tk;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Date : 2023/03/04 18:19
 * @Auther : tiankun
 */
public class UserMapperInterceptor4 {
    /**
     * 被@RuntimeType标准的方法就是拦截方法，此时方法签名或者返回值可以与被拦截方法不一致
     * bytebuddy会在运行期间给被指定注解修饰的方法参数进行赋值
     * @param
     * @return
     */
    @RuntimeType
    public Object aaa(
            // 目标方法参数
            @AllArguments Object[] targetMethodArgs,
            // 用于调用目标方法
            @Morph MyCallable caller
    ){
        Object call = null;
        try {
            // 调用目标方法
            call = caller.call(targetMethodArgs);
            System.out.println("call = " + call);   // 用户id:1的名字为：5c2aa571-d909-4e0b-87e8-b93071296586
            return call;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
