package com.webank.wecrosssdk.performance.Fabric;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;

public class FabricCallSuite implements PerformanceSuite {
    private Resource resource;

    public FabricCallSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        try {
            String[] ret = resource.call("query", "a");
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
            String[] ret = resource.call("query", "a");
            callback.onSuccess(ret[0]);
        } catch (WeCrossSDKException e) {
            callback.onFailed(e.getMessage());
        }
    }
}
