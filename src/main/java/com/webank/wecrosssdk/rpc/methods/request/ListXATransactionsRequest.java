package com.webank.wecrosssdk.rpc.methods.request;

import java.util.HashMap;
import java.util.Map;

public class ListXATransactionsRequest {

    private int size = 0;

    private Map<String, Integer> offsets = new HashMap<>();

    private String chainPath;

    public ListXATransactionsRequest(int size) {
        this.size = size;
    }

    public ListXATransactionsRequest(int size, String chainPath) {
        this.size = size;
        this.chainPath = chainPath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<String, Integer> getOffsets() {
        return offsets;
    }

    public void setOffsets(Map<String, Integer> offsets) {
        this.offsets = offsets;
    }

    public String getChainPath() {
        return chainPath;
    }

    public void setChainPath(String chainPath) {
        this.chainPath = chainPath;
    }
}
