package com.webank.wecrosssdk.console.mock;

import com.webank.wecrosssdk.console.common.ConsoleUtils;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.SetDataResponse;
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
                System.out.println();
                return;
            }
            System.out.println(path + " : " + response.getData());
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void getData(String key) {
        try {
            GetDataResponse getDataResponse = weCrossRPC.getData(path, key).send();
            if (getDataResponse.getResult() != 0) {
                System.out.println(getDataResponse.toString());
                System.out.println();
                return;
            }
            String result = getDataResponse.getStatusAndValue().toString();
            ConsoleUtils.printJson(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void setData(String key, String value) {
        try {
            SetDataResponse setDataResponse = weCrossRPC.setData(path, key, value).send();
            if (setDataResponse.getResult() != 0) {
                System.out.println(setDataResponse.toString());
                System.out.println();
                return;
            }
            String result = setDataResponse.getStatus().toString();
            ConsoleUtils.printJson(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void call(String method, Object... args) {
        try {
            if (args.length == 0) {
                TransactionResponse transactionResponse = weCrossRPC.call(path, method).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    return;
                }
                String result = transactionResponse.getCallContractResult().toString();
                ConsoleUtils.printJson(result);
            } else {
                TransactionResponse transactionResponse =
                        weCrossRPC.call(path, method, args).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    System.out.println();
                    return;
                }
                String result = transactionResponse.getCallContractResult().toString();
                ConsoleUtils.printJson(result);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void sendTransaction(String method, Object... args) {
        try {
            if (args.length == 0) {
                TransactionResponse transactionResponse =
                        weCrossRPC.sendTransaction(path, method).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    return;
                }
                String result = transactionResponse.getCallContractResult().toString();
                ConsoleUtils.printJson(result);
            } else {
                TransactionResponse transactionResponse =
                        weCrossRPC.sendTransaction(path, method, args).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    System.out.println();
                    return;
                }
                String result = transactionResponse.getCallContractResult().toString();
                ConsoleUtils.printJson(result);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
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
