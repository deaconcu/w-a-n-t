package com.prosper.want.api.mapper;

import com.prosper.want.api.bean.Invitation;
import org.apache.ibatis.annotations.Param;

/**
 * Created by deacon on 2017/8/19.
 */
public interface InvitationMapper extends MapperI<Invitation> {

    Invitation selectOneByWantUserTo(
            @Param("wantId") Integer wantId,
            @Param("toUserId") Integer toUserId);

}
