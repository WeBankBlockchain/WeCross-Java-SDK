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

public class BCOSSendTransactionSuite implements PerformanceSuite {
    private Resource resource;
    private String data = "aa";
    private TypeReference<?> typeReference = new TypeReference<TransactionResponse>() {};
    private ObjectMapper objectMapper = new ObjectMapper();

    public BCOSSendTransactionSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        try {
            resource.sendTransaction("set", data);
        } catch (WeCrossSDKException e) {
            System.out.println("Invalid contract or user or db: " + e.getMessage());
            System.out.println(
                    "\tRollback: Check the HelloWecross address configuration in stub.toml");
            System.out.println("\tUser type: check the user type is BCOS2.0");
            System.out.println(
                    "\tBlocklimit failed: check the router is syncing or couldn't be the next block");
            throw new WeCrossSDKException(ErrorCode.INVALID_CONTRACT, "Init contract failed");
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "BCOS Send Transaction Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback) {
        try {
            resource.getWeCrossRPC()
                    .sendTransaction(resource.getPath(), resource.getAccountName(), "get", null)
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
