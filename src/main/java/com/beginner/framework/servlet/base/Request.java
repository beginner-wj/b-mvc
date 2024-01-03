package com.beginner.framework.servlet.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface Request {

    String getHeader(String var1);

    int getContentLength();

    String getContentType();

    String getCookie(String var1);

    String getEncoding();


    InputStream getInputStream() throws IOException;

    String getParameter(String var1);

    Map<Object, Object> getParameterMap();

    String[] getParameterValues(String var1);

    String getContextPath();

    String getInnerContextPath();

    String getPathInfo();

    String getRemoteAddr();

    String getRequestURI();

    Response getResponse();

    String getSessionId();

    void newSession(String var1);


    void prepareParameters() throws IOException;

    void setServletPath(String var1);
}
