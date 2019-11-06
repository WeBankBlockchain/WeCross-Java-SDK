package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import java.util.concurrent.CompletableFuture;

public class RemoteCall<T extends Response> {

    private WeCrossService weCrossService;

    private Class<T> responseType;

    private Request request;

    public RemoteCall(WeCrossService weCrossService, Class<T> responseType, Request request) {
        this.weCrossService = weCrossService;
        this.responseType = responseType;
        this.request = request;
    }

    public T send() throws Exception {
        return weCrossService.send(request, responseType);
    }

    public void sendOnly() {
        weCrossService.sendOnly(request);
    }

    public CompletableFuture<T> sendAsync() {
        return weCrossService.sendAsync(request, responseType);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
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
