package com.webank.wecrosssdk.rpc.methods.request;

import java.util.Arrays;

public class RoutineRequest {
    private String transactionID;
    private String[] accounts;
    private String[] paths;

    public RoutineRequest() {}

    public RoutineRequest(String transactionID, String[] accounts, String[] paths) {
        this.transactionID = transactionID;
        this.accounts = accounts;
        this.paths = paths;
    }

    public String[] getAccounts() {
        return accounts;
    }

    public void setAccounts(String[] accounts) {
        this.accounts = accounts;
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
                + ", accounts="
                + Arrays.toString(accounts)
                + ", paths="
                + Arrays.toString(paths)
                + '}';
    }
}
