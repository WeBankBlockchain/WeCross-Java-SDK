package com.webank.wecrosssdk.performance.Fabric;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;

public class FabricSendTransactionSuite implements PerformanceSuite {
    private Resource resource;

    public FabricSendTransactionSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(
                    ErrorCode.RESOURCE_INACTIVE, "Router or resource inactive, please check.");
        }

        try {
            String[] ret = resource.call("invoke", "a", "b", "1");
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract or user: " + e.getMessage());
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "Fabric Send Transaction Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback) {
        try {
            String[] ret = resource.sendTransaction("invoke", "a", "b", "1");
            callback.onSuccess(ret[0]);
        } catch (WeCrossSDKException e) {
            callback.onFailed(e.getMessage());
        }
    }
}
