package com.webank.wecrosssdk.performance;

public interface PerformanceSuiteCallback {
    void onSuccess(String message);

    void onFailed(String message);
}
