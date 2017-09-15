package com.prosper.want.api.controller;

import com.prosper.want.api.bean.Comment;
import com.prosper.want.api.bean.Friend;
import com.prosper.want.api.mapper.CommentMapper;
import com.prosper.want.api.mapper.FriendMapper;
import com.prosper.want.api.service.CommentService;
import com.prosper.want.api.service.FriendService;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/comment",method= RequestMethod.GET)
    public Object getComment(
            @RequestParam(value="ids", required=false) String ids,
            @RequestParam(value="wantId", required=false) Integer wantId,
            @RequestParam(value="offsetId", required=false) Integer offsetId,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="pageLength", defaultValue="50") int pageLength) {
        if (ids != null && !"".equals(ids)) {
            List<Integer> idList = new LinkedList<>();
            String[] idStrings = ids.split(",");
            for (String idString : idStrings) {
                try {
                    idList.add(Integer.parseInt(idString));
                } catch (NumberFormatException e) {
                    throw new InvalidArgumentException("id is not integer");
                }
            }
            return commentMapper.selectListByIds(idList);
        } if (wantId != null) {
            if (offsetId == null) {
                offsetId = Integer.MAX_VALUE;
            }
            return commentService.getListByWantId(wantId, offsetId, 20);
        } else {
            return commentMapper.selectListByPage(page, pageLength);
        }
    }

    @RequestMapping(value="/comment",method= RequestMethod.POST)
    public Object createComment(HttpServletRequest request, @RequestBody String body) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        Comment comment = validation.getObject(
                body, Comment.class, null, new String[]{"id"});
        comment.setFromUserId(userId);
        commentService.addComment(comment);
        return null;
    }

}












