package com.tk.app.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tk.app.entity.User;
import com.tk.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Date : 2024/05/30 9:32
 * @Auther : tiankun
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/list")
    public List<User> list(){
        return userService.list(Wrappers.lambdaQuery(User.class).eq(User::getId, 1).like(User::getUserName, "乾坤"));
    }

}
