package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.ChainErrorMessage;
import java.util.ArrayList;
import java.util.List;

public class RawXAResponse {
    private int status = 0;
    private List<ChainErrorMessage> chainErrorMessages = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ChainErrorMessage> getChainErrorMessages() {
        return chainErrorMessages;
    }

    public void setChainErrorMessages(List<ChainErrorMessage> chainErrorMessages) {
        this.chainErrorMessages = chainErrorMessages;
    }

    @Override
    public String toString() {
        return "rawResponse{"
                + "status="
                + status
                + ", chainErrorMessages="
                + chainErrorMessages
                + '}';
    }
}
