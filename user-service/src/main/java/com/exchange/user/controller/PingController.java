package com.exchange.user.controller;

import com.exchange.user.UserEntry;
import com.exchange.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    UserMapper userMapper;

    @GetMapping("/ping")
    public String ping() {
        return "user-service pong";
    }

    @PostMapping("insertUser")
    public String insertUser(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        UserEntry userEntry = new UserEntry();
        userEntry.setUserName(userName);
        userEntry.setPassword(password);
        int insert = userMapper.insert(userEntry);
        if (insert > 0) {
            log.info("插入成功----");
        }else{
            log.info("插入失败----");
        }
        return "user-service insertUser";
    }
}
