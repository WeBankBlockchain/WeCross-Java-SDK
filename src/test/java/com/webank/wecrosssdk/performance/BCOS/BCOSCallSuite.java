package com.webank.wecrosssdk.performance.BCOS;

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

public class BCOSCallSuite implements PerformanceSuite {
    private Resource resource;
    private String data = "aa";
    private TypeReference<?> typeReference = new TypeReference<TransactionResponse>() {};
    private ObjectMapper objectMapper = new ObjectMapper();

    public BCOSCallSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }
        try {
            resource.sendTransaction("set", data);
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract or user: " + e.getMessage());
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "BCOS Call Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback) {
        try {
            resource.getWeCrossRPC()
                    .call(resource.getPath(), resource.getAccountName(), "get", null)
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
                                        if (response.getReceipt().getResult()[0].equals(data)) {
                                            callback.onSuccess(data);
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
