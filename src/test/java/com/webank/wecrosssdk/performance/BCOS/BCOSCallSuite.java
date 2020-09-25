package com.webank.wecrosssdk.performance.BCOS;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BCOSCallSuite implements PerformanceSuite {

    private static Logger logger = LoggerFactory.getLogger(BCOSCallSuite.class);

    private Resource resource;
    private String data = "[\"HelloWorld" + System.currentTimeMillis() + "\"]";
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
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            resource.getWeCrossRPC()
                    .call(resource.getPath(), "get", (String) null)
                    .asyncSend(
                            new Callback<TransactionResponse>() {
                                @Override
                                public void onSuccess(TransactionResponse response) {

                                    if (logger.isDebugEnabled()) {
                                        logger.debug(" result: {}", response.getReceipt());
                                    }

                                    if (response.getReceipt().getErrorCode() == 0) {
                                        callback.onSuccess(data);
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
