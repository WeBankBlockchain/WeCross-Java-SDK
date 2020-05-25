package com.webank.wecrosssdk.performance.Fabric;

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

public class FabricPerformanceTest {
    private static Logger logger = LoggerFactory.getLogger(FabricPerformanceTest.class);

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest [accountName] call [count] [qps] [poolSize]");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest [accountName] sendTransaction [count] [qps] [poolSize]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest fabric_user1 call 100 10 2000");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest fabric_user1 sendTransaction 100 10 500");
        System.out.println("===================================================================");
        System.out.println(
                "Performance test resource info: \n"
                        + "IPath: \tpayment.fabric.sacc\n"
                        + "Config dir: \tchains/fabric/stub.toml\n"
                        + "[[resources]]\n"
                        + "    name = 'sacc'\n"
                        + "    type = 'FABRIC_CONTRACT'\n"
                        + "    chainCodeName = 'sacc'\n"
                        + "    chainLanguage = \"go\"\n"
                        + "    peers=['org1']");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            usage();
        }

        String accountName = args[0];
        String command = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger qps = new BigInteger(args[3]);
        int poolSize = Integer.parseInt(args[4]);

        System.out.println(
                "FabricPerformanceTest: command is "
                        + command
                        + ", count is "
                        + count
                        + ", qps is "
                        + qps);

        switch (command) {
            case "call":
                callTest(accountName, count, qps, poolSize);
                exit();
            case "sendTransaction":
                sendTransactionTest(accountName, count, qps, poolSize);
                exit();
            default:
                usage();
        }
    }

    public static void callTest(
            String accountName, BigInteger count, BigInteger qps, int poolSize) {
        Resource resource = loadResource("payment.fabric.sacc", accountName);
        if (resource == null) {
            logger.warn("Default to payment.fabric.sacc");
            resource = loadResource("payment.fabric.sacc", accountName);
        }

        if (resource != null) {
            try {
                PerformanceSuite suite = new FabricCallSuite(resource);
                PerformanceManager performanceManager =
                        new PerformanceManager(suite, count, qps, poolSize);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void sendTransactionTest(
            String accountName, BigInteger count, BigInteger qps, int poolSize) {
        Resource resource = loadResource("payment.fabric.sacc", accountName);
        if (resource == null) {
            logger.warn("Default to payment.fabric.sacc");
            resource = loadResource("payment.fabric.sacc", accountName);
        }

        if (resource != null) {
            try {
                PerformanceSuite suite = new FabricSendTransactionSuite(resource);
                PerformanceManager performanceManager =
                        new PerformanceManager(suite, count, qps, poolSize);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static Resource loadResource(String path, String accountName) {
        WeCrossRPCService weCrossRPCService = new WeCrossRPCService();
        try {
            WeCrossRPC weCrossRPC = WeCrossRPCFactory.build(weCrossRPCService);
            Resource resource = ResourceFactory.build(weCrossRPC, path, accountName);
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
