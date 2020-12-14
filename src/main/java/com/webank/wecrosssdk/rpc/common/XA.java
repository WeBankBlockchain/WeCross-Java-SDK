package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;

public class XA {
    private String xaTransactionID;
    private String username;
    private String status;
    private long timestamp;
    private String[] paths;

    public String getXaTransactionID() {
        return xaTransactionID;
    }

    public void setXaTransactionID(String xaTransactionID) {
        this.xaTransactionID = xaTransactionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    @Override
    public String toString() {
        return "XA{"
                + "xaTransactionID='"
                + xaTransactionID
                + '\''
                + ", username='"
                + username
                + '\''
                + ", status='"
                + status
                + '\''
                + ", timestamp="
                + timestamp
                + ", paths="
                + Arrays.toString(paths)
                + '}';
    }
}
