package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.common.ResourceDetail;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResourceTest {

    private WeCrossRPC weCrossRPC;

    @Before
    public void init() {
        WeCrossService service = new MockWeCrossService();
        try {
            weCrossRPC = WeCrossRPCFactory.build(service);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void infoTest() {
        try {
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test");
            ResourceDetail result = resource.detail();
            Assert.assertNotEquals(result, null);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void callTest() {
        try {
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test");
            String[] result = resource.call("test");
            Assert.assertNull(result);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void sendTransactionTest() {
        try {
            Resource resource = ResourceFactory.build(weCrossRPC, "test.test.test");
            String[] result = resource.sendTransaction("test", "test");
            Assert.assertNull(result);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
