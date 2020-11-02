package com.webank.wecrosssdk.rpc.service;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;

public interface WeCrossService {

    void init() throws WeCrossSDKException;

    <T extends Response> T send(String uri, Request request, Class<T> responseType)
            throws Exception;

    <T extends Response> void asyncSend(
            String uri, Request<?> request, Class<T> responseType, Callback<T> callback);
}
