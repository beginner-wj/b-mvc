package com.beginner.business.impl;

import com.beginner.business.service.DemoService;
import com.beginner.framework.annotation.Service;


@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public void dosomething() {
        System.out.println("=================dosomething11111==================");
    }
}
