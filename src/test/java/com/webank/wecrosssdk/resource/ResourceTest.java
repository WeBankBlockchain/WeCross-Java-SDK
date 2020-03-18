package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.common.ResourceInfo;
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
            ResourceInfo result = resource.info();
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
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
