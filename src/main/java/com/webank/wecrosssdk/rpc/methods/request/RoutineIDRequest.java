package com.webank.wecrosssdk.rpc.methods.request;

public class RoutineIDRequest {

    private String path;
    private int option = 0;

    public RoutineIDRequest() {}

    public RoutineIDRequest(String path, int option) {
        this.path = path;
        this.option = option;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
                + ", option="
                + option
                + '}';
    }
}
