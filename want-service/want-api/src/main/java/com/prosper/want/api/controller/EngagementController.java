package com.prosper.want.api.controller;

import com.prosper.want.api.mapper.EngagementMapper;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
public class EngagementController {

    @Autowired
    private EngagementMapper engagementMapper;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/engagement",method= RequestMethod.GET)
    public Object getDate(
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
            return engagementMapper.selectListByIds(idList);
        } if (userId != null && offsetId != null) {
            return engagementMapper.selectListByUser(userId, offsetId, 20);
        } else {
            return engagementMapper.selectListByPage(page, pageLength);
        }
    }
}
