package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Test;

public class ResourceTest {
    @Test
    public void buildTest() throws Exception {

        WeCrossRPC weCrossRPC = WeCrossRPC.init(new MockWeCrossService());

        String iPath = "test.test.test";
        ResourceFactory.load(weCrossRPC, iPath);
        Assert.assertTrue(true); // Assume no exception, goes here
    }

    @Test
    public void setGetDataTest() throws Exception {
        WeCrossRPC weCrossRPC = WeCrossRPC.init(new MockWeCrossService());

        String iPath = "test.test.test";
        Resource resource = ResourceFactory.load(weCrossRPC, iPath);
        resource.setData("name", "Tom");

        GetDataResponse response = resource.getData("name");
        String value = response.getData().getValue();
        Assert.assertEquals("Tom", value);

        try {
            resource.getData("aa"); // Throw exception
            Assert.assertTrue(false); // Assume never goes here
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void callTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPC.init(service);
        String iPath = "test.test.test";
        Resource resource = ResourceFactory.load(weCrossRPC, iPath);

        resource.call(new String[] {""}, "get");
        Assert.assertTrue(true);
    }

    @Test
    public void sendTransactionTest() throws Exception {
        WeCrossService service = new MockWeCrossService();
        WeCrossRPC weCrossRPC = WeCrossRPC.init(service);
        String iPath = "test.test.test";
        Resource resource = ResourceFactory.load(weCrossRPC, iPath);

        resource.sendTransaction(new String[] {""}, "get");
        Assert.assertTrue(true);
    }
}
