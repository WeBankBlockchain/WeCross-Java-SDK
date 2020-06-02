package com.webank.wecrosssdk.performance;

public interface PerformanceSuite {
    String getName();

    void call(PerformanceSuiteCallback callback, int index);
}
