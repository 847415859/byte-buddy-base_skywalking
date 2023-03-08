package com.tk;

import java.util.UUID;

/**
 * @Description:
 * @Date : 2023/03/04 17:55
 * @Auther : tiankun
 */
public class UserMapperInterceptor2 {
    public String selectUsername(Long id){
        return "UserMapperInterceptor2:用户id:"+id+"的名字为："+ UUID.randomUUID().toString();
    }
}
