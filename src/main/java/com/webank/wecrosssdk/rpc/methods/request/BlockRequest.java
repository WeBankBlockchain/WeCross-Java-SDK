package com.webank.wecrosssdk.rpc.methods.request;

import java.math.BigInteger;

public class BlockRequest {
    private BigInteger blockNumber;
    private String path;

    public BlockRequest() {}

    public BlockRequest(BigInteger blockNumber, String path) {
        this.blockNumber = blockNumber;
        this.path = path;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
