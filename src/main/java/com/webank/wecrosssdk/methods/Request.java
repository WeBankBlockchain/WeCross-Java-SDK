package com.webank.wecrosssdk.methods;

public class Request<T> {
    private String version = "0.1";
    private String path;
    private String method;
    private String sig;
    private T data;

    private WeCrossCallback callback;

    public Request() {}

    public Request(String path, String method, String sig, T data) {
        this.path = path;
        this.method = method;
        this.sig = sig;
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public WeCrossCallback getCallback() {
        return callback;
    }

    public void setCallback(WeCrossCallback callback) {
        this.callback = callback;
    }
}
