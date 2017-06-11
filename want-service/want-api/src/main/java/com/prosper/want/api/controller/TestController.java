package com.prosper.want.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value="/test",method= RequestMethod.GET)
    public Object test() {
        return "hello want project, you will be a giant, trust me, and give me strength!";
    }
}
