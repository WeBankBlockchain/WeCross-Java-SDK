package com.webank.wecrosssdk.performance.Fabric;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;

public class FabricCallSuite implements PerformanceSuite {
    private Resource resource;
    private TypeReference<?> typeReference = new TypeReference<TransactionResponse>() {};
    private ObjectMapper objectMapper = new ObjectMapper();

    public FabricCallSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        try {
            resource.call("query", "a");
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract or user: " + e.getMessage());
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "Fabric Call Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            resource.getWeCrossRPC()
                    .call(resource.getPath(), resource.getAccount(), "query", "a")
                    .asyncSend(
                            new Callback<TransactionResponse>() {
                                @Override
                                public void onSuccess(TransactionResponse response) {
                                    if (response.getReceipt().getErrorCode() == 0) {
                                        callback.onSuccess("success");
                                    } else {
                                        callback.onFailed("failed");
                                    }
                                }

                                @Override
                                public void onFailed(WeCrossSDKException e) {
                                    callback.onFailed(e.getMessage());
                                }
                            });

        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }
}
