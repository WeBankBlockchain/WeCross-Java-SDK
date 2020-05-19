package com.webank.wecrosssdk.performance.Fabric;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import java.security.SecureRandom;

public class FabricSendTransactionSuite implements PerformanceSuite {
    private Resource resource;
    static final int BOUND = Integer.MAX_VALUE - 1;
    SecureRandom rand = new SecureRandom();

    public FabricSendTransactionSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(
                    ErrorCode.RESOURCE_INACTIVE, "Router or resource inactive, please check.");
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
        return "Fabric Send Transaction Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback) {
        try {
            String key = String.valueOf(rand.nextInt(BOUND));
            String value = String.valueOf(rand.nextInt(BOUND));
            String[] ret = resource.sendTransaction("set", key, value);
            callback.onSuccess(ret[0]);
        } catch (WeCrossSDKException e) {
            callback.onFailed(e.getMessage());
        }
    }
}
