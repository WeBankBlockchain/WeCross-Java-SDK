package com.webank.wecrosssdk.performance.xa;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.methods.response.XAResponse;
import java.util.concurrent.atomic.AtomicLong;

public class BCOSXASuite implements PerformanceSuite {
    private WeCrossRPC weCrossRPC;
    private String path;
    private AtomicLong id;

    public BCOSXASuite(WeCrossRPC weCrossRPC, String path) {
        this.path = path;
        this.weCrossRPC = weCrossRPC;
        this.id = new AtomicLong(System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return "BCOS XA Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        String sIndex = String.valueOf(id.incrementAndGet());
        String iPath = path + '_' + index;
        try {
            weCrossRPC
                    .startXATransaction(
                            sIndex,
                            new String[] {
                                iPath,
                            })
                    .asyncSend(
                            new Callback<XAResponse>() {
                                @Override
                                public void onSuccess(XAResponse response) {
                                    if (response.getErrorCode() == 0
                                            && response.getXARawResponse().getStatus() == 0) {
                                        callback.releaseLimiter();
                                        try {
                                            weCrossRPC
                                                    .sendXATransaction(
                                                            sIndex,
                                                            "1",
                                                            iPath,
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
                                                                            && "true"
                                                                                    .equals(
                                                                                            response
                                                                                                    .getReceipt()
                                                                                                    .getResult()[
                                                                                                    0])) {
                                                                        try {
                                                                            weCrossRPC
                                                                                    .commitXATransaction(
                                                                                            sIndex,
                                                                                            new String
                                                                                                    [] {
                                                                                                iPath,
                                                                                            })
                                                                                    .asyncSend(
                                                                                            new Callback<
                                                                                                    XAResponse>() {
                                                                                                @Override
                                                                                                public
                                                                                                void
                                                                                                        onSuccess(
                                                                                                                XAResponse
                                                                                                                        response) {
                                                                                                    if (response
                                                                                                                            .getErrorCode()
                                                                                                                    == 0
                                                                                                            && response.getXARawResponse()
                                                                                                                            .getStatus()
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
                                                path + index + " " + response.toString());
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
