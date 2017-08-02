package com.prosper.want.api.controller;

import com.prosper.want.api.bean.Want;
import com.prosper.want.api.mapper.WantMapper;
import com.prosper.want.api.service.WantService;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
public class WantController {

    @Autowired
    private WantMapper wantMapper;
    @Autowired
    private WantService wantService;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/want",method= RequestMethod.GET)
    public Object getWant(
        @RequestParam(value="ids", required=false) String ids,
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
            return wantMapper.selectListByIds(idList);
        } if (userId != null && offsetId != null) {
            return wantMapper.selectListByUser(userId, offsetId, 20);
        } else {
            return wantMapper.selectListByPage(page, pageLength);
        }
    }

    @RequestMapping(value="/want",method= RequestMethod.POST)
    public Object createWant(HttpServletRequest request, @RequestBody String body) {
        Want want = validation.getObject(body, Want.class, new String[]{"userId", "title", "content"});
        wantService.addWant(want);
        return null;
    }

    @RequestMapping(value="/want",method= RequestMethod.PUT)
    public Object updateWant(HttpServletRequest request, @RequestBody String body) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        Want want = validation.getObject(body, Want.class, new String[]{"id", "userId", "title", "content"});
        wantService.updateWant(want, userId);
        return null;
    }

    @RequestMapping(value="/want",method= RequestMethod.DELETE)
    public Object deleteWant(HttpServletRequest request, @RequestParam(value="id", required=false) String wantId) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        wantService.deleteWant(wantId, userId);
        return null;
    }
}












