package com.prosper.want.api.mapper;

import com.prosper.want.api.bean.Engagement;
import com.prosper.want.api.bean.Want;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EngagementMapper extends MapperI<Engagement>{

    List<Want> selectListByUser(
            @Param("userId") int userId,
            @Param("offsetId") int offsetId,
            @Param("count") int count);

}
