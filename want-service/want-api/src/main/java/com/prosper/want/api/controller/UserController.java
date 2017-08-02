package com.prosper.want.api.controller;

import com.prosper.want.api.bean.User;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.api.service.UserService;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/user",method= RequestMethod.GET)
    public Object getUser(
            @RequestParam(value="id") int userId) {
        return userMapper.selectOne(userId);
    }

    @RequestMapping(value="/user",method= RequestMethod.POST)
    public Object createUser(HttpServletRequest request, @RequestBody String body) {
        User user = validation.getObject(body, User.class, new String[]{"phone", "name"});
        userService.addUser(user);
        return null;
    }
}
