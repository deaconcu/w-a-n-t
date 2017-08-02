package com.prosper.want.api.mapper;

import com.prosper.want.api.bean.Friend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendMapper extends MapperI<Friend>{

    List<Friend> selectListByUser(@Param("userId") int userId);

    Friend selectOneByPair(@Param("friendFrom") int friendFrom, @Param("friendTo") int friendTo);
    
}
