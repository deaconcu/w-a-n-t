package com.prosper.want.api.service;

import com.prosper.want.api.bean.Friend;
import com.prosper.want.api.bean.User;
import com.prosper.want.api.bean.UserInfo;
import com.prosper.want.api.mapper.FriendMapper;
import com.prosper.want.api.mapper.UserInfoMapper;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.common.exception.ResourceExistException;
import com.prosper.want.common.exception.ResourceNotExistException;
import com.prosper.want.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by deacon on 2017/6/18.
 */
@Service
public class FriendService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private FriendMapper friendMapper;

    public List<UserInfo> getFriend(int userId) {
        List<Friend> FriendList = friendMapper.selectListByUser(userId);
        List<Integer> idList = new LinkedList<Integer>();
        for(Friend friend: FriendList) {
            idList.add(friend.getFriendTo());
        }
        List<UserInfo> userList = userInfoMapper.selectListByIds(idList);
        return userList;
    }

    public void addFriend(Friend friend) {
        User from = userMapper.selectOne(friend.getFriendFrom());
        User to = userMapper.selectOne(friend.getFriendTo());
        if (from == null && to == null) {
            throw new ResourceNotExistException();
        }
        Friend friendInDb = friendMapper.selectOneByPair(friend.getFriendFrom(), friend.getFriendTo());
        if (friendInDb != null) {
            throw new ResourceExistException();
        }
        friend.setCreateTime(CommonUtil.getTime(new Date()));
        friendMapper.insert(friend);
    }
}
