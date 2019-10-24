package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;

public interface WeCrossRPC {

    static WeCrossRPC init(WeCrossService weCrossService) {
        return new WeCrossRPCRest(weCrossService);
    }

    RemoteCall<Response> exists(String path);

    RemoteCall<ResourcesResponse> list(Boolean ignoreRemote);

    RemoteCall<TransactionResponse> call(String path, String method, Object... args);

    RemoteCall<TransactionResponse> sendTransaction(String path, String method, Object... args);
}
