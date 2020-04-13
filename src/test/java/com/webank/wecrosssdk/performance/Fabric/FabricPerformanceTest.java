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
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest call count qps");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest sendTransaction count qps");
        System.out.println("===================================================================");
        System.out.println(
                "Performance test resource info: \n"
                        + "IPath: \tpayment.fabric.HelloWorld\n"
                        + "Config dir: \tstubs/fabric/stub.toml\n"
                        + "[[resources]]\n"
                        + "    name = 'HelloWorld'\n"
                        + "    type = 'FABRIC_CONTRACT'\n"
                        + "    chainCodeName = 'mycc'\n"
                        + "    chainLanguage = \"go\"\n"
                        + "    peers=['org1', 'org2']");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            usage();
        }

        String command = args[0];
        BigInteger count = new BigInteger(args[1]);
        BigInteger qps = new BigInteger(args[2]);

        System.out.println(
                "FabricPerformanceTest: command is "
                        + command
                        + ", count is "
                        + count
                        + ", qps is "
                        + qps);

        switch (command) {
            case "call":
                callTest(count, qps);
                exit();
            case "sendTransaction":
                sendTransactionTest(count, qps);
                exit();
            default:
                usage();
        }
    }

    public static void callTest(BigInteger count, BigInteger qps) {
        Resource resource = loadResource("payment.fabric.HelloWorld", "fabric_user1");
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

    public static void sendTransactionTest(BigInteger count, BigInteger qps) {
        Resource resource = loadResource("payment.fabric.HelloWorld", "fabric_user1");
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
