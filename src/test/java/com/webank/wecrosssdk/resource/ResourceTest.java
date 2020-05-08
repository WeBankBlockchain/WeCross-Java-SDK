package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.methods.Request;
import com.webank.wecrosssdk.rpc.methods.request.TransactionRequest;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Test;

public class ResourceTest {

    @Test
    public void statusTest() {
        try {
            WeCrossService service = new MockWeCrossService();
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
            String result = resource.status();
            Assert.assertEquals(result, "test");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void infoTest() {
        try {
            WeCrossService service = new MockWeCrossService();
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
            ResourceDetail result = resource.detail();
            Assert.assertNotEquals(result, null);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void callTest() {
        try {
            WeCrossService service = new MockWeCrossService();
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
            String[] result = resource.call("test");
            Assert.assertNull(result);

            Request<TransactionRequest> request = new Request<>();
            request.setMethod("call");
            TransactionResponse response = resource.call(request);
            Assert.assertEquals(response.getErrorCode(), 0);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void sendTransactionTest() {
        try {
            WeCrossService service = new MockWeCrossService();
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(service);
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test", "test");
            String[] result = resource.sendTransaction("test", "test");
            Assert.assertNull(result);

            Request<TransactionRequest> request = new Request<>();
            request.setMethod("sendTransaction");
            TransactionResponse response = resource.sendTransaction(request);
            Assert.assertEquals(response.getErrorCode(), 0);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
