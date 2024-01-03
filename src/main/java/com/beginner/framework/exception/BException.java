package com.beginner.framework.exception;

public class BException  extends Exception{
    public BException(String message) {
        super(message);
    }

    public BException(Exception e) {
        super(e.getMessage());
    }
}
