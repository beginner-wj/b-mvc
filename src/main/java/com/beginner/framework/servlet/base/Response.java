package com.beginner.framework.servlet.base;

import com.beginner.framework.exception.BException;

import javax.servlet.http.Cookie;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public interface Response {
    void addCookie(Cookie var1);

    void addHeader(String var1, String var2);

    boolean containsHeader(String var1);

    String getCharacterEncoding();

    String getContentType();

    Locale getLocale();

    OutputStream getOutputStream() throws BException;

    PrintWriter getWriter() throws BException;

    boolean isCommitted();

    void setCharacterEncoding(String var1);

    void setContentLength(int var1);

    void setContentType(String var1);

    void setHeader(String var1, String var2);

    void setLocale(Locale var1);

    void setStatus(int var1);

}


