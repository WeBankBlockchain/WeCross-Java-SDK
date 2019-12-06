package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class SendTransactionTest {

    public static void main(String[] args) throws Exception {

        String server = "127.0.0.1:8080";

        WeCrossService weCrossService = new WeCrossRPCService(server);
        WeCrossRPC weCrossRPC = WeCrossRPC.init(weCrossService);

        // call without return
        System.out.println("*************** void sendTransaction ***************");
        TransactionResponse transactionResponseVoid =
                weCrossRPC
                        .sendTransaction(
                                "payment.bcos2.HelloWorldContract",
                                "setAndgetConstant",
                                "hello world",
                                10086)
                        .send();
        System.out.println("Result: " + transactionResponseVoid.toString());

        System.out.println("*************** sendTransaction ***************");
        TransactionResponse transactionResponse =
                weCrossRPC
                        .sendTransaction(
                                "payment.bcos2.HelloWorldContract",
                                new String[] {"Int", "IntArray", "String", "StringArray"},
                                "getAll",
                                10086,
                                new int[] {1, 2, 3},
                                "hello",
                                new String[] {"Int", "IntArray", "String", "StringArray"})
                        .send();
        System.out.println("Result: " + transactionResponse.toString());

        System.out.println("*************** sendTransactionInt ***************");
        TransactionResponse transactionResponseInt =
                weCrossRPC
                        .sendTransactionInt("payment.bcos2.HelloWorldContract", "getInt", 10086)
                        .send();
        System.out.println("Result: " + transactionResponseInt.toString());

        System.out.println("*************** sendTransactionString ***************");
        TransactionResponse decodeCallTransactionResponse =
                weCrossRPC
                        .sendTransactionString(
                                "payment.bcos2.HelloWorldContract", "getString", "hello")
                        .send();
        System.out.println("Result: " + decodeCallTransactionResponse.toString());

        System.out.println("*************** sendTransactionIntArray ***************");
        TransactionResponse transactionResponseIntArray =
                weCrossRPC
                        .sendTransactionIntArray(
                                "payment.bcos2.HelloWorldContract",
                                "getIntArray",
                                (Object) new int[] {1, 2, 3})
                        .send();
        System.out.println("Result: " + transactionResponseIntArray.toString());

        System.out.println("*************** sendTransactionStringArray ***************");
        TransactionResponse transactionResponseStringArray =
                weCrossRPC
                        .sendTransactionStringArray(
                                "payment.bcos2.HelloWorldContract",
                                "getStringArray",
                                (Object) new String[] {"Int", "IntArray", "String", "StringArray"})
                        .send();
        System.out.println("Result: " + transactionResponseStringArray.toString());
    }
}
