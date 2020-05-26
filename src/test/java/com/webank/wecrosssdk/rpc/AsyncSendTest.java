package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import java.util.concurrent.TimeUnit;

public class AsyncSendTest {
    public static void main(String[] args) throws Exception {
        WeCrossRPCService weCrossRPCService = new WeCrossRPCService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossRPCService);
        weCrossRPC
                .test()
                .asyncSend(
                        new Callback<Response>() {
                            @Override
                            public void onSuccess(Response response) {
                                System.out.println(response.toString());
                            }

                            @Override
                            public void onFailed(WeCrossSDKException e) {
                                System.out.println(e.getMessage());
                            }
                        });

        weCrossRPC
                .sendTransaction("payment.bcos.HelloWeCross", "bcos1", "set", "hello", "world")
                .asyncSend(
                        new Callback<TransactionResponse>() {
                            @Override
                            public void onSuccess(TransactionResponse response) {
                                System.out.println(response.toString());
                            }

                            @Override
                            public void onFailed(WeCrossSDKException e) {
                                System.out.println(e.getMessage());
                            }
                        });

        weCrossRPC
                .call("payment.bcos.HelloWeCross", "bcos1", "get", "hello", "world")
                .asyncSend(
                        new Callback<TransactionResponse>() {
                            @Override
                            public void onSuccess(TransactionResponse response) {
                                System.out.println(response.toString());
                            }

                            @Override
                            public void onFailed(WeCrossSDKException e) {
                                System.out.println(e.getMessage());
                            }
                        });

        TimeUnit.SECONDS.sleep(10);

        System.exit(0);
    }
}
