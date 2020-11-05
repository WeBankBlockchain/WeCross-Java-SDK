package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;
import java.util.List;

public class Receipt {
    private int errorCode = -1;
    private String message;
    private String hash;
    private List<String> extraHashes;
    private long blockNumber;
    private String[] result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<String> getExtraHashes() {
        return extraHashes;
    }

    public void setExtraHashes(List<String> extraHashes) {
        this.extraHashes = extraHashes;
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
                + message
                + '\''
                + ", hash='"
                + hash
                + '\''
                + ", extraHashes="
                + extraHashes
                + ", blockNumber="
                + blockNumber
                + ", result="
                + Arrays.toString(result)
                + '}';
    }
}
