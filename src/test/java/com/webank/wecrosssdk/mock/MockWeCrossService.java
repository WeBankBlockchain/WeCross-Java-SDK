package com.webank.wecrosssdk.mock;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.common.ResourceInfo;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.AccountResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceInfoResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceResponse;
import com.webank.wecrosssdk.rpc.methods.response.StubResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.util.HashMap;
import java.util.Map;

public class MockWeCrossService implements WeCrossService {
    private Map<String, String> datas = new HashMap<>();

    @Override
    public void init() throws WeCrossSDKException {}

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws Exception {
        switch (request.getMethod()) {
            case "status":
                return (T) handleStatus(request);
            case "info":
                return (T) handleInfo(request);
            case "supportedStubs":
                return (T) handleSupportedStubs(request);
            case "listAccounts":
                return (T) handleListAccounts(request);
            case "listResources":
                return (T) handleListResources(request);
            case "call":
                return (T) handleCall(request);
            case "sendTransaction":
                return (T) handleSendTransaction(request);
            default:
                return handleMethodNotFound(request, responseType);
        }
    }

    public Response handleStatus(Request request) {
        Response response = new Response();
        response.setResult(0);
        response.setData("test");
        return response;
    }

    public ResourceInfoResponse handleInfo(Request request) {
        ResourceInfoResponse response = new ResourceInfoResponse();
        response.setResult(0);
        response.setData(new ResourceInfo());
        return response;
    }

    public StubResponse handleSupportedStubs(Request request) {
        StubResponse response = new StubResponse();
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public AccountResponse handleListAccounts(Request request) {
        AccountResponse response = new AccountResponse();
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public ResourceResponse handleListResources(Request request) {
        ResourceResponse response = new ResourceResponse();
        response.setResult(0);
        return response; // Use Mockito to define handler
    }

    public TransactionResponse handleCall(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setResult(0);
        response.setData(new Receipt());
        return response; // Use Mockito to define handler
    }

    public TransactionResponse handleSendTransaction(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setResult(0);
        response.setData(new Receipt());
        return response; // Use Mockito to define handler
    }

    public <T extends Response> T handleMethodNotFound(Request request, Class<T> responseType) {
        Response response = new Response();
        response.setResult(-1);
        response.setMessage("Method not found");
        return (T) response;
    }
}
