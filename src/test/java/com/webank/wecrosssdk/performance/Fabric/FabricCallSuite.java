package com.webank.wecrosssdk.performance.Fabric;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

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
    public void call(PerformanceSuiteCallback callback) {
        try {
            resource.getWeCrossRPC()
                    .call(resource.getPath(), resource.getAccountName(), "query", "a")
                    .asyncSend(
                            new FutureCallback<HttpResponse>() {
                                @Override
                                public void completed(HttpResponse result) {
                                    try {
                                        TransactionResponse response =
                                                (TransactionResponse)
                                                        objectMapper.readValue(
                                                                EntityUtils.toString(
                                                                        result.getEntity()),
                                                                typeReference);
                                        System.out.println(response.getReceipt().toString());
                                        if (response.getReceipt().getErrorCode() == 0) {
                                            callback.onSuccess("success");
                                        } else {
                                            callback.onFailed("failed");
                                        }
                                    } catch (Exception e) {
                                        callback.onFailed(e.getMessage());
                                    }
                                }

                                @Override
                                public void failed(Exception e) {
                                    callback.onFailed(e.getMessage());
                                }

                                @Override
                                public void cancelled() {}
                            });

        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }
}
