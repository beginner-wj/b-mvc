package com.beginner.business.controller;

import com.beginner.business.impl.DemoServiceImpl;
import com.beginner.framework.annotation.Autowired;
import com.beginner.framework.annotation.Controller;
import com.beginner.framework.annotation.RequestMapping;
import com.beginner.framework.annotation.RequestParams;
import com.beginner.framework.servlet.BaseServletRequest;
import com.beginner.framework.servlet.BaseServletResponse;

import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/demo")
public class DemoController {



    @Autowired
    DemoServiceImpl demoServiceImpl1;

    @RequestMapping("/test")
    public void test(BaseServletRequest req, BaseServletResponse resp, @RequestParams(value = "a") String a) throws IOException {
        System.out.println("==================/demo/test/////////////" + req.getParameterMap());
        demoServiceImpl1.dosomething();
        outResponse(resp);
    }

    public static void outResponse(BaseServletResponse response) throws IOException {
        response.setContentType("text/html;charset=GBK");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setStatus(200);

        response.setHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept,Authorization,adminToken");
        PrintWriter out = response.getSres().getWriter();
        out.print("1234234");
        out.close();
        out = null;
    }
}
