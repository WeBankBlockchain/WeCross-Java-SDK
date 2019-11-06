package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;

public class RPC {

    public static WeCrossRPC weCrossRPC;

    public static void main(String[] args) throws Exception {

        String server = "127.0.0.1:8080";

        WeCrossService weCrossService = new WeCrossRPCService(server);
        weCrossRPC = WeCrossRPC.init(weCrossService);

        // test exists
        Response response = weCrossRPC.exists("payment.bcos2.HelloWorldContract").send();
        System.out.println("Should exists: " + response.toString());

        response = weCrossRPC.exists("test.test.test").send();
        System.out.println("Not exists: " + response.toString());

        // list test
        ResourcesResponse resourcesResponse = weCrossRPC.list(true).send();
        System.out.println("Local resources: " + resourcesResponse.toString());

        resourcesResponse = weCrossRPC.list(false).send();
        System.out.println("With remote resources: " + resourcesResponse.toString());

        // call test
        TransactionResponse callTransactionResponse =
                weCrossRPC
                        .call(
                                "payment.bcos2.HelloWorldContract",
                                "setAndgetConstant",
                                "hello world",
                                10086)
                        .send();
        System.out.println("Correct: " + callTransactionResponse.toString());

        callTransactionResponse =
                weCrossRPC.call("test.test.test", "setAndgetConstant", "hello world", 10086).send();
        System.out.println("Not correct: " + callTransactionResponse.toString());

        // sendTransaction test
        TransactionResponse sendTransactionResponse =
                weCrossRPC
                        .sendTransaction(
                                "payment.bcos2.HelloWorldContract",
                                "setAndget",
                                "hello world",
                                10086)
                        .send();
        System.out.println("Correct: " + sendTransactionResponse.toString());

        sendTransactionResponse =
                weCrossRPC
                        .sendTransaction(
                                "test.test.test", "setAndgetConstant", "hello world", 10086)
                        .send();
        System.out.println("Not correct: " + sendTransactionResponse.toString());
    }
}
