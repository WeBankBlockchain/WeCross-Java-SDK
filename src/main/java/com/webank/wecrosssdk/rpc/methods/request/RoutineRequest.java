package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class RoutineRequest {
    private String transactionID;
    private String account;
    private String[] paths;

    public RoutineRequest() {}

    public RoutineRequest(String transactionID, String account, String[] paths) {
        this.transactionID = transactionID;
        this.account = account;
        this.paths = paths;
    }

    public String getAccounts() {
        return account;
    }

    public void setAccounts(String accounts) {
        this.account = account;
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
                + ", account="
                + account
                + ", paths="
                + Arrays.toString(paths)
                + '}';
    }
}
