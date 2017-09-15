package com.prosper.want.api.service;

import com.prosper.want.api.bean.Invitation;
import com.prosper.want.api.bean.User;
import com.prosper.want.api.mapper.InvitationMapper;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.api.mapper.WantMapper;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by deacon on 2017/8/19.
 */
@Service
public class InvitationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WantMapper wantMapper;

    @Autowired
    private InvitationMapper invitationMapper;

    public void addInvitation(Invitation invitation) {
        if (wantMapper.selectOne(invitation.getWantId()) == null) {
            throw new InvalidArgumentException("want is not valid");
        }
        if (userMapper.selectOne(invitation.getFromUserId()) == null) {
            throw new InvalidArgumentException("from user is not exist");
        }
        if (userMapper.selectOne(invitation.getToUserId()) == null) {
            throw new InvalidArgumentException("to user is not exist");
        }
        invitation.setCreateTime(CommonUtil.getTime(new Date()));
        invitation.setUpdateTime(CommonUtil.getTime(new Date()));
        invitationMapper.insert(invitation);
    }

    public Invitation getByWantUser(Integer wantId, Integer userId) {
        Invitation invitation = invitationMapper.selectOneByWantUserTo(wantId, userId);
        if (invitation == null) {
            return null;
        }
        List<Integer> userIdList = Arrays.asList(
                new Integer[]{invitation.getFromUserId(), invitation.getToUserId()});
        List<User> userList = userMapper.selectListByIds(userIdList);
        for(User user: userList) {
            if (user.getId() == invitation.getFromUserId()) {
                invitation.setFromUserName(user.getName());
            }
            if (user.getId() == invitation.getToUserId()) {
                invitation.setToUserName(user.getName());
            }
        }

        return invitation;
    }
}
