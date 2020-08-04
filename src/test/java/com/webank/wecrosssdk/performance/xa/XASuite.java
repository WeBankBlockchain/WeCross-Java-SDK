package com.webank.wecrosssdk.performance.xa;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.RoutineResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;

public class XASuite implements PerformanceSuite {
    private WeCrossRPC weCrossRPC;
    private String path;
    private String account;
    private String id;
    private String[] accounts;

    public XASuite(WeCrossRPC weCrossRPC, String account, String path) {
        this.account = account;
        this.path = path;
        this.accounts = new String[] {account};
        this.weCrossRPC = weCrossRPC;
        this.id = String.valueOf(System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return "XA Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            weCrossRPC
                    .startTransaction(
                            id + index,
                            accounts,
                            new String[] {
                                path + '_' + index,
                            })
                    .asyncSend(
                            new Callback<RoutineResponse>() {
                                @Override
                                public void onSuccess(RoutineResponse response) {
                                    if (response.getErrorCode() == 0 && response.getResult() == 0) {
                                        try {
                                            weCrossRPC
                                                    .execTransaction(
                                                            id + index,
                                                            "1",
                                                            path + '_' + index,
                                                            account,
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
                                                                                    .commitTransaction(
                                                                                            id
                                                                                                    + index,
                                                                                            accounts,
                                                                                            new String
                                                                                                    [] {
                                                                                                path
                                                                                                        + '_'
                                                                                                        + index,
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
                                                                                                                .onSuccess(
                                                                                                                        "success");
                                                                                                    } else {
                                                                                                        System
                                                                                                                .out
                                                                                                                .println(
                                                                                                                        response
                                                                                                                                .toString());
                                                                                                        callback
                                                                                                                .onFailed(
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
                                                                                                            .onFailed(
                                                                                                                    e
                                                                                                                            .getMessage());
                                                                                                }
                                                                                            });
                                                                        } catch (Exception e) {
                                                                            callback.onFailed(
                                                                                    e.getMessage());
                                                                        }
                                                                    } else {
                                                                        System.out.println(
                                                                                response
                                                                                        .toString());
                                                                        callback.onFailed(
                                                                                "execTransaction failed");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailed(
                                                                        WeCrossSDKException e) {
                                                                    callback.onFailed(
                                                                            e.getMessage());
                                                                }
                                                            });
                                        } catch (Exception e) {
                                            callback.onFailed(e.getMessage());
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
