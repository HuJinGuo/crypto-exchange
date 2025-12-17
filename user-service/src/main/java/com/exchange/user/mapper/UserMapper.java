package com.exchange.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exchange.user.UserEntry;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntry> {
}
