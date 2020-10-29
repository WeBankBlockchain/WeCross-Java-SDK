package com.webank.wecrosssdk.performance.xa;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.RoutineResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.util.concurrent.atomic.AtomicLong;

public class FabricXASuite implements PerformanceSuite {
    private WeCrossRPC weCrossRPC;
    private String path;
    private AtomicLong id;

    public FabricXASuite(WeCrossRPC weCrossRPC, String path) {
        this.path = path;
        this.weCrossRPC = weCrossRPC;
        this.id = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return "Fabric XA Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        String sIndex = String.valueOf(id.incrementAndGet());
        try {
            weCrossRPC
                    .startTransaction(
                            sIndex,
                            new String[] {
                                path,
                            })
                    .asyncSend(
                            new Callback<RoutineResponse>() {
                                @Override
                                public void onSuccess(RoutineResponse response) {
                                    if (response.getErrorCode() == 0 && response.getResult() == 0) {
                                        callback.releaseLimiter();
                                        try {
                                            weCrossRPC
                                                    .execTransaction(
                                                            sIndex,
                                                            "1",
                                                            path,
                                                            "newEvidence",
                                                            "a",
                                                            "a")
                                                    .asyncSend(
                                                            new Callback<TransactionResponse>() {
                                                                @Override
                                                                public void onSuccess(
                                                                        TransactionResponse
                                                                                response) {
                                                                    if (response.getErrorCode() == 0
                                                                            && "newEvidence success"
                                                                                    .equals(
                                                                                            response
                                                                                                    .getReceipt()
                                                                                                    .getResult()[
                                                                                                    0])) {
                                                                        try {
                                                                            weCrossRPC
                                                                                    .commitTransaction(
                                                                                            sIndex,
                                                                                            new String
                                                                                                    [] {
                                                                                                path,
                                                                                            })
                                                                                    .asyncSend(
                                                                                            new Callback<
                                                                                                    RoutineResponse>() {
                                                                                                @Override
                                                                                                public
                                                                                                void
                                                                                                        onSuccess(
                                                                                                                RoutineResponse
                                                                                                                        response) {
                                                                                                    if (response
                                                                                                                            .getErrorCode()
                                                                                                                    == 0
                                                                                                            && response
                                                                                                                            .getResult()
                                                                                                                    == 0) {
                                                                                                        callback
                                                                                                                .onSuccessWithoutRelease(
                                                                                                                        "success");
                                                                                                    } else {
                                                                                                        System
                                                                                                                .out
                                                                                                                .println(
                                                                                                                        response
                                                                                                                                .toString());
                                                                                                        callback
                                                                                                                .onFailedWithoutRelease(
                                                                                                                        "commitTransaction failed");
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public
                                                                                                void
                                                                                                        onFailed(
                                                                                                                WeCrossSDKException
                                                                                                                        e) {
                                                                                                    callback
                                                                                                            .onFailedWithoutRelease(
                                                                                                                    e
                                                                                                                            .getMessage());
                                                                                                }
                                                                                            });
                                                                        } catch (Exception e) {
                                                                            callback
                                                                                    .onFailedWithoutRelease(
                                                                                            e
                                                                                                    .getMessage());
                                                                        }
                                                                    } else {
                                                                        System.out.println(
                                                                                response
                                                                                        .toString());
                                                                        callback
                                                                                .onFailedWithoutRelease(
                                                                                        "execTransaction failed");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailed(
                                                                        WeCrossSDKException e) {
                                                                    callback.onFailedWithoutRelease(
                                                                            e.getMessage());
                                                                }
                                                            });
                                        } catch (Exception e) {
                                            callback.onFailedWithoutRelease(e.getMessage());
                                        }

                                    } else {
                                        System.out.println(
                                                FabricXASuite.this.path
                                                        + index
                                                        + " "
                                                        + response.toString());
                                        callback.onFailed("startTransaction failed");
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
