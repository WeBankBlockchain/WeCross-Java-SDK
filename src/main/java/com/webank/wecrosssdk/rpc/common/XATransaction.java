package com.webank.wecrosssdk.rpc.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class XATransaction {
    private String xaTransactionID;
    private String username;
    private String status;
    private long startTimestamp;
    private long commitTimestamp;
    private long rollbackTimestamp;
    private List<String> paths;
    private List<XATransactionStep> xaTransactionSteps;

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

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getCommitTimestamp() {
        return commitTimestamp;
    }

    public void setCommitTimestamp(long commitTimestamp) {
        this.commitTimestamp = commitTimestamp;
    }

    public long getRollbackTimestamp() {
        return rollbackTimestamp;
    }

    public void setRollbackTimestamp(long rollbackTimestamp) {
        this.rollbackTimestamp = rollbackTimestamp;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public List<XATransactionStep> getXaTransactionSteps() {
        return xaTransactionSteps;
    }

    public void setXaTransactionSteps(List<XATransactionStep> xaTransactionSteps) {
        this.xaTransactionSteps = xaTransactionSteps;
    }

    @Override
    public String toString() {
        return "XATransactionResponse{"
                + "xaTransactionID='"
                + xaTransactionID
                + '\''
                + ", username='"
                + username
                + '\''
                + ", status='"
                + status
                + '\''
                + ", startTimestamp="
                + startTimestamp
                + ", commitTimestamp="
                + commitTimestamp
                + ", rollbackTimestamp="
                + rollbackTimestamp
                + ", paths="
                + paths
                + ", xaTransactionSteps="
                + xaTransactionSteps
                + '}';
    }
}
