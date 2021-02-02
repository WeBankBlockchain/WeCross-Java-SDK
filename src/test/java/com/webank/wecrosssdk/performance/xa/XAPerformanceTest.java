package com.webank.wecrosssdk.performance.xa;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceManager;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.WeCrossPerfRPCFactory;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import java.math.BigInteger;

public class XAPerformanceTest {

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.xa.XAPerformanceTest [type] [path] [count] [qps] [poolSize]");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.xa.XAPerformanceTest bcos payment.bcos.Evidence 1000 100 1000");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.xa.XAPerformanceTest fabric payment.fabric.Evidence 10 1 1");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            usage();
        }

        String type = args[0];
        String path = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger qps = new BigInteger(args[3]);
        int poolSize = Integer.parseInt(args[4]);

        try {
            WeCrossRPC weCrossRPC = WeCrossPerfRPCFactory.build();

            PerformanceSuite suite = null;
            if ("bcos".equals(type)) {
                suite = new BCOSXASuite(weCrossRPC, path);
            } else if ("fabric".equals(type)) {
                suite = new FabricXASuite(weCrossRPC, path);
            } else {
                exit();
            }
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
