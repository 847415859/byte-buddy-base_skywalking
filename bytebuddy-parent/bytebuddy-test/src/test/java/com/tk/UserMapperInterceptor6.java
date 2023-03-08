package com.tk;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Date : 2023/03/05 8:50
 * @Auther : tiankun
 */
public class UserMapperInterceptor6 {
    /**
     * 被@RuntimeType标准的方法就是拦截方法，此时方法签名或者返回值可以与被拦截方法不一致
     * bytebuddy会在运行期间给被指定注解修饰的方法参数进行赋值
     * @param
     * @return
     */
    @RuntimeType
    public Object aaa(
            // 被拦截的目标对象，只有拦截实例方法可用
            @Origin Class<?> targetClass,
            // 被拦截的目标方法，只有拦截实例或者静态方法可用
            @Origin Method targetObjMethod,
            // 目标方法参数
            @AllArguments Object[] targetMethodArgs,
            // @Super UserMapper targetObj2
            @SuperCall Callable<?> caller
    ){
        System.out.println("targetClass = " + targetClass);
        System.out.println("targetObjMethod = " + targetObjMethod); // public java.lang.String com.tk.UserMapper.selectUsername(java.lang.Long)
        System.out.println("targetMethodArgs = " + Arrays.toString(targetMethodArgs));  // [1]
        Object call = null;
        try {
            // 调用目标方法
            call = caller.call();
            // 不能这样写，会导致递归调用
            // call = targetObjMethod.invoke(targetObj, targetMethodArgs);
            System.out.println("call = " + call);   // 用户id:1的名字为：5c2aa571-d909-4e0b-87e8-b93071296586
            return call;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
