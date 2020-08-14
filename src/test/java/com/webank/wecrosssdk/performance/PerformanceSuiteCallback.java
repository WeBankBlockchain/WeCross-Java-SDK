package com.webank.wecrosssdk.performance;

public interface PerformanceSuiteCallback {
    void onSuccess(String message);

    void onFailed(String message);

    void releaseLimiter();

    void onSuccessWithoutRelease(String message);

    void onFailedWithoutRelease(String message);
}
