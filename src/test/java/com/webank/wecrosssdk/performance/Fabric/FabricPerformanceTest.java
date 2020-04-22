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

public class FabricPerformanceTest {

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest [accountName] call [count] [qps]");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest [accountName] sendTransaction [count] [qps]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest fabric_user1 call 100 10");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest fabric_user1 sendTransaction 100 10");
        System.out.println("===================================================================");
        System.out.println(
                "Performance test resource info: \n"
                        + "IPath: \tpayment.fabric.HelloWorld\n"
                        + "Config dir: \tstubs/fabric/stub.toml\n"
                        + "[[resources]]\n"
                        + "    name = 'abac'\n"
                        + "    type = 'FABRIC_CONTRACT'\n"
                        + "    chainCodeName = 'mycc'\n"
                        + "    chainLanguage = \"go\"\n"
                        + "    peers=['org1', 'org2']");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            usage();
        }

        String accountName = args[0];
        String command = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger qps = new BigInteger(args[3]);

        System.out.println(
                "FabricPerformanceTest: command is "
                        + command
                        + ", count is "
                        + count
                        + ", qps is "
                        + qps);

        switch (command) {
            case "call":
                callTest(accountName, count, qps);
                exit();
            case "sendTransaction":
                sendTransactionTest(accountName, count, qps);
                exit();
            default:
                usage();
        }
    }

    public static void callTest(String accountName, BigInteger count, BigInteger qps) {
        Resource resource = loadResource("payment.fabric.abac", accountName);
        if (resource == null) {
            logger.warn("Default to payment.fabric.HelloWeCross");
            resource = loadResource("payment.fabric.HelloWeCross", accountName);
        }

        if (resource != null) {
            try {
                PerformanceSuite suite = new FabricCallSuite(resource);
                PerformanceManager performanceManager = new PerformanceManager(suite, count, qps);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void sendTransactionTest(String accountName, BigInteger count, BigInteger qps) {
        Resource resource = loadResource("payment.fabric.abac", accountName);
        if (resource == null) {
            logger.warn("Default to payment.fabric.HelloWeCross");
            resource = loadResource("payment.fabric.HelloWeCross", accountName);
        }

        if (resource != null) {
            try {
                PerformanceSuite suite = new FabricSendTransactionSuite(resource);
                PerformanceManager performanceManager = new PerformanceManager(suite, count, qps);
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
