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
    private static Logger logger = LoggerFactory.getLogger(BCOSPerformanceTest.class);

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest call count qps");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest sendTransaction count qps");
        System.out.println("===================================================================");
        System.out.println(
                "Performance test resource info: \n"
                        + "IPath: \tpayment.bcos.HelloWorld\n"
                        + "Config dir: \tstubs/bcos/stub.toml\n"
                        + "[[resources]]\n"
                        + "    # name cannot be repeated\n"
                        + "    name = 'HelloWeCross'\n"
                        + "    type = 'BCOS_CONTRACT'\n"
                        + "    contractAddress = '0x_the_address_you_deploy_HelloWeCross.sol_'");
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
                "BCOSPerformanceTest: command is "
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
        Resource resource = loadResource("payment.bcos.HelloWeCross", "bcos");
        if (resource != null) {
            try {
                PerformanceSuite suite = new BCOSCallSuite(resource);
                PerformanceManager performanceManager = new PerformanceManager(suite, count, qps);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void sendTransactionTest(BigInteger count, BigInteger qps) {
        Resource resource = loadResource("payment.bcos.HelloWeCross", "bcos");
        if (resource != null) {
            try {
                PerformanceSuite suite = new BCOSSendTransactionSuite(resource);
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
