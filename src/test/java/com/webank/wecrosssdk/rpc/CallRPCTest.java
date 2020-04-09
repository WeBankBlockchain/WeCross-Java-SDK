package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.AccountResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceDetailResponse;
import com.webank.wecrosssdk.rpc.methods.response.ResourceResponse;
import com.webank.wecrosssdk.rpc.methods.response.StubResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Test;

public class CallRPCTest {

    @Test
    public void supportedStubsTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        StubResponse stubResponse = weCrossRPC.supportedStubs().send();
        Assert.assertEquals(stubResponse.getErrorCode(), 0);
        Assert.assertNotNull(stubResponse.getStubs());
    }

    @Test
    public void listAccountsTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        AccountResponse accountResponse = weCrossRPC.listAccounts().send();
        Assert.assertEquals(accountResponse.getErrorCode(), 0);
        Assert.assertNotNull(accountResponse.getAccounts());
    }

    @Test
    public void listResourcesTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        ResourceResponse resourceResponse = weCrossRPC.listResources(true).send();
        Assert.assertEquals(resourceResponse.getErrorCode(), 0);
        Assert.assertNotNull(resourceResponse.getResources());
    }

    @Test
    public void statusTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        Response response = weCrossRPC.status("test.test.test").send();
        Assert.assertEquals(response.getErrorCode(), 0);
    }

    @Test
    public void detailTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        ResourceDetailResponse resourceDetailResponse = weCrossRPC.detail("test.test.test").send();
        Assert.assertEquals(resourceDetailResponse.getErrorCode(), 0);
    }

    @Test
    public void callTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        TransactionResponse transactionResponse =
                weCrossRPC.call("test.test.test", "test", "test", "test").send();
        Assert.assertEquals(transactionResponse.getErrorCode(), 0);
    }

    @Test
    public void sendTransactionTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        TransactionResponse transactionResponse =
                weCrossRPC.sendTransaction("test.test.test", "test", "test", "test").send();
        Assert.assertEquals(transactionResponse.getErrorCode(), 0);
    }
}
