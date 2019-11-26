package com.webank.wecrosssdk.console.mock;

import com.webank.wecrosssdk.console.common.CallResult;
import com.webank.wecrosssdk.console.common.ConsoleUtils;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.data.CallContractResult;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
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

    public void call(String retTypes, String method, Object... args) {
        try {
            String types[] = retTypes.split(",");
            if (types[0].equals("Void")) {
                types = null;
            }
            if (args.length == 0) {
                TransactionResponse transactionResponse =
                        weCrossRPC.call(path, types, method).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    return;
                }
                String result = transactionResponse.getCallContractResult().toString();
                ConsoleUtils.printJson(result);
            } else {
                TransactionResponse transactionResponse =
                        weCrossRPC.call(path, types, method, args).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    System.out.println();
                    return;
                }
                if (types.length == 1) {
                    CallContractResult callContractResult =
                            transactionResponse.getCallContractResult();
                    CallResult callResult =
                            new CallResult(
                                    callContractResult.getErrorCode(),
                                    callContractResult.getErrorMessage());
                    if (callContractResult.getErrorCode() == 0) {
                        callResult.setResult(callContractResult.getResult()[0]);
                    }
                    ConsoleUtils.printJson(callResult.toString());
                } else {
                    String result = transactionResponse.getCallContractResult().toString();
                    ConsoleUtils.printJson(result);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void callInt(String method, Object... args) {
        call("Int", method, args);
    }

    public void callIntArray(String method, Object... args) {
        call("IntArray", method, args);
    }

    public void callString(String method, Object... args) {
        call("String", method, args);
    }

    public void callStringArray(String method, Object... args) {
        call("StringArray", method, args);
    }

    public void sendTransaction(String retTypes, String method, Object... args) {
        try {
            String types[] = retTypes.split(",");
            if (types[0].equals("Void")) {
                types = null;
            }
            if (args.length == 0) {
                TransactionResponse transactionResponse =
                        weCrossRPC.sendTransaction(path, types, method).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    return;
                }
                String result = transactionResponse.getCallContractResult().toString();
                ConsoleUtils.printJson(result);
            } else {
                TransactionResponse transactionResponse =
                        weCrossRPC.sendTransaction(path, types, method, args).send();

                if (transactionResponse.getResult() != 0) {
                    System.out.println(transactionResponse.toString());
                    System.out.println();
                    return;
                }
                if (types.length == 1) {
                    CallContractResult callContractResult =
                            transactionResponse.getCallContractResult();
                    CallResult callResult =
                            new CallResult(
                                    callContractResult.getErrorCode(),
                                    callContractResult.getErrorMessage());
                    if (callContractResult.getErrorCode() == 0) {
                        callResult.setResult(callContractResult.getResult()[0]);
                    }
                    ConsoleUtils.printJson(callResult.toString());
                } else {
                    String result = transactionResponse.getCallContractResult().toString();
                    ConsoleUtils.printJson(result);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }

    public void sendTransactionInt(String method, Object... args) {
        sendTransaction("Int", method, args);
    }

    public void sendTransactionIntArray(String method, Object... args) {
        sendTransaction("IntArray", method, args);
    }

    public void sendTransactionString(String method, Object... args) {
        sendTransaction("String", method, args);
    }

    public void sendTransactionStringArray(String method, Object... args) {
        sendTransaction("StringArray", method, args);
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
