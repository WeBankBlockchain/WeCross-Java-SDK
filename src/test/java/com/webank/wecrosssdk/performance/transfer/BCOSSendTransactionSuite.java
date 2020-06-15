package com.webank.wecrosssdk.performance.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.math.BigInteger;

public class BCOSSendTransactionSuite implements PerformanceSuite {
    private Resource resource;
    private DagUserMgr dagUserMgr = null;
    private TypeReference<?> typeReference = new TypeReference<TransactionResponse>() {};
    private ObjectMapper objectMapper = new ObjectMapper();

    public BCOSSendTransactionSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        /*
        try {
            resource.sendTransaction("transfer", data);
        } catch (WeCrossSDKException e) {
            System.out.println("Invalid contract or user or db: " + e.getMessage());
            System.out.println(
                    "\tRollback: Check the HelloWecross address configuration in stub.toml");
            System.out.println("\tUser type: check the user type is BCOS2.0");
            System.out.println(
                    "\tBlocklimit failed: check the router is syncing or couldn't be the next block");
            throw new WeCrossSDKException(ErrorCode.INVALID_CONTRACT, "Init contract failed");
        }
        */

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "BCOS Send Transaction Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            resource.getWeCrossRPC()
                    .sendTransaction(
                            resource.getPath(),
                            resource.getAccountName(),
                            "transfer",
                            dagUserMgr.getFrom(index).getUser(),
                            dagUserMgr.getTo(index).getUser(),
                            BigInteger.valueOf(1).toString(10))
                    .asyncSend(
                            new Callback<TransactionResponse>() {
                                @Override
                                public void onSuccess(TransactionResponse response) {
                                    if (response.getReceipt().getErrorCode() == 0) {
                                        callback.onSuccess("");
                                    } else {
                                        callback.onFailed("transaction execution failed");
                                    }
                                }

                                @Override
                                public void onFailed(WeCrossSDKException e) {
                                    callback.onFailed(e.getMessage());
                                }
                            });

        } catch (Exception e) {
            callback.onFailed("sendTransaction exception: " + e.getMessage());
        }
    }

    public DagUserMgr getDagUserMgr() {
        return dagUserMgr;
    }

    public void setDagUserMgr(DagUserMgr dagUserMgr) {
        this.dagUserMgr = dagUserMgr;
    }
}
