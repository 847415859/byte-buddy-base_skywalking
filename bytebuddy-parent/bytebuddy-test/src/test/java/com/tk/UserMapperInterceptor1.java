package com.tk;

import java.util.UUID;

/**
 * @Description:
 * @Date : 2023/03/04 17:51
 * @Auther : tiankun
 */
public class UserMapperInterceptor1 {
    public static String selectUsername(Long id){
        return "UserMapperInterceptor1:用户id:"+id+"的名字为："+ UUID.randomUUID().toString();
    }
}
