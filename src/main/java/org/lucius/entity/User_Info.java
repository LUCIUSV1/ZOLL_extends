package org.lucius.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author lucius
 * @Date 2021-03-22
 */
@Data
@TableName("user_info")
public class User_Info {

    @TableField(value = "user_id")
    private int userId;
    @TableField(value = "user_name")
    private String userName;
    @TableField(value = "user_pwd")
    private String userPwd;
    @TableField(value = "user_remark")
    private String userRemark;
}
