package com.tk;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * @Description:
 * @Date : 2023/03/05 8:10
 * @Auther : tiankun
 */
public class UserMapperInterceptor5 {
    @RuntimeType
    public void aaa(@This Object targetObject){
        System.out.println("UserMapperInterceptor5 " +targetObject+ "进行了实例化");
    }
}
