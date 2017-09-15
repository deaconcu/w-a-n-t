package com.prosper.want.api.controller;

import com.prosper.want.api.bean.Recommend;
import com.prosper.want.api.bean.Want;
import com.prosper.want.api.mapper.RecommendMapper;
import com.prosper.want.api.mapper.WantMapper;
import com.prosper.want.api.service.RecommendService;
import com.prosper.want.api.service.WantService;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RecommendController {

    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/recommend",method= RequestMethod.GET)
    public Object getWant(
        @RequestParam(value="ids", required=false) String ids,
        @RequestParam(value="wantId", required=false) Integer wantId,
        @RequestParam(value="userId", required=false) Integer userId,
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
            return recommendMapper.selectListByIds(idList);
        } if (wantId != null && offsetId != null) {
            return recommendMapper.selectListByWant(wantId, offsetId, 20);
        } if (userId != null && offsetId != null) {
            return recommendMapper.selectListByUser(userId, offsetId, 20);
        } else {
            return recommendMapper.selectListByPage(page, pageLength);
        }
    }

    @RequestMapping(value="/recommend",method= RequestMethod.POST)
    public Object createRecommend(HttpServletRequest request, @RequestBody String body) {
        Recommend recommend = validation.getObject(
                body, Recommend.class, null, new String[]{"id"});
        recommendService.addRecommend(recommend);
        return null;
    }

    @RequestMapping(value="/recommend",method= RequestMethod.PUT)
    public Object updateRecommend(HttpServletRequest request, @RequestBody String body) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        Recommend recommend = validation.getObject(body, Recommend.class);
        recommendService.updateRecommend(recommend, userId);
        return null;
    }

    @RequestMapping(value="/recommend",method= RequestMethod.DELETE)
    public Object deleteWant(
            HttpServletRequest request,
            @RequestParam(value="id", required=false) Integer recommendId) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        recommendService.deleteRecommend(recommendId, userId);
        return null;
    }
}












