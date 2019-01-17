package com.lst.sharding.service;

import com.lst.sharding.entity.User;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-17
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
public interface UserService {

    void insert(User user);

    User getById(Integer id);

}
