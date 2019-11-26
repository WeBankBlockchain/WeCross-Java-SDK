package com.webank.wecrosssdk.console.common;

public class CallResult {
    private Integer errorCode;
    private String errorMessage;
    private Object result;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public CallResult(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CallResult(Integer errorCode, String errorMessage, Object result) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.result = result;
    }

    @Override
    public String toString() {
        return "CallResult{"
                + "errorCode="
                + errorCode
                + ", errorMessage='"
                + errorMessage
                + '\''
                + ", result="
                + result
                + '}';
    }
}
