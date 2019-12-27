package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class CallTest {

    public static void main(String[] args) throws Exception {

        String server = "127.0.0.1:8250";

        WeCrossService weCrossService = new WeCrossRPCService(server);
        WeCrossRPC weCrossRPC = WeCrossRPC.init(weCrossService);

        // call without return
        System.out.println("*************** void call ***************");
        TransactionResponse transactionResponseVoid =
                weCrossRPC
                        .call(
                                "payment.bcos.HelloWeCross",
                                "getInputs",
                                0,
                                new int[] {1, 2, 3},
                                "Hello World",
                                new String[] {"Bei Bei", "Jing Jing", "Huan Huan", "Ying Ying"})
                        .send();
        System.out.println("Result: " + transactionResponseVoid.toString());

        System.out.println("*************** call ***************");
        TransactionResponse transactionResponse =
                weCrossRPC
                        .call(
                                "payment.bcos.HelloWeCross",
                                new String[] {"Int", "IntArray", "String", "StringArray"},
                                "getInputs",
                                0,
                                new int[] {1, 2, 3},
                                "Hello World",
                                new String[] {"Bei Bei", "Jing Jing", "Huan Huan", "Ying Ying"})
                        .send();
        System.out.println("Result: " + transactionResponse.toString());

        System.out.println("*************** callInt ***************");
        TransactionResponse transactionResponseInt =
                weCrossRPC.callInt("payment.bcos.HelloWeCross", "getNumber").send();
        System.out.println("Result: " + transactionResponseInt.toString());

        System.out.println("*************** callString ***************");
        TransactionResponse decodeCallTransactionResponse =
                weCrossRPC.callString("payment.bcos.HelloWeCross", "getMessage").send();
        System.out.println("Result: " + decodeCallTransactionResponse.toString());

        System.out.println("*************** callIntArray ***************");
        TransactionResponse transactionResponseIntArray =
                weCrossRPC.callIntArray("payment.bcos.HelloWeCross", "getNumbers").send();
        System.out.println("Result: " + transactionResponseIntArray.toString());

        System.out.println("*************** callStringArray ***************");
        TransactionResponse transactionResponseStringArray =
                weCrossRPC.callStringArray("payment.bcos.HelloWeCross", "getMessages").send();
        System.out.println("Result: " + transactionResponseStringArray.toString());
    }
}
