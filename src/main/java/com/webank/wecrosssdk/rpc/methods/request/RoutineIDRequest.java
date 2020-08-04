package com.webank.wecrosssdk.rpc.methods.request;

public class RoutineIDRequest {

    private String path;
    private String account;
    private int option = 0;

    public RoutineIDRequest() {}

    public RoutineIDRequest(String path, String account, int option) {
        this.path = path;
        this.account = account;
        this.option = option;
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

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "RoutineIDRequest{"
                + "path='"
                + path
                + '\''
                + ", account='"
                + account
                + '\''
                + ", option="
                + option
                + '}';
    }
}
