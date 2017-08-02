package com.prosper.want.api.service;

import com.prosper.want.api.bean.User;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by deacon on 2017/6/17.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void addUser(User user) {
        if (user.getEmail() == null || "".equals(user.getEmail())) {
            user.setEmail("");
        }
        user.setCreateTime(CommonUtil.getTime(new Date()));
        user.setUpdateTime(CommonUtil.getTime(new Date()));
        userMapper.insert(user);
    }
}
