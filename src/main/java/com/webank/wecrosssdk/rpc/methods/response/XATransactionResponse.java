package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.XATransaction;
import com.webank.wecrosssdk.rpc.methods.Response;

public class XATransactionResponse
        extends Response<XATransactionResponse.RawXATransactionResponse> {
    public XATransactionResponse() {
        super();
    }

    public RawXATransactionResponse getRawXATransactionResponse() {
        return getData();
    }

    public void setRawXATransactionResponse(RawXATransactionResponse xaTransactionResponse) {
        setData(xaTransactionResponse);
    }

    public static class RawXATransactionResponse {
        private RawXAResponse xaResponse = new RawXAResponse();
        private XATransaction xaTransaction;

        public RawXAResponse getXaResponse() {
            return xaResponse;
        }

        public void setXaResponse(RawXAResponse xaResponse) {
            this.xaResponse = xaResponse;
        }

        public XATransaction getXaTransaction() {
            return xaTransaction;
        }

        public void setXaTransaction(XATransaction xaTransaction) {
            this.xaTransaction = xaTransaction;
        }

        @Override
        public String toString() {
            return "RawXATransactionResponse{"
                    + "xaResponse="
                    + xaResponse
                    + ", xaTransaction="
                    + xaTransaction
                    + '}';
        }
    }
}
