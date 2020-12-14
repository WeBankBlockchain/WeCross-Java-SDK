package com.webank.wecrosssdk.rpc.methods.request;

import java.util.HashMap;
import java.util.Map;

public class ListXATransactionsRequest {

    private int size = 0;

    private Map<String, Integer> offsets = new HashMap<>();

    public ListXATransactionsRequest(int size) {
        this.size = size;
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
}
