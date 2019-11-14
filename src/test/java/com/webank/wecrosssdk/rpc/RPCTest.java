package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class RPCTest {

    public static WeCrossRPC weCrossRPC;

    public static void main(String[] args) throws Exception {

        String server = "127.0.0.1:8080";

        WeCrossService weCrossService = new WeCrossRPCService(server);
        weCrossRPC = WeCrossRPC.init(weCrossService);

        // test exists
        System.out.println("*************** exists ***************");
        Response response = weCrossRPC.exists("payment.bcos2.HelloWorldContract").send();
        System.out.println("Should exists: " + response.toString());

        response = weCrossRPC.exists("test.test.test").send();
        System.out.println("Not exists: " + response.toString());

        // list test
        System.out.println("*************** list ***************");
        ResourcesResponse resourcesResponse = weCrossRPC.list(true).send();
        System.out.println("Local resources: " + resourcesResponse.toString());

        resourcesResponse = weCrossRPC.list(false).send();
        System.out.println("With remote resources: " + resourcesResponse.toString());

        // call test
        System.out.println("*************** call ***************");
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
        System.out.println("*************** sendTransaction ***************");
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

        // getData test
        System.out.println("*************** getData ***************");
        GetDataResponse getDataResponse =
                weCrossRPC.getData("payment.bcos2.HelloWorldContract", "get").send();
        System.out.println("Correct: " + getDataResponse.toString());

        // setData test
        System.out.println("*************** getData ***************");
        SetDataResponse setDataResponse =
                weCrossRPC.setData("payment.bcos2.HelloWorldContract", "set", "data").send();
        System.out.println("Correct: " + setDataResponse.toString());
    }
}
