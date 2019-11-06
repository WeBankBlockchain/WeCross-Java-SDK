package com.webank.wecrosssdk.exception;

public class ConsoleException extends java.lang.Exception {

    private static final long serialVersionUID = 3754251447587995515L;

    private Integer errorCode;

    public ConsoleException(Integer code, String message) {
        super(message);
        errorCode = code;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
