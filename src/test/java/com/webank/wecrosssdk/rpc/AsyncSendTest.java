package com.webank.wecrosssdk.rpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import java.util.concurrent.TimeUnit;

public class AsyncSendTest {
    public static void main(String[] args) throws Exception {
        WeCrossRPCService weCrossRPCService = new WeCrossRPCService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossRPCService);

        HttpCallback callback1 = new HttpCallback();
        callback1.setTypeReference(new TypeReference<TransactionResponse>() {});
        weCrossRPC
                .sendTransaction("payment.bcos.HelloWeCross", "bcos1", "set", "hello", "world")
                .asyncSend(callback1);

        HttpCallback callback2 = new HttpCallback();
        callback2.setTypeReference(new TypeReference<TransactionResponse>() {});
        weCrossRPC
                .call("payment.bcos.HelloWeCross", "bcos1", "get", "hello", "world")
                .asyncSend(callback2);

        TimeUnit.SECONDS.sleep(1);
        System.out.println(callback1.getResponse().toString());
        System.out.println(callback2.getResponse().toString());

        System.exit(0);
    }
}
