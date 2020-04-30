package com.webank.wecrosssdk.performance.BCOS;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;

public class StatusSuite implements PerformanceSuite {
    private Resource resource;

    public StatusSuite(Resource resource) throws Exception {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        try {
            resource.status();
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract or user: " + e.getMessage());
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "Status Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback) {
        try {
            String ret = resource.status();
            if (ret.equals("exists")) {
                callback.onSuccess(ret);
            } else {
                callback.onFailed(ret);
            }

        } catch (WeCrossSDKException e) {
            callback.onFailed(e.getMessage());
        }
    }
}
