package com.webank.wecrosssdk.exception;

public class WeCrossSDKException extends java.lang.Exception {

    private static final long serialVersionUID = 3754251347587995515L;

    private final Integer errorCode;

    public WeCrossSDKException(Integer code, String message) {
        super(message);
        errorCode = code;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
