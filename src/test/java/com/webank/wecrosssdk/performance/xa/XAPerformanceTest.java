package com.webank.wecrosssdk.performance.xa;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceManager;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XAPerformanceTest {
    private static Logger logger = LoggerFactory.getLogger(XAPerformanceTest.class);

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.xa.XAPerformanceTest [path] [account] [count] [qps] [poolSize]");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.xa.XAPerformanceTest payment.bcos.xa bcos_user1 1000 100 1000");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            usage();
        }

        String path = args[0];
        String account = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger qps = new BigInteger(args[3]);
        int poolSize = Integer.parseInt(args[4]);

        try {
            WeCrossRPCService weCrossRPCService = new WeCrossRPCService();
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossRPCService);

            PerformanceSuite suite = new XASuite(weCrossRPC, account, path);
            PerformanceManager performanceManager =
                    new PerformanceManager(suite, count, qps, poolSize);
            performanceManager.run();
        } catch (WeCrossSDKException e) {
            System.out.println(e.getMessage());
        }
        exit();
    }

    private static void exit() {
        System.exit(0);
    }
}
