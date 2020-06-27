package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.common.StatusCode;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.RemoteCall;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.ResourceDetailResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.utils.RPCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
    private Logger logger = LoggerFactory.getLogger(Resource.class);
    private WeCrossRPC weCrossRPC;
    private String path;
    private String account;

    // Use given account to send transaction
    Resource(WeCrossRPC weCrossRPC, String path, String account) {
        this.weCrossRPC = weCrossRPC;
        this.path = path;
        this.account = account;
    }

    public void check() throws WeCrossSDKException {
        checkWeCrossRPC(this.weCrossRPC);
        checkIPath(this.path);
        checkAccountName(this.account);
    }

    public boolean isActive() {
        try {
            return status().equals("exists");
        } catch (Exception e) {
            logger.error("Get status exception: " + e);
            return false;
        }
    }

    public String status() throws WeCrossSDKException {
        Response<String> response = (Response<String>) mustOkRequest(weCrossRPC.status(path));
        checkResponse(response);
        return response.getData();
    }

    public ResourceDetail detail() throws WeCrossSDKException {
        ResourceDetailResponse response =
                (ResourceDetailResponse) mustOkRequest(weCrossRPC.detail(path));
        checkResponse(response);
        return response.getData();
    }

    public TransactionResponse call(Request<TransactionRequest> request)
            throws WeCrossSDKException {
        return (TransactionResponse) mustOkRequest(weCrossRPC.call(request));
    }

    public String[] call(String method) throws WeCrossSDKException {
        return call(method, null);
    }

    public String[] call(String method, String... args) throws WeCrossSDKException {
        TransactionResponse response =
                (TransactionResponse) mustOkRequest(weCrossRPC.call(path, account, method, args));
        checkResponse(response);
        Receipt receipt = response.getReceipt();
        if (receipt.getErrorCode() != StatusCode.SUCCESS) {
            throw new WeCrossSDKException(ErrorCode.CALL_CONTRACT_ERROR, receipt.toString());
        }
        return receipt.getResult();
    }

    public TransactionResponse sendTransaction(Request<TransactionRequest> request)
            throws WeCrossSDKException {
        return (TransactionResponse) mustOkRequest(weCrossRPC.sendTransaction(request));
    }

    public String[] sendTransaction(String method) throws WeCrossSDKException {
        return sendTransaction(method, null);
    }

    public String[] sendTransaction(String method, String... args) throws WeCrossSDKException {
        TransactionResponse response =
                (TransactionResponse)
                        mustOkRequest(weCrossRPC.sendTransaction(path, account, method, args));
        checkResponse(response);
        Receipt receipt = response.getReceipt();
        if (receipt.getErrorCode() != StatusCode.SUCCESS) {
            throw new WeCrossSDKException(ErrorCode.CALL_CONTRACT_ERROR, receipt.toString());
        }
        return receipt.getResult();
    }

    private void checkWeCrossRPC(WeCrossRPC weCrossRPC) throws WeCrossSDKException {
        if (weCrossRPC == null) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_ERROR, "WeCrossRPC not set");
        }
    }

    private void checkIPath(String path) throws WeCrossSDKException {
        RPCUtils.checkPath(path);
    }

    private void checkAccountName(String accountName) throws WeCrossSDKException {
        if (accountName == null || accountName.equals("")) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_ERROR, "AccountName not set");
        }
    }

    private Response<?> mustOkRequest(RemoteCall<?> call) throws WeCrossSDKException {
        try {
            return call.send();
        } catch (Exception e) {
            logger.error("Error in RemoteCall: {}, exception: {}", e.getMessage(), e);
            throw new WeCrossSDKException(ErrorCode.REMOTECALL_ERROR, e.getMessage());
        }
    }

    private void checkResponse(Response<?> response) throws WeCrossSDKException {
        if (response == null) {
            throw new WeCrossSDKException(ErrorCode.RPC_ERROR, "response is null");
        } else if (response.getErrorCode() != StatusCode.SUCCESS || response.getData() == null) {
            throw new WeCrossSDKException(ErrorCode.RPC_ERROR, response.toString());
        }
    }

    public WeCrossRPC getWeCrossRPC() {
        return weCrossRPC;
    }

    public void setWeCrossRPC(WeCrossRPC weCrossRPC) {
        this.weCrossRPC = weCrossRPC;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
