package com.prosper.want.api.service;

import com.prosper.want.api.bean.Recommend;
import com.prosper.want.api.bean.User;
import com.prosper.want.api.bean.Want;
import com.prosper.want.api.mapper.RecommendMapper;
import com.prosper.want.api.mapper.UserMapper;
import com.prosper.want.api.mapper.WantMapper;
import com.prosper.want.api.util.Constant;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.exception.ResourceNotExistException;
import com.prosper.want.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by deacon on 2017/6/18.
 */
@Service
public class RecommendService {

    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private WantMapper wantMapper;
    @Autowired
    private UserMapper userMapper;

    public void addRecommend(Recommend recommend) {
        Want want = wantMapper.selectOne(recommend.getWantId());
        if (want == null) {
            throw new ResourceNotExistException();
        }
        User user = userMapper.selectOne(recommend.getUserId());
        if (user == null) {
            throw new ResourceNotExistException();
        }
        recommend.setCreateTime(CommonUtil.getTime(new Date()));
        recommend.setUpdateTime(CommonUtil.getTime(new Date()));
        recommend.setState(Constant.RecommendState.created);
        recommendMapper.insert(recommend);
    }

    public void updateRecommend(Recommend recommend, int userId) {
        if (recommend.getState() != Constant.RecommendState.created &&
                recommend.getState() != Constant.RecommendState.accepted) {
            throw new InvalidArgumentException("recommend state is invalid");
        }
        Recommend recommendInDb = recommendMapper.selectOne(recommend.getId());
        if (recommendInDb != null && recommendInDb.getUserId() == userId) {
            recommendInDb.setContent(recommend.getContent());
            recommendInDb.setUpdateTime(CommonUtil.getTime(new Date()));
            recommendInDb.setState(recommend.getState());
            recommendMapper.update(recommendInDb);
        }
    }

    public void deleteRecommend(int recommendId, int userId) {
        Recommend recommendInDb = recommendMapper.selectOne(recommendId);
        if (recommendInDb != null && recommendInDb.getUserId() == userId) {
            recommendMapper.delete(recommendId);
        }
    }
}
