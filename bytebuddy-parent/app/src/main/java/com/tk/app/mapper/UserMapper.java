package com.tk.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tk.app.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @Date : 2024/05/30 9:23
 * @Auther : tiankun
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
