package com.webank.wecrosssdk.performance.BCOS;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;

public class BCOSSendTransactionSuite implements PerformanceSuite {
    private Resource resource;
    private String data = "aa";

    public BCOSSendTransactionSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        try {
            String[] ret = resource.sendTransaction("set", data);
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract, method is not exists");
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
            String[] ret = resource.sendTransaction("set", data);
            if (ret[0].equals(data)) {
                callback.onSuccess(ret[0]);
            } else {
                callback.onFailed(ret[0]);
            }

        } catch (WeCrossSDKException e) {
            callback.onFailed(e.getMessage());
        }
    }
}
