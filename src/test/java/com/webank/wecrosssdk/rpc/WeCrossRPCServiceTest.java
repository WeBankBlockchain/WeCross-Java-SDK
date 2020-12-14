package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.common.Version;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.Connection;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import org.junit.Assert;
import org.junit.Test;

public class WeCrossRPCServiceTest {
    @Test
    public void initTest() {
        try {
            WeCrossRPCService weCrossService = new WeCrossRPCService();
            Connection connection = new Connection();
            connection.setServer("server");
            connection.setCaCert("caCrt");
            connection.setSslCert("sslCrt");
            connection.setSslKey("sslKey");
            WeCrossRPCFactory.build(weCrossService);
        } catch (WeCrossSDKException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void sendTest() {
        Request request = new Request<>();
        request.setVersion(Version.CURRENT_VERSION);
        WeCrossRPCService weCrossService = new WeCrossRPCService();
        try {
            weCrossService.send("POST", "/test/test/", request, TransactionResponse.class);
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }
    }
}
