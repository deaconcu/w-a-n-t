package com.prosper.want.api.service;

import com.prosper.want.api.bean.User;
import com.prosper.want.api.bean.Want;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.api.mapper.WantMapper;
import com.prosper.want.api.util.Constant;
import com.prosper.want.api.util.Util;
import com.prosper.want.common.exception.InvalidArgumentException;
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
        if (user == null) {
            throw new InvalidArgumentException("user is not exist");
        }
        if (want.getScheduleTime() == null || "".equals(want.getScheduleTime())) {
            want.setScheduleTime("1970-01-01 00:00:01");
        }
        want.setCreateTime(CommonUtil.getTime(new Date()));
        want.setUpdateTime(CommonUtil.getTime(new Date()));
        want.setState(Constant.WantState.created);
        wantMapper.insert(want);
    }

    public void updateWant(Want want, int userId) {
        if (want.getState() != Constant.WantState.created && want.getState() != Constant.WantState.paused) {
            throw new InvalidArgumentException("want state is not right");
        }
        Want wantInDb = wantMapper.selectOne(want.getId());
        if (wantInDb.getUserId() != userId) {
            throw new OperationNotAllowedException();
        }
        wantInDb.setTitle(want.getTitle());
        wantInDb.setContent(want.getContent());
        wantInDb.setAttendance(want.getAttendance());
        wantInDb.setUpdateTime(CommonUtil.getTime(new Date()));
        wantInDb.setScheduleTime(want.getScheduleTime());
        wantInDb.setScheduleLocation(want.getScheduleLocation());
        wantInDb.setScheduleLocationPoint(want.getScheduleLocationPoint());
        wantInDb.setState(want.getState());
        if (wantInDb.getScheduleTime() == null || "".equals(wantInDb.getScheduleTime())) {
            wantInDb.setScheduleTime("1970-01-01 00:00:01");
        }
        wantMapper.update(wantInDb);
    }

    public void deleteWant(String wantId, int userId) {
        Want wantInDb = wantMapper.selectOne(wantId);
        if (wantInDb != null && wantInDb.getUserId() == userId) {
            wantInDb.setState(Constant.WantState.deleted);
            wantMapper.update(wantInDb);
        }
    }
}
