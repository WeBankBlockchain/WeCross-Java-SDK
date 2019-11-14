package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.GetDataRequest;
import com.webank.wecrosssdk.rpc.methods.GetDataResponse;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.ResourcesRequest;
import com.webank.wecrosssdk.rpc.methods.ResourcesResponse;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.SetDataRequest;
import com.webank.wecrosssdk.rpc.methods.SetDataResponse;
import com.webank.wecrosssdk.rpc.methods.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class WeCrossRPCRest implements WeCrossRPC {

    private WeCrossService weCrossService;

    public WeCrossRPCRest(WeCrossService weCrossService) {
        this.weCrossService = weCrossService;
    }

    @Override
    public RemoteCall<Response> exists(String path) {
        //    String prefix = weCrossService.getWeCrossServer();
        //    String url = RPCUtils.pathToUrl(prefix, path);
        @SuppressWarnings("unchecked")
        Request<?> request = new Request(path, "exists", null);
        return new RemoteCall<>(weCrossService, Response.class, request);
    }

    @Override
    public RemoteCall<ResourcesResponse> list(Boolean ignoreRemote) {
        ResourcesRequest resourcesRequest = new ResourcesRequest(ignoreRemote);

        @SuppressWarnings("unchecked")
        Request<ResourcesRequest> request = new Request("", "list", resourcesRequest);
        return new RemoteCall<ResourcesResponse>(weCrossService, ResourcesResponse.class, request);
    }

    @Override
    public RemoteCall<GetDataResponse> getData(String path, String key) {
        GetDataRequest getDataRequest = new GetDataRequest(key);

        @SuppressWarnings("unchecked")
        Request<GetDataRequest> request = new Request(path, "getData", getDataRequest);
        return new RemoteCall<GetDataResponse>(weCrossService, GetDataResponse.class, request);
    }

    @Override
    public RemoteCall<SetDataResponse> setData(String path, String key, String value) {
        SetDataRequest setDataRequest = new SetDataRequest(key, value);

        @SuppressWarnings("unchecked")
        Request<SetDataRequest> request = new Request(path, "setData", setDataRequest);
        return new RemoteCall<SetDataResponse>(weCrossService, SetDataResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> call(String path, String method, Object... args) {
        TransactionRequest transactionRequest = new TransactionRequest("", method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request = new Request(path, "call", transactionRequest);
        return new RemoteCall<TransactionResponse>(
                weCrossService, TransactionResponse.class, request);
    }

    @Override
    public RemoteCall<TransactionResponse> sendTransaction(
            String path, String method, Object... args) {
        TransactionRequest transactionRequest = new TransactionRequest("", method, args);

        @SuppressWarnings("unchecked")
        Request<TransactionRequest> request =
                new Request(path, "sendTransaction", transactionRequest);
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
