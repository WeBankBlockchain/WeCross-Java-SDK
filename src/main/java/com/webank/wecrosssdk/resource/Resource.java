package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.RemoteCall;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.ResourceInfoResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.utils.RPCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
    private Logger logger = LoggerFactory.getLogger(Resource.class);
    private WeCrossRPC weCrossRPC;
    private String path;
    private String accountName;

    // Use given account to send transaction
    Resource(WeCrossRPC weCrossRPC, String path, String accountName) {
        this.weCrossRPC = weCrossRPC;
        this.path = path;
        this.accountName = accountName;
    }

    public void check() throws WeCrossSDKException {
        checkWeCrossRPC(this.weCrossRPC);
        checkIPath(this.path);
        checkAccountName(this.accountName);
    }

    public Response<String> status() throws WeCrossSDKException {
        return (Response<String>) mustOkRequest(weCrossRPC.status(path));
    }

    public ResourceInfoResponse info() throws WeCrossSDKException {
        return (ResourceInfoResponse) mustOkRequest(weCrossRPC.info(path));
    }

    public TransactionResponse call(String method, String... args) throws WeCrossSDKException {
        return (TransactionResponse)
                mustOkRequest(weCrossRPC.call(path, accountName, method, args));
    }

    public TransactionResponse sendTransaction(String method, String... args) throws Exception {
        return (TransactionResponse)
                mustOkRequest(weCrossRPC.sendTransaction(path, accountName, method, args));
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
            logger.error("Error in RemoteCall: " + e.getMessage());
            throw new WeCrossSDKException(ErrorCode.REMOTECALL_ERROR, e.getMessage());
        }
    }
}
