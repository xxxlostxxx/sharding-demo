package com.lst.sharding.dao.sharding;

import com.lst.sharding.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author youzhu@dian.so
 * @version 1.0.0
 * @Date 2019-01-11
 * @Copyright 北京伊电园网络科技有限公司 2016-2018 © 版权所有 京ICP备17000101号
 */
public interface UserMapper {

    Integer insert(User user);

    User getById(Integer id);

    List<User>  getUserList(User user);



}
