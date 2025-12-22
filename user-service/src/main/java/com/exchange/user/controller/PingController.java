package com.exchange.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exchange.common.redis.constant.RedisKeyPrefix;
import com.exchange.common.redis.util.RedisKeyUtil;
import com.exchange.user.UserEntry;
import com.exchange.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Jinyue
 * @CreateTime: 2025-12-09  22:32
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
@RestController
public class PingController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/ping")
    public String ping() {
        return "user-service pong";
    }


    @GetMapping("insertUser")
    public String insertUser() {
        UserEntry userEntry = new UserEntry();
        userEntry.setUserName("zhangsan");
        userEntry.setPassword("123456");
        int insert = userMapper.insert(userEntry);
        if (insert > 0) {
            log.info("插入成功----");
        }else{
            log.info("插入失败----");
        }
        UserEntry userEntry1 = userMapper.selectOne(new LambdaQueryWrapper<UserEntry>()
                .eq(UserEntry::getUserName, userEntry.getUserName()));
        String key = RedisKeyUtil.build(RedisKeyPrefix.USER, userEntry1.getId());
        redisTemplate.opsForValue().set(key, userEntry1, 1, TimeUnit.HOURS);
        return "user-service insertUser";
    }



}
