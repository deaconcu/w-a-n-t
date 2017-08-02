package com.prosper.want.api.service;

import com.prosper.want.api.bean.User;
import com.prosper.want.api.bean.Want;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.api.mapper.WantMapper;
import com.prosper.want.common.exception.OperationNotAllowedException;
import com.prosper.want.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by deacon on 2017/6/17.
 */
@Service
public class WantService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    WantMapper wantMapper;

    public void addWant(Want want) {
        User user = userMapper.selectOne(want.getUserId());
        if(user != null) {
            want.setCreateTime(CommonUtil.getTime(new Date()));
            want.setUpdateTime(CommonUtil.getTime(new Date()));
            wantMapper.insert(want);
        }
    }

    public void updateWant(Want want, int userId) {
        Want wantInDb = wantMapper.selectOne(want.getId());
        if (wantInDb.getUserId() != userId) {
            throw new OperationNotAllowedException();
        }
        wantInDb.setTitle(want.getTitle());
        wantInDb.setContent(want.getContent());
        wantInDb.setUpdateTime(CommonUtil.getTime(new Date()));
        wantMapper.update(wantInDb);
    }

    public void deleteWant(String wantId, int userId) {
        Want wantInDb = wantMapper.selectOne(wantId);
        if (wantInDb != null && wantInDb.getUserId() == userId) {
            wantMapper.delete(wantInDb.getId());
        }
    }
}
