package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.response.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public interface WeCrossRPC {

    static WeCrossRPC init(WeCrossService weCrossService) {
        return new WeCrossRPCRest(weCrossService);
    }

    RemoteCall<Response> exists(String path);

    RemoteCall<ResourcesResponse> list(Boolean ignoreRemote);

    RemoteCall<GetDataResponse> getData(String path, String key);

    RemoteCall<SetDataResponse> setData(String path, String key, String value);

    RemoteCall<TransactionResponse> call(String path, String method, Object... args);

    RemoteCall<TransactionResponse> call(
            String path, String retTypes[], String method, Object... args);

    RemoteCall<TransactionResponse> callInt(String path, String method, Object... args);

    RemoteCall<TransactionResponse> callIntArray(String path, String method, Object... args);

    RemoteCall<TransactionResponse> callString(String path, String method, Object... args);

    RemoteCall<TransactionResponse> callStringArray(String path, String method, Object... args);

    RemoteCall<TransactionResponse> sendTransaction(String path, String method, Object... args);

    RemoteCall<TransactionResponse> sendTransaction(
            String path, String retTypes[], String method, Object... args);

    RemoteCall<TransactionResponse> sendTransactionInt(String path, String method, Object... args);

    RemoteCall<TransactionResponse> sendTransactionIntArray(
            String path, String method, Object... args);

    RemoteCall<TransactionResponse> sendTransactionString(
            String path, String method, Object... args);

    RemoteCall<TransactionResponse> sendTransactionStringArray(
            String path, String method, Object... args);
}
