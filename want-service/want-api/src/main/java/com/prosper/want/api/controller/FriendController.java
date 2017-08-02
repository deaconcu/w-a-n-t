package com.prosper.want.api.controller;

import com.prosper.want.api.bean.Friend;
import com.prosper.want.api.bean.Recommend;
import com.prosper.want.api.mapper.FriendMapper;
import com.prosper.want.api.mapper.RecommendMapper;
import com.prosper.want.api.service.FriendService;
import com.prosper.want.api.service.RecommendService;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
public class FriendController {

    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private FriendService friendService;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/friend",method= RequestMethod.GET)
    public Object getFriend(
            @RequestParam(value="userId", required=false) Integer userId) {
        return friendService.getFriend(userId);
    }

    @RequestMapping(value="/friend",method= RequestMethod.POST)
    public Object createFriend(HttpServletRequest request, @RequestBody String body) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        Friend friend = validation.getObject(body, Friend.class, new String[]{"friendTo"});
        friend.setFriendFrom(userId);
        friendService.addFriend(friend);
        return null;
    }

}












