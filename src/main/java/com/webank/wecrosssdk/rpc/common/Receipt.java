package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;

public class Receipt {
    private int errorCode = -1;
    private String errorMessage;
    private String hash;
    private long blockNumber;
    private String[] result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
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

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
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
                + ", blockNumber="
                + blockNumber
                + ", result="
                + Arrays.toString(result)
                + '}';
    }
}
