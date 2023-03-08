package com.tk.app.controller;

import com.tk.app.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Date : 2023/03/05 10:32
 * @Auther : tiankun
 */
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("say")
    public String say(String username){
        return helloService.say(username);
    }
}
