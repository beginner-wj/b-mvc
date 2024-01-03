package com.beginner.framework.servlet;

import com.beginner.framework.servlet.base.Response;
import com.beginner.framework.exception.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public class BaseServletResponse implements Response {
    HttpServletResponse sres;

    String responseBody;

    BaseServletResponse(HttpServletResponse sres) {
        this.sres = sres;
    }

    public HttpServletResponse getSres() {
        return sres;
    }

    public void setSres(HttpServletResponse sres) {
        this.sres = sres;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void addCookie(Cookie arg0) {
        this.sres.addCookie(arg0);
    }

    public void addHeader(String arg0, String arg1) {
        this.sres.addHeader(arg0, arg1);
    }

    public boolean containsHeader(String arg0) {
        return this.sres.containsHeader(arg0);
    }

    public String getCharacterEncoding() {
        return this.sres.getCharacterEncoding();
    }

    public String getContentType() {
        return this.sres.getContentType();
    }

    public Locale getLocale() {
        return this.sres.getLocale();
    }

    public OutputStream getOutputStream() throws BException {
        try {
            return this.sres.getOutputStream();
        } catch (IOException var2) {
            throw new BException(var2);
        }
    }

    public PrintWriter getWriter() throws BException {
        try {
            return this.sres.getWriter();
        } catch (IOException var2) {
            throw new BException(var2);
        }
    }

    public boolean isCommitted() {
        return this.sres.isCommitted();
    }

    public void setCharacterEncoding(String arg0) {
        this.sres.setCharacterEncoding(arg0);
    }

    public void setContentLength(int arg0) {
        this.sres.setContentLength(arg0);
    }

    public void setContentType(String arg0) {
        this.sres.setContentType(arg0);
    }

    public void setHeader(String arg0, String arg1) {
        this.sres.setHeader(arg0, arg1);
    }

    public void setLocale(Locale arg0) {
        this.sres.setLocale(arg0);
    }

    public void setStatus(int status) {
        this.sres.setStatus(status);
    }
}
