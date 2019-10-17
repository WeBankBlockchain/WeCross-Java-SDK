package com.webank.wecrosssdk.methods;

import com.webank.wecrosssdk.rpc.WeCrossRpc;
import com.webank.wecrosssdk.rpc.WeCrossRpcService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeCrossRpcRest implements WeCrossRpc {

    private WeCrossRpcService weCrossRpcService;

    public WeCrossRpcRest(WeCrossRpcService weCrossRpcService) {
        this.weCrossRpcService = weCrossRpcService;
    }

    @Override
    public RemoteCall<Response> exists(String path) {
        //    String prefix = weCrossRpcService.getWeCrossServer();
        //    String url = Path.pathToUrl(prefix, path);
        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, "exists", "", null);
        return new RemoteCall<>(weCrossRpcService, Response.class, request);
    }

    @Override
    public RemoteCall<Response> call(String path, String method, Object... args) {
        return getCallContractRemoteCall(path, "call", method, args);
    }

    @Override
    public RemoteCall<Response> sendTransaction(String path, String method, Object... args) {
        return getCallContractRemoteCall(path, "sendTransaction", method, args);
    }

    private RemoteCall<Response> getCallContractRemoteCall(
            String path, String func, String method, Object[] args) {
        //    String prefix = weCrossRpcService.getWeCrossServer();
        //    String url = Path.pathToUrl(prefix, path);

        Map<String, Object> data = new HashMap<>();
        List<Object> listArgs = Arrays.<Object>asList(args);
        data.put("method", method);
        data.put("args", listArgs);

        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, func, "", data);
        return new RemoteCall<>(weCrossRpcService, Response.class, request);
    }

    public WeCrossRpcService getWeCrossRpcService() {
        return weCrossRpcService;
    }

    public void setWeCrossRpcService(WeCrossRpcService weCrossRpcService) {
        this.weCrossRpcService = weCrossRpcService;
    }
}
