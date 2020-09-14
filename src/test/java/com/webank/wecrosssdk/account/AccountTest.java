package com.webank.wecrosssdk.account;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.methods.response.UAResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountTest {
    /**
     * 多线程测试用例
     *
     * @param threadCount 开启线程数
     * @param run 线程执行方法
     * @param finish 线程全部执行结束后运行方法
     */
    public void begin(int threadCount, Run run, Finish finish) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            fixedThreadPool.execute(
                    () -> {
                        try {
                            countDownLatch.await();
                            run.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
        countDownLatch.countDown();
        fixedThreadPool.shutdown();
        while (!fixedThreadPool.isTerminated()) {}
        finish.finish();
    }
}

interface Run {
    void run() throws Exception;
}

interface Finish {
    void finish();
}

class Test {
    static int m = 0;

    public static void main(String[] args) throws WeCrossSDKException {
        WeCrossService weCrossService = new WeCrossRPCService();
        WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossService);
        new AccountTest()
                .begin(
                        2,
                        () -> {
                            UAResponse uaResponse = weCrossRPC.login("Kyon", "123456").send();
                        },
                        () -> System.out.println(m));
    }
}
