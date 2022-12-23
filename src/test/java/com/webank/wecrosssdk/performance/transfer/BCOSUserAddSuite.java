package com.webank.wecrosssdk.performance.transfer;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.math.BigInteger;

public class BCOSUserAddSuite implements PerformanceSuite {
    private Resource resource;
    private DagUserMgr dagUserMgr = null;

    public BCOSUserAddSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(ErrorCode.RESOURCE_INACTIVE, "Resource inactive");
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "BCOS User Add Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            resource.getWeCrossRPC()
                    .sendTransaction(
                            resource.getPath(),
                            "set",
                            dagUserMgr.getFrom(index).getUser(),
                            BigInteger.valueOf(1000000000).toString(10))
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
