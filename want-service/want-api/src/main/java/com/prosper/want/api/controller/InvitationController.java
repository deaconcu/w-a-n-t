package com.prosper.want.api.controller;

import com.prosper.want.api.bean.Invitation;
import com.prosper.want.api.mapper.InvitationMapper;
import com.prosper.want.api.service.InvitationService;
import com.prosper.want.common.exception.InvalidArgumentException;
import com.prosper.want.common.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@RestController
public class InvitationController {

    @Autowired
    private InvitationMapper invitationMapper;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private Validation validation;

    @RequestMapping(value="/invitation",method= RequestMethod.GET)
    public Object getInvitation(
            @RequestParam(value="ids", required=false) String ids,
            @RequestParam(value="wantId", required=false) Integer wantId,
            @RequestParam(value="userId", required=false) Integer userId,
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
            return invitationMapper.selectListByIds(idList);
        } if (userId != null && wantId != null) {
            return invitationService.getByWantUser(wantId, userId);
        } else {
            return invitationMapper.selectListByPage(page, pageLength);
        }
    }

    @RequestMapping(value="/invitation",method= RequestMethod.POST)
    public Object createInvitation(HttpServletRequest request, @RequestBody String body) {
        int userId = Integer.parseInt(request.getHeader("userId"));
        Invitation invitation = validation.getObject(
                body, Invitation.class, null, new String[]{"id"});
        invitation.setFromUserId(userId);
        invitationService.addInvitation(invitation);
        return null;
    }

}












