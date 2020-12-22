package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.common.StatusCode;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.RemoteCall;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.ResourceDetailResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.utils.RPCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Resource {
    private final Logger logger = LoggerFactory.getLogger(Resource.class);
    private WeCrossRPC weCrossRPC;
    private String path;

    Resource(WeCrossRPC weCrossRPC, String path) {
        this.weCrossRPC = weCrossRPC;
        this.path = path;
    }

    public void check() throws WeCrossSDKException {
        checkWeCrossRPC(this.weCrossRPC);
        checkIPath(this.path);
    }

    public boolean isActive() {
        try {
            ResourceResponse resourceResponse =
                    (ResourceResponse) mustOkRequest(weCrossRPC.listResources(false));
            checkResponse(resourceResponse);
            ResourceDetail[] resourceDetails = resourceResponse.getResources().getResourceDetails();
            boolean isActiveFlag = false;
            for (ResourceDetail resourceDetail : resourceDetails) {
                if (resourceDetail.getPath().equals(path)) {
                    isActiveFlag = true;
                    break;
                }
            }
            return isActiveFlag;
        } catch (Exception e) {
            logger.error("Get status exception: ", e);
            return false;
        }
    }

    public ResourceDetail detail() throws WeCrossSDKException {
        ResourceDetailResponse response =
                (ResourceDetailResponse) mustOkRequest(weCrossRPC.detail(path));
        checkResponse(response);
        return response.getData();
    }

    public String[] call(String method) throws WeCrossSDKException {
        return call(method, (String[]) null);
    }

    public String[] call(String method, String... args) throws WeCrossSDKException {
        TransactionResponse response =
                (TransactionResponse) mustOkRequest(weCrossRPC.call(path, method, args));
        checkResponse(response);
        Receipt receipt = response.getReceipt();
        if (receipt.getErrorCode() != StatusCode.SUCCESS) {
            throw new WeCrossSDKException(
                    ErrorCode.CALL_CONTRACT_ERROR,
                    "Resource.call fail, receipt:" + receipt.toString());
        }
        return receipt.getResult();
    }

    public String[] sendTransaction(String method) throws WeCrossSDKException {
        return sendTransaction(method, (String[]) null);
    }

    public String[] sendTransaction(String method, String... args) throws WeCrossSDKException {
        TransactionResponse response =
                (TransactionResponse) mustOkRequest(weCrossRPC.sendTransaction(path, method, args));
        checkResponse(response);
        Receipt receipt = response.getReceipt();
        if (receipt.getErrorCode() != StatusCode.SUCCESS) {
            throw new WeCrossSDKException(
                    ErrorCode.CALL_CONTRACT_ERROR,
                    "Resource.sendTransaction fail, receipt:" + receipt.toString());
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

    private Response<?> mustOkRequest(RemoteCall<?> call) throws WeCrossSDKException {
        try {
            return call.send();
        } catch (Exception e) {
            logger.error("Error in RemoteCall: {}", e.getMessage(), e);
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
}
