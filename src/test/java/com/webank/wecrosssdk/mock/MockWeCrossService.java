package com.webank.wecrosssdk.mock;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.Accounts;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.common.Resources;
import com.webank.wecrosssdk.rpc.common.Stubs;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.AccountResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceDetailResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceResponse;
import com.webank.wecrosssdk.rpc.methods.response.StubResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            case "detail":
                return (T) handleDetail(request);
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
        response.setErrorCode(0);
        response.setData("test");
        return response;
    }

    public ResourceDetailResponse handleDetail(Request request) {
        ResourceDetailResponse response = new ResourceDetailResponse();
        response.setErrorCode(0);
        response.setData(new ResourceDetail());
        return response;
    }

    public StubResponse handleSupportedStubs(Request request) {
        StubResponse response = new StubResponse();
        Stubs stubs = new Stubs();
        stubs.setStubTypes(new String[] {"BCOS", "Fabric"});
        response.setErrorCode(0);
        response.setStubs(stubs);
        return response; // Use Mockito to define handler
    }

    public AccountResponse handleListAccounts(Request request) {
        AccountResponse response = new AccountResponse();
        Accounts accounts = new Accounts();
        List<Map<String, String>> accountInfos = new ArrayList<>();
        accountInfos.add((Map<String, String>) new HashMap<>().put("name", "bcos"));
        accounts.setAccountInfos(accountInfos);
        response.setErrorCode(0);
        response.setAccounts(accounts);
        return response; // Use Mockito to define handler
    }

    public ResourceResponse handleListResources(Request request) {
        ResourceResponse response = new ResourceResponse();
        Resources resources = new Resources();
        resources.setResourceDetails(new ResourceDetail[1]);
        response.setErrorCode(0);
        response.setResources(resources);
        return response; // Use Mockito to define handler
    }

    public TransactionResponse handleCall(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setErrorCode(0);
        response.setData(receipt);
        return response; // Use Mockito to define handler
    }

    public TransactionResponse handleSendTransaction(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setErrorCode(0);
        response.setData(receipt);
        return response; // Use Mockito to define handler
    }

    public <T extends Response> T handleMethodNotFound(Request request, Class<T> responseType) {
        Response response = new Response();
        response.setErrorCode(-1);
        response.setMessage("Method not found");
        return (T) response;
    }
}
