package com.webank.wecrosssdk.performance.Fabric;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.rpc.methods.Callback;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.security.SecureRandom;

public class FabricSendTransactionSuite implements PerformanceSuite {
    private Resource resource;
    private TypeReference<?> typeReference = new TypeReference<TransactionResponse>() {};
    private ObjectMapper objectMapper = new ObjectMapper();
    static final int BOUND = Integer.MAX_VALUE - 1;
    SecureRandom rand = new SecureRandom();

    public FabricSendTransactionSuite(Resource resource) throws WeCrossSDKException {
        if (!resource.isActive()) {
            throw new WeCrossSDKException(
                    ErrorCode.RESOURCE_INACTIVE,
                    "Resource sacc inactive, please check router has started and deploy it by:\n"
                            + "\tdocker exec -it cli peer chaincode install -n sacc -v 1.0 -p github.com/chaincode/sacc/\n"
                            + "\tdocker exec -it cli peer chaincode instantiate -o orderer.example.com:7050 --tls true --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n sacc -l golang -v 1.0 -c '{\"Args\":[\"a\",\"10\"]}' -P 'OR ('\\''Org1MSP.peer'\\'','\\''Org2MSP.peer'\\'')'");
        }

        try {
            resource.call("query", "a");
        } catch (WeCrossSDKException e) {
            throw new WeCrossSDKException(
                    ErrorCode.INVALID_CONTRACT, "Invalid contract or user: " + e.getMessage());
        }

        this.resource = resource;
    }

    @Override
    public String getName() {
        return "Fabric Send Transaction Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            resource.getWeCrossRPC()
                    .sendTransaction(
                            resource.getPath(),
                            resource.getAccount(),
                            "set",
                            String.valueOf(rand.nextInt(BOUND)),
                            String.valueOf(rand.nextInt(BOUND)))
                    .asyncSend(
                            new Callback<TransactionResponse>() {
                                @Override
                                public void onSuccess(TransactionResponse response) {
                                    if (response.getReceipt().getErrorCode() == 0) {
                                        callback.onSuccess("success");
                                    } else {
                                        callback.onFailed("failed");
                                    }
                                }

                                @Override
                                public void onFailed(WeCrossSDKException e) {
                                    callback.onFailed(e.getMessage());
                                }
                            });

        } catch (Exception e) {
            callback.onFailed(e.getMessage());
        }
    }
}
