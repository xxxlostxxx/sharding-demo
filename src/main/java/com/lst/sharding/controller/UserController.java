package com.lst.sharding.controller;

import com.lst.sharding.entity.User;
import com.lst.sharding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-17
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping("user/insert")
    public void insert(@RequestBody User user){
        userService.insert(user);
    }

    @RequestMapping("user/get")
    @ResponseBody
    public User getById(@RequestParam Integer id){
       return userService.getById(id);
    }
}
