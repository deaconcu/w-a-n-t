package com.prosper.want.api.mapper;

import com.prosper.want.api.bean.Want;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WantMapper extends MapperI<Want>{
    List<Want> selectListByUser(
            @Param("userId") int userId,
            @Param("offsetId") int offsetId,
            @Param("count") int count,
            @Param("reverse") boolean reverse
    );
}
