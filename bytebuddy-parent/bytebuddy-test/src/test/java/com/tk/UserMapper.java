package com.tk;

import java.util.UUID;

/**
 * @Description:
 * @Date : 2023/03/04 14:51
 * @Auther : tiankun
 */
public class UserMapper {

    public UserMapper() {
        System.out.println("执行了构造方法......");
    }

    public String selectUsername(Long id){
        return "用户id:"+id+"的名字为："+ UUID.randomUUID().toString();
    }

    public void print(){
        System.out.println(1);
    }

    public Integer selectAge(){
        return 33;
    }
}
