package com.lst.sharding.service.impl;

import com.lst.sharding.dao.sharding.UserMapper;
import com.lst.sharding.entity.User;
import com.lst.sharding.service.UserService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-17
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;
    @Override
    public void insert(User user) {
        Integer insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Override
    public User getById(Integer id) {
       return userMapper.getById(id);
    }
}
