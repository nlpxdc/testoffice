package io.cjf.testoffice.controller;

import io.cjf.testoffice.dao.UserMapper;
import io.cjf.testoffice.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getAll")
    public List<User> getAll(){
        List<User> users = userMapper.selectAll();
        return users;
    }
}
