package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeCrossRPCRest implements WeCrossRPC {

    private WeCrossService weCrossService;

    public WeCrossRPCRest(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }

    @Override
    public RemoteCall<Response> exists(String path) {
        //    String prefix = weCrossService.getWeCrossServer();
        //    String url = Path.pathToUrl(prefix, path);
        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, "exists", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
    }

    @Override
    public RemoteCall<ResourcesResponse> list(Boolean ignoreRemote) {
        Map<String, Object> data = new HashMap<>();
        data.put("ignoreRemote", ignoreRemote);

        @SuppressWarnings("unchecked")
        Request<?> request = new Request("", "list", data);
        return new RemoteCall<ResourcesResponse>(weCrossService, ResourcesResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(String path, String method, Object... args) {
        Map<String, Object> data = new HashMap<>();
        List<Object> listArgs = Arrays.<Object>asList(args);
        data.put("sig", "");
        data.put("method", method);
        data.put("args", listArgs);

        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, "call", data);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String method, Object... args) {
        Map<String, Object> data = new HashMap<>();
        List<Object> listArgs = Arrays.<Object>asList(args);
        data.put("sig", "");
        data.put("method", method);
        data.put("args", listArgs);

        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, "sendTransaction", data);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    public WeCrossService getWeCrossService() {
        return weCrossService;
    }

    public void setWeCrossService(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }
}
