package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.service.AuthenticationManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Before;
import org.junit.Test;

public class AsynCredentialTest {

    WeCrossRPC weCrossRPC;

    @Before
    public void init() throws WeCrossSDKException {
        MockWeCrossService mockWeCrossService = new MockWeCrossService();
        weCrossRPC = WeCrossRPCFactory.build(mockWeCrossService);
    }

    @Test
    public void credentialAsyncTest() {
        AuthenticationManager.setCurrentUser("hello", "Async world");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            fixedThreadPool.execute(
                    () -> {
                        try {
                            countDownLatch.await();
                            System.out.println(AuthenticationManager.getCurrentUser());
                            System.out.println(AuthenticationManager.getCurrentUserCredential());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        }
        countDownLatch.countDown();
        fixedThreadPool.shutdown();
    }
}
