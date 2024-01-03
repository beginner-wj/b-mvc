package com.beginner.framework.servlet;

import com.beginner.framework.servlet.base.Response;
import com.beginner.framework.servlet.base.WebRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class BaseServletRequest extends WebRequest {
    HttpServletRequest sreq;
    Response sres;
    String servletPath;
    String pathInfo;

    String actionName;

    String action;

    String methodName;

    public String lastParm;//最后一个

    public BaseServletRequest() {
    }

    public BaseServletRequest(HttpServletRequest sreq)  {
        this.sreq = sreq;
    }

    public HttpServletRequest getHttpRequest() {
        return this.sreq;
    }

    public int getContentLength() {
        return this.sreq.getContentLength();
    }

    public String getHeader(String name) {
        return this.sreq.getHeader(name);
    }

    public String getInnerContextPath() {
        return this.sreq.getServletPath() + (this.sreq.getPathInfo() == null ? "" : this.sreq.getPathInfo());
    }

    public String getContentType() {
        return this.sreq.getContentType();
    }

    public String getCookie(String key) {
        Cookie[] cookies = this.sreq.getCookies();
        if (cookies == null) {
            return null;
        } else {
            Cookie[] var6 = cookies;
            int var5 = cookies.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                Cookie cookie = var6[var4];
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }

            return null;
        }
    }

    public String getEncoding() {
        return this.sreq.getCharacterEncoding();
    }

    public InputStream getInputStream() throws IOException {
        return this.sreq.getInputStream();
    }

    public String getParameter(String param) {
        return this.parameterMap.isEmpty() ? this.sreq.getParameter(param) : super.getParameter(param);
    }

    public String getContextPath() {
        return this.sreq.getContextPath();
    }

    public Map getParameterMap() {
        return this.parameterMap.isEmpty() ? this.sreq.getParameterMap() : this.parameterMap;
    }

    public String[] getParameterValues(String key) {
        return this.parameterMap.isEmpty() ? this.sreq.getParameterValues(key) : super.getParameterValues(key);
    }

    public String getPathInfo() {
        return this.pathInfo != null ? this.pathInfo : this.sreq.getPathInfo();
    }

    public String getRemoteAddr() {
        return this.sreq.getRemoteAddr();
    }

    public String getRequestURI() {
        return this.sreq.getRequestURI();
    }

    public Response getResponse() {
        return this.sres;
    }

    public String getSessionId() {
        return this.getCookie("session_id");
    }

    public void newSession(String sessionId) {
        Cookie cookie = new Cookie("session_id", sessionId);
        cookie.setPath("/");
        this.sres.addCookie(cookie);
    }

    public void prepareParameters() throws IOException {
    }

    void setRequest(HttpServletRequest sreq, Response sres) {
        this.sreq = sreq;
        this.sres = sres;
    }

    public void setServletPath(String servletPath) {
        String requestURI = this.sreq.getRequestURI();
        this.servletPath = servletPath;
        int qInd = requestURI.indexOf("?");
        int pathLen = this.sreq.getContextPath().length() + servletPath.length();
        if (qInd < 0) {
            this.pathInfo = requestURI.substring(pathLen - 1);
        } else {
            this.pathInfo = requestURI.substring(pathLen - 1, qInd);
        }

    }



}
