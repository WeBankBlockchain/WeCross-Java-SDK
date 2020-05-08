package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;

public class Stubs {
    String[] stubTypes;

    public String[] getStubTypes() {
        return stubTypes;
    }

    public void setStubTypes(String[] stubTypes) {
        this.stubTypes = stubTypes;
    }

    @Override
    public String toString() {
        return "Stubs{" + "stubTypes=" + Arrays.toString(stubTypes) + '}';
    }
}
