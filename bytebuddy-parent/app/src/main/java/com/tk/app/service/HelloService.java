package com.tk.app.service;

import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Date : 2023/03/05 10:31
 * @Auther : tiankun
 */
@Service
public class HelloService {

    public String say(String username){
        return "你好,"+username;
    }
}
