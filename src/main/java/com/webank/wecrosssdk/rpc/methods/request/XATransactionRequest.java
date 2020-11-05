package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class XATransactionRequest {
    private String xaTransactionID;
    private String[] paths;

    public XATransactionRequest() {}

    public XATransactionRequest(String xaTransactionID, String[] paths) {
        this.xaTransactionID = xaTransactionID;
        this.paths = paths;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    public String getXaTransactionID() {
        return xaTransactionID;
    }

    public void setXaTransactionID(String xaTransactionID) {
        this.xaTransactionID = xaTransactionID;
    }

    @Override
    public String toString() {
        return "RoutineRequest{"
                + "transactionID='"
                + xaTransactionID
                + '\''
                + ", paths="
                + Arrays.toString(paths)
                + '}';
    }
}
