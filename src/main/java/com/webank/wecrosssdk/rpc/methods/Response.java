package com.webank.wecrosssdk.rpc.methods;

import java.util.Objects;

public class Response<T> {
    private String version;
    private int result = -1;
    private String message;
    private T data;

    public Response() {}

    public Response(Integer result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Response)) {
            return false;
        }
        Response<?> response = (Response<?>) o;
        return Objects.equals(getVersion(), response.getVersion())
                && Objects.equals(getResult(), response.getResult())
                && Objects.equals(getMessage(), response.getMessage())
                && Objects.equals(getData(), response.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion(), getResult(), getMessage(), getData());
    }

    @Override
    public String toString() {
        return "Response{"
                + "version='"
                + version
                + '\''
                + ", result="
                + result
                + ", message='"
                + message
                + '\''
                + ", data="
                + data
                + '}';
    }
}
