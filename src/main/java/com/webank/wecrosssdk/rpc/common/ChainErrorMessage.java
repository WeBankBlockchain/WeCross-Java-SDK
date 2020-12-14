package com.webank.wecrosssdk.rpc.common;

public class ChainErrorMessage {
    private String path;
    private String message;

    public ChainErrorMessage() {}

    public ChainErrorMessage(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChainErrorMessage{"
                + "chain='"
                + path
                + '\''
                + ", message='"
                + message
                + '\''
                + '}';
    }
}
