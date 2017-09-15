package com.prosper.want.api.mapper;

import com.prosper.want.api.bean.Recommend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecommendMapper extends MapperI<Recommend>{

    List<Recommend> selectListByWant(
            @Param("wantId") int wantId,
            @Param("offsetId") Integer offsetId,
            @Param("count") int count
    );

    List<Recommend> selectListByUser(
            @Param("userId") Integer userId,
            @Param("offsetId") Integer offsetId,
            @Param("count") int count
    );

}
