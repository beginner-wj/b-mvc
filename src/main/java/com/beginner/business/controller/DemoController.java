package com.beginner.business.controller;

import com.beginner.framework.annotation.Controller;
import com.beginner.framework.annotation.RequestMapping;
import com.beginner.framework.annotation.RequestParams;
import com.beginner.framework.servlet.BaseServletRequest;
import com.beginner.framework.servlet.BaseServletResponse;


@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/test")
    public void test(BaseServletRequest req, BaseServletResponse resp, @RequestParams(value = "a") String a) {
        System.out.println("==================/demo/test/////////////" + req.getParameterMap());
        System.out.println("==================/a/====>" + a);
    }
}
