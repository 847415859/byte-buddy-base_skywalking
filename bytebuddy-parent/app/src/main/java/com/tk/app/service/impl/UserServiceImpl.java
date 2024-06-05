package com.tk.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tk.app.entity.User;
import com.tk.app.mapper.UserMapper;
import com.tk.app.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Date : 2024/05/30 9:28
 * @Auther : tiankun
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
