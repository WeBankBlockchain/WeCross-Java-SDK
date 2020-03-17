package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;

public class Stubs {
    String[] stubs;

    public String[] getStubs() {
        return stubs;
    }

    public void setStubs(String[] stubs) {
        this.stubs = stubs;
    }

    @Override
    public String toString() {
        return "Stubs{" + "stubs=" + Arrays.toString(stubs) + '}';
    }
}
