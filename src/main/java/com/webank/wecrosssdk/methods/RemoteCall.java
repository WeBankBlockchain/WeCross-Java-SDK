package com.webank.wecrosssdk.methods;

import com.webank.wecrosssdk.rpc.WeCrossRpcService;
import java.util.concurrent.CompletableFuture;

public class RemoteCall<T extends Response> {

    private WeCrossRpcService weCrossRpcService;

    private Class<T> responseType;

    private Request request;

    public RemoteCall(WeCrossRpcService weCrossRpcService, Class<T> responseType, Request request) {
        this.weCrossRpcService = weCrossRpcService;
        this.responseType = responseType;
        this.request = request;
    }

    public T send() {
        return weCrossRpcService.send(request, responseType);
    }

    public void sendOnly() {
        weCrossRpcService.sendOnly(request);
    }

    public CompletableFuture<T> sendAsync() {
        return weCrossRpcService.sendAsync(request, responseType);
    }

    public WeCrossRpcService getWeCrossRpcService() {
        return weCrossRpcService;
    }

    public void setWeCrossRpcService(WeCrossRpcService weCrossRpcService) {
        this.weCrossRpcService = weCrossRpcService;
    }

    public Class<T> getResponseType() {
        return responseType;
    }

    public void setResponseType(Class<T> responseType) {
        this.responseType = responseType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
