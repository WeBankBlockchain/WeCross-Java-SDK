package com.webank.wecrosssdk.rpc.methods;

import org.springframework.core.ParameterizedTypeReference;

public class WeCrossCallback<T> {
    private ParameterizedTypeReference responseClassType;
    protected int status;
    private String message;
    protected T data;

    public void execute() {
        this.onResponse(status, message, data);
    }

    public void onResponse(int status, String message, T data) {}

    public void setStatus(int status) {
        this.status = status;
    }

    public ParameterizedTypeReference getResponseClassType() {
        return responseClassType;
    }

    public void setResponseClassType(ParameterizedTypeReference responseClassType) {
        this.responseClassType = responseClassType;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
