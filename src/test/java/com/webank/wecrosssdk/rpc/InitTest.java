package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Test;

public class InitTest {
    @Test
    public void initTest() {
        try {
            WeCrossService weCrossService = new WeCrossRPCService();
            WeCrossRPCFactory.build(weCrossService);
        } catch (WeCrossSDKException e) {
            Assert.assertNotNull(e);
        }
    }
}
