package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.methods.RemoteCall;
import com.webank.wecrosssdk.methods.Response;
import com.webank.wecrosssdk.methods.WeCrossRpcRest;

public interface WeCrossRpc<T> {

    static WeCrossRpc init(WeCrossRpcService weCrossRpcService) {
        return new WeCrossRpcRest(weCrossRpcService);
    }

    RemoteCall<Response> exists(String path);

    RemoteCall<Response> call(String path, String method, Object... args);

    RemoteCall<Response> sendTransaction(String path, String method, Object... args);
}
