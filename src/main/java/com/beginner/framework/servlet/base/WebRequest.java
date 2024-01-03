package com.beginner.framework.servlet.base;

import cn.hutool.core.util.StrUtil;
import com.beginner.framework.exception.BException;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

public abstract class WebRequest implements Request {
    protected Map<String, Object> parameterMap;
    public WebRequest() {
        this.parameterMap = Collections.EMPTY_MAP;
    }

    public void addRequestParamter(String key, String value) throws BException {
        if (!"".equals(value)) {
            value = value.trim();

            try {
                String encoding = this.getEncoding();
                if (StrUtil.isBlank(encoding)) {
                    value = URLDecoder.decode(value, "UTF-8");
                } else {
                    value = URLDecoder.decode(value, encoding);
                }
            } catch (Exception var5) {
                throw new BException(var5);
            }

            Object o = this.parameterMap.get(key);
            if (o instanceof String) {
                List<Object> l = new ArrayList();
                l.add(o);
                l.add(value);
                this.parameterMap.put(key, l);
            } else if (o instanceof List) {
                ((List)o).add(value);
            } else if (o instanceof String[]) {
                List<String> l = Arrays.asList((String[])o);
                l.add(value);
                this.parameterMap.put(key, l);
            } else if (o == null) {
                this.parameterMap.put(key, value);
            }

        }
    }


    public String getParameter(String param) {
        Object o = this.parameterMap.get(param);
        return o instanceof String[] ? ((String[])o)[0] : (String)o;
    }

    public String[] getParameterValues(String key) {
        Object o = this.parameterMap.get(key);
        return o instanceof String[] ? (String[])o : new String[]{(String)o};
    }

    public boolean isMultipart() {
        String content_type = this.getContentType();
        return content_type != null && content_type.startsWith("multipart/form-data");
    }


    protected void turnListsToArray() {
        Iterator<String> iter = this.parameterMap.keySet().iterator();

        while(iter.hasNext()) {
            String key = (String)iter.next();
            Object o = this.parameterMap.get(key);
            if (o instanceof List) {
                List<Object> list = (List)o;
                this.parameterMap.put(key, list.toArray(new String[list.size()]));
            }
        }

    }
}

