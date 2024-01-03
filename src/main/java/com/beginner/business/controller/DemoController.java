package com.beginner.business.controller;

import com.beginner.business.impl.DemoServiceImpl;
import com.beginner.framework.annotation.*;
import com.beginner.framework.constant.SysConstant;
import com.beginner.framework.servlet.BaseServletRequest;
import com.beginner.framework.servlet.BaseServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("/demo")
public class DemoController {



    @Autowired
    DemoServiceImpl demoServiceImpl1;

    @RequestMapping("/test")
    @RequestMethod(method = SysConstant.POST)
    public void test(BaseServletRequest req, BaseServletResponse resp, @RequestParams(value = "a") String a) throws IOException {
        System.out.println("==================/demo/test/////////////" + req.getParameterMap());
        demoServiceImpl1.dosomething();
        outResponseMsg(resp,"succ");
    }

    @RequestMapping("/test2")
    public void testGet(BaseServletRequest req, BaseServletResponse resp, @RequestParams(value = "a") String a) throws IOException {
        System.out.println("==================/demo/test/////////////" + req.getParameterMap());
        demoServiceImpl1.dosomething();
        outResponseMsg(resp,"succ");
    }


    public static void outResponseMsg(BaseServletResponse response,String msg) throws IOException {
        response.setContentType("text/html;charset=GBK");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setStatus(200);

        response.setHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept,Authorization,adminToken");
        PrintWriter out = response.getSres().getWriter();
        out.print(msg);
        out.close();
        out = null;
    }
}
