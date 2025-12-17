package com.exchange.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.exchange.common.datasource.entry.BaseEntity;
import lombok.Data;

@Data
@TableName("ex_user")
public class UserEntry extends BaseEntity {
    private String userName;
    private String password;
}
