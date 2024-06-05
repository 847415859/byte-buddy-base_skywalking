package com.tk.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Date : 2024/05/30 9:23
 * @Auther : tiankun
 */
@Data
@TableName("t_user")
public class User {

    @TableId
    private Long id;
    private String userName;
    private Integer age;
    private Date createTime;
}
