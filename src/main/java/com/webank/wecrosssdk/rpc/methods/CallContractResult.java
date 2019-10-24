package com.webank.wecrosssdk.rpc.methods;

import java.util.Arrays;
import java.util.Objects;

public class CallContractResult {
    private Integer errorCode;
    private String errorMessage;
    private String hash;
    private Object result[];

    public CallContractResult() {}

    public CallContractResult(
            Integer errorCode, String errorMessage, String hash, Object[] result) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.hash = hash;
        this.result = result;
    }

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Object[] getResult() {
        return result;
    }

    public void setResult(Object result[]) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CallContractResult)) {
            return false;
        }
        CallContractResult that = (CallContractResult) o;
        return Objects.equals(getErrorCode(), that.getErrorCode())
                && Objects.equals(getErrorMessage(), that.getErrorMessage())
                && Objects.equals(getHash(), that.getHash())
                && Arrays.equals(getResult(), that.getResult());
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(getErrorCode(), getErrorMessage(), getHash());
        result1 = 31 * result1 + Arrays.hashCode(getResult());
        return result1;
    }

    @Override
    public String toString() {
        return "Receipt{"
                + "errorCode="
                + errorCode
                + ", errorMessage='"
                + errorMessage
                + '\''
                + ", hash='"
                + hash
                + '\''
                + ", result="
                + Arrays.toString(result)
                + '}';
    }
}
