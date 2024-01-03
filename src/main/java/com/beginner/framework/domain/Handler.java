package com.beginner.framework.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.beginner.framework.annotation.*;
import com.beginner.framework.servlet.BaseServletRequest;
import com.beginner.framework.servlet.BaseServletResponse;

public class Handler {
    public Object controller;
    public Method method;
    public Pattern pattern;


    public Map<String,Integer> paramIndexMapping;

    public String requestMethod;



    public Handler(Pattern pattern, Object controller, Method method,String requestMethod) {

        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
        this.paramIndexMapping = new HashMap<String,Integer>();
        this.requestMethod = requestMethod;
        putParamIndexMapping(method);
    }

    private void putParamIndexMapping(Method method2) {

        Annotation[][] pa = method.getParameterAnnotations();
        for(int i = 0; i<pa.length;i++) {
            for(Annotation a: pa[i]) {
                if(a instanceof RequestParams) {
                    String paramName = ((RequestParams)a).value();
                    if(!paramName.trim().equals("")) {
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }
        Class<?> [] paramsTypes = method.getParameterTypes();
        for(int i = 0; i<paramsTypes.length;i++) {
            Class<?> type = paramsTypes[i];
            if(type== BaseServletRequest.class || type== BaseServletResponse.class ) {
                paramIndexMapping.put(type.getName(), i);
            }
        }

    }
    public Object convert(Class<?> type,String value) {

        if(Integer.class == type) {
            return Integer.valueOf(value);
        }

        return value;
    }

}
