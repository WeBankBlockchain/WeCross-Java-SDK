package com.webank.wecrosssdk.console.mock;

import com.webank.wecrosssdk.console.common.ConsoleUtils;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;
import java.io.Serializable;

public class MockResource implements Serializable {
    public String path;
    private WeCrossRPC weCrossRPC;

    public MockResource(String path, WeCrossRPC weCrossRPC) {
        this.path = path;
        this.weCrossRPC = weCrossRPC;
    }

    public void exists() {
        try {
            Response response = weCrossRPC.exists(path).send();
            if (response.getResult() != 0) {
                System.out.println(response.toString());
                return;
            }
            System.out.println(path + " : " + response.getData());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void call(String method, Object args[]) {
        try {
            if (args.length == 0) {
                TransactionResponse callTransactionResponse = weCrossRPC.call(path, method).send();
                System.out.println(callTransactionResponse.getCallContractResult().toString());
                return;
            }

            TransactionResponse transactionResponse = weCrossRPC.call(path, method, args).send();

            if (transactionResponse.getResult() != 0) {
                System.out.println(transactionResponse.toString());
                return;
            }

            String result = transactionResponse.getCallContractResult().toString();
            ConsoleUtils.printJson(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendTransaction(String method, Object args[]) {
        try {
            if (args.length == 0) {
                TransactionResponse callTransactionResponse =
                        weCrossRPC.sendTransaction(path, method).send();
                System.out.println(callTransactionResponse.getCallContractResult().toString());
                return;
            }

            TransactionResponse transactionResponse =
                    weCrossRPC.sendTransaction(path, method, args).send();

            if (transactionResponse.getResult() != 0) {
                System.out.println(transactionResponse.toString());
                return;
            }

            String result = transactionResponse.getCallContractResult().toString();
            ConsoleUtils.printJson(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public WeCrossRPC getWeCrossRPC() {
        return weCrossRPC;
    }

    public void setWeCrossRPC(WeCrossRPC weCrossRPC) {
        this.weCrossRPC = weCrossRPC;
    }
}
