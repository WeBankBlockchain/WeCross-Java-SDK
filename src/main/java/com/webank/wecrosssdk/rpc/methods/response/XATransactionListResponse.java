package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.XA;
import com.webank.wecrosssdk.rpc.methods.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XATransactionListResponse
        extends Response<XATransactionListResponse.RawXATransactionListResponse> {
    public XATransactionListResponse() {
        super();
    }

    public RawXATransactionListResponse getRawXATransactionListResponse() {
        return getData();
    }

    public void setRawXATransactionListResponse(
            RawXATransactionListResponse xaTransactionListResponse) {
        setData(xaTransactionListResponse);
    }

    public static class RawXATransactionListResponse {
        private List<XA> xaList = new ArrayList<>();
        private Map<String, Integer> nextOffsets = new HashMap<>();
        private boolean finished = false;

        public List<XA> getXaList() {
            return xaList;
        }

        public void setXaList(List<XA> xaList) {
            this.xaList = xaList;
        }

        public Map<String, Integer> getNextOffsets() {
            return nextOffsets;
        }

        public void setNextOffsets(Map<String, Integer> nextOffsets) {
            this.nextOffsets = nextOffsets;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        @Override
        public String toString() {
            return "RawXATransactionListResponse{"
                    + "xaList="
                    + xaList
                    + ", nextOffsets="
                    + nextOffsets
                    + ", finished="
                    + finished
                    + '}';
        }
    }
}
