package com.webank.wecrosssdk.mock;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.common.Resources;
import com.webank.wecrosssdk.rpc.common.Stubs;
import com.webank.wecrosssdk.rpc.common.account.BCOSAccount;
import com.webank.wecrosssdk.rpc.common.account.ChainAccount;
import com.webank.wecrosssdk.rpc.common.account.UniversalAccount;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.*;
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
            case "listAccount":
                return (T) handleListAccounts(request);
            case "listResources":
                return (T) handleListResources(request);
            case "call":
                return (T) handleCall(request);
            case "sendTransaction":
                return (T) handleSendTransaction(request);
            case "startTransaction":
                return (T) handleStartTransaction(request);
            case "commitTransaction":
                return (T) handleCommitTransaction(request);
            case "rollbackTransaction":
                return (T) handleRollbackTransaction(request);
            case "getTransactionInfo":
                return (T) handleGetTransactionInfo(request);
            case "customCommand":
                return (T) handleCustomCommand(request);
            case "getTransactionIDs":
                return (T) handleGetTransactionIDs(request);
            default:
                return handleMethodNotFound(request, responseType);
        }
    }

    @Override
    public <T extends Response> void asyncSend(
            Request<?> request, Class<T> responseType, Callback<T> callback) {
        return;
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
        UniversalAccount account = new UniversalAccount();
        List<ChainAccount> list = new ArrayList<>();
        ChainAccount chainAccount = new BCOSAccount(1, "BCOS2.0", "XXX", "XXX", "XXX", true);
        list.add(chainAccount);
        account.setChainAccounts(list);
        response.setErrorCode(0);
        response.setAccount(account);
        return response;
    }

    public ResourceResponse handleListResources(Request request) {
        ResourceResponse response = new ResourceResponse();
        Resources resources = new Resources();
        resources.setResourceDetails(new ResourceDetail[1]);
        response.setErrorCode(0);
        response.setResources(resources);
        return response;
    }

    public TransactionResponse handleCall(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setErrorCode(0);
        response.setData(receipt);
        return response;
    }

    public TransactionResponse handleSendTransaction(Request request) {
        TransactionResponse response = new TransactionResponse();
        response.setErrorCode(0);
        Receipt receipt = new Receipt();
        receipt.setErrorCode(0);
        response.setData(receipt);
        return response;
    }

    public RoutineResponse handleStartTransaction(Request request) {
        RoutineResponse response = new RoutineResponse();
        response.setErrorCode(0);
        response.setResult(0);
        return response;
    }

    public RoutineResponse handleCommitTransaction(Request request) {
        RoutineResponse response = new RoutineResponse();
        response.setErrorCode(0);
        response.setResult(0);
        return response;
    }

    public RoutineResponse handleRollbackTransaction(Request request) {
        RoutineResponse response = new RoutineResponse();
        response.setErrorCode(0);
        response.setResult(0);
        return response;
    }

    public RoutineInfoResponse handleGetTransactionInfo(Request request) {
        RoutineInfoResponse response = new RoutineInfoResponse();
        response.setErrorCode(0);
        response.setInfo("success");
        return response;
    }

    public CommandResponse handleCustomCommand(Request request) {
        CommandResponse response = new CommandResponse();
        response.setErrorCode(0);
        response.setResult("success");
        return response;
    }

    public RoutineIDResponse handleGetTransactionIDs(Request request) {
        RoutineIDResponse response = new RoutineIDResponse();
        response.setErrorCode(0);
        response.setIDs(new String[] {"001"});
        return response;
    }

    public <T extends Response> T handleMethodNotFound(Request request, Class<T> responseType) {
        Response response = new Response();
        response.setErrorCode(-1);
        response.setMessage("Method not found");
        return (T) response;
    }
}
