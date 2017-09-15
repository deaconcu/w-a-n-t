package com.prosper.want.api.service;

import com.prosper.want.api.bean.Comment;
import com.prosper.want.api.bean.User;
import com.prosper.want.api.mapper.CommentMapper;
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
public class CommentService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WantMapper wantMapper;

    @Autowired
    private CommentMapper commentMapper;

    public void addComment(Comment comment) {
        if (wantMapper.selectOne(comment.getWantId()) == null) {
            throw new InvalidArgumentException("want is not valid");
        }
        if (userMapper.selectOne(comment.getFromUserId()) == null) {
            throw new InvalidArgumentException("from user is not exist");
        }
        if (comment.getToUserId() > 0 && userMapper.selectOne(comment.getToUserId()) == null) {
            throw new InvalidArgumentException("to user is not exist");
        }
        comment.setCreateTime(CommonUtil.getTime(new Date()));
        comment.setUpdateTime(CommonUtil.getTime(new Date()));
        commentMapper.insert(comment);
    }

    public Object getListByWantId(Integer wantId, Integer offsetId, int size) {
        List<Comment> commentList = commentMapper.selectListByWant(wantId, offsetId, size);
        List<Comment> validCommentList = new LinkedList<>();
        Set<Integer> userIdList = new HashSet<Integer>();
        for (Comment comment: commentList) {
            userIdList.add(comment.getFromUserId());
            userIdList.add(comment.getToUserId());
        }
        if (userIdList.size() > 0) {
            List<User> userList = userMapper.selectListByIds(new ArrayList<>(userIdList));
            Map<Integer, User> userMap = new HashMap<Integer, User>();
            for(User user: userList) {
                userMap.put(user.getId(), user);
            }


            for (Comment comment: commentList) {
                if (userMap.containsKey(comment.getFromUserId())) {
                    comment.setFromUserName(userMap.get(comment.getFromUserId()).getName());
                    validCommentList.add(comment);
                }
                if (userMap.containsKey(comment.getToUserId())) {
                    comment.setToUserName(userMap.get(comment.getToUserId()).getName());
                } else {
                    comment.setToUserName("");
                }
            }
        }
        return validCommentList;
    }
}
