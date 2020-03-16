package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.ResourceInfoResponse;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Test;

public class ResourceTest {

    @Test
    public void statusTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
        Response response = resource.status();
        Assert.assertEquals(response.getResult(), 0);
    }

    @Test
    public void infoTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
        ResourceInfoResponse resourceInfoResponse = resource.info();
        Assert.assertEquals(resourceInfoResponse.getResult(), 0);
    }

    @Test
    public void callTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
        TransactionResponse response = resource.call("get");
        Assert.assertEquals(response.getResult(), 0);
    }

    @Test
    public void sendTransactionTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
        Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
        TransactionResponse response = resource.sendTransaction("set", "test");
        Assert.assertEquals(response.getResult(), 0);
    }
}
