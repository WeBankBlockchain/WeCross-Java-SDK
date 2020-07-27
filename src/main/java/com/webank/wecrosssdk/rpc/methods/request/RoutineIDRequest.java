package com.webank.wecrosssdk.rpc.methods.request;

public class RoutineIDRequest {

    private String path;
    private String account;

    public RoutineIDRequest() {}

    public RoutineIDRequest(String path, String account) {
        this.path = path;
        this.account = account;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "RoutineIDRequest{" + "path='" + path + '\'' + ", account='" + account + '\'' + '}';
    }
}
