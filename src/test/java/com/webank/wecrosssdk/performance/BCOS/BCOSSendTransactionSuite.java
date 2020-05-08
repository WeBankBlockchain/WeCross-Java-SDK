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
