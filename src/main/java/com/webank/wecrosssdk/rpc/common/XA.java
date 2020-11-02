package com.webank.wecrosssdk.rpc.common;

public class XA {
    private String xaTransactionID;
    private String username;
    private String status;
    private long timestamp;

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

    @Override
    public String toString() {
        return "XATransactionInfo{"
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
                + '}';
    }
}
