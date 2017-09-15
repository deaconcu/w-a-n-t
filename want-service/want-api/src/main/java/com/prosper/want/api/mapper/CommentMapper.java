package com.prosper.want.api.mapper;

import com.prosper.want.api.bean.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by deacon on 2017/8/19.
 */
public interface CommentMapper extends MapperI<Comment> {

    List<Comment> selectListByWant(
            @Param("wantId") int wantId,
            @Param("offsetId") int offsetId,
            @Param("count") int count);

}
