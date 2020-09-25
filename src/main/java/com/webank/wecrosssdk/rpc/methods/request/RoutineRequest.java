package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class RoutineRequest {
    private String transactionID;
    private String[] paths;

    public RoutineRequest() {}

    public RoutineRequest(String transactionID, String[] paths) {
        this.transactionID = transactionID;
        this.paths = paths;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public String toString() {
        return "RoutineRequest{"
                + "transactionID='"
                + transactionID
                + '\''
                + ", paths="
                + Arrays.toString(paths)
                + '}';
    }
}
