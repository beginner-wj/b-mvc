package com.beginner.framework.domain;

import java.io.Serializable;

public class AppProperties implements Serializable {
    int serverport;
    String serverpath;

    String scanpackage;

    String encoding;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getScanpackage() {
        return scanpackage;
    }

    public void setScanpackage(String scanpackage) {
        this.scanpackage = scanpackage;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }

    public String getServerpath() {
        return serverpath;
    }

    public void setServerpath(String serverpath) {
        this.serverpath = serverpath;
    }

    @Override
    public String toString() {
        return "AppProperties{" +
                "serverport=" + serverport +
                ", serverpath='" + serverpath + '\'' +
                ", scanpackage='" + scanpackage + '\'' +
                '}';
    }
}
