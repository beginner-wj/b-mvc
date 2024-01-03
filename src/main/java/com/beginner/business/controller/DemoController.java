package com.beginner.business.controller;

import com.beginner.business.impl.DemoServiceImpl;
import com.beginner.framework.annotation.Autowired;
import com.beginner.framework.annotation.Controller;
import com.beginner.framework.annotation.RequestMapping;
import com.beginner.framework.annotation.RequestParams;
import com.beginner.framework.servlet.BaseServletRequest;
import com.beginner.framework.servlet.BaseServletResponse;


@Controller
@RequestMapping("/demo")
public class DemoController {



    @Autowired
    DemoServiceImpl demoServiceImpl1;

    @RequestMapping("/test")
    public void test(BaseServletRequest req, BaseServletResponse resp, @RequestParams(value = "a") String a) {
        System.out.println("==================/demo/test/////////////" + req.getParameterMap());
        demoServiceImpl1.dosomething();

    }
}
