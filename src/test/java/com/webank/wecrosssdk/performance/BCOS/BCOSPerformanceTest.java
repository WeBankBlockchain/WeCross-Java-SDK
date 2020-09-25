package com.webank.wecrosssdk.performance.BCOS;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceManager;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.resource.ResourceFactory;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import java.math.BigInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BCOSPerformanceTest {
    private static final Logger logger = LoggerFactory.getLogger(BCOSPerformanceTest.class);

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest [path] call [count] [qps] [poolSize]");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest [path] sendTransaction [count] [qps] [poolSize]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest payment.bcos.HelloWeCross call 100 10 2000");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest payment.bcos.HelloWeCross sendTransaction 100 10 500");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            usage();
        }

        String path = args[0];
        String command = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger qps = new BigInteger(args[3]);
        int poolSize = Integer.parseInt(args[4]);

        System.out.println(
                "BCOSPerformanceTest: "
                        + ", command: "
                        + command
                        + ", count: "
                        + count
                        + ", qps: "
                        + qps
                        + ", path: "
                        + path);

        switch (command) {
            case "call":
                callTest(path, count, qps, poolSize);
                exit();
            case "sendTransaction":
                sendTransactionTest(path, count, qps, poolSize);
                exit();
            case "status":
                statusTest(path, count, qps, poolSize);
                exit();
            default:
                usage();
        }
    }

    public static void callTest(String path, BigInteger count, BigInteger qps, int poolSize) {
        Resource resource = loadResource(path);
        if (resource != null) {
            try {
                PerformanceSuite suite = new BCOSCallSuite(resource);
                PerformanceManager performanceManager =
                        new PerformanceManager(suite, count, qps, poolSize);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void sendTransactionTest(
            String path, BigInteger count, BigInteger qps, int poolSize) {
        Resource resource = loadResource(path);
        if (resource != null) {
            try {
                PerformanceSuite suite = new BCOSSendTransactionSuite(resource);
                PerformanceManager performanceManager =
                        new PerformanceManager(suite, count, qps, poolSize);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void statusTest(String path, BigInteger count, BigInteger qps, int poolSize) {
        Resource resource = loadResource(path);
        if (resource != null) {
            try {
                PerformanceSuite suite = new StatusSuite(resource);
                PerformanceManager performanceManager =
                        new PerformanceManager(suite, count, qps, poolSize);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static Resource loadResource(String path) {
        WeCrossRPCService weCrossRPCService = new WeCrossRPCService();
        try {
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossRPCService);
            Resource resource = ResourceFactory.build(weCrossRPC, path);
            return resource;
        } catch (WeCrossSDKException e) {
            System.out.println("Error: Init wecross service failed: {}" + e);
            return null;
        }
    }

    private static void exit() {
        System.exit(0);
    }
}
