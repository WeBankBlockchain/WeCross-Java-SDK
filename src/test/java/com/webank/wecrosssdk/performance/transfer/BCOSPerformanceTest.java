package com.webank.wecrosssdk.performance.transfer;

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
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest [accountName] sendTransaction [count] [qps] [poolSize] [userFile]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest bcos_user1 sendTransaction 100 10 500 ./user");
        System.out.println("===================================================================");
        System.out.println(
                "Performance test resource info: \n"
                        + "IPath: \tpayment.bcos.HelloWeCross\n"
                        + "Config dir: \tchains/bcos/stub.toml\n"
                        + "[[resources]]\n"
                        + "    # name cannot be repeated\n"
                        + "    name = 'HelloWeCross'\n"
                        + "    type = 'BCOS_CONTRACT'\n"
                        + "    contractAddress = '0x_the_address_you_deploy_HelloWeCross.sol_'");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 6) {
            usage();
        }

        String accountName = args[0];
        String command = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger qps = new BigInteger(args[3]);
        int poolSize = Integer.parseInt(args[4]);
        String file = args[5];

        System.out.println(
                "BCOSPerformanceTest: command is "
                        + command
                        + ", count is "
                        + count
                        + ", qps is "
                        + qps
                        + ", file is "
                        + file);

        switch (command) {
            case "sendTransaction":
                sendTransactionTest(accountName, count, qps, poolSize, file);
                exit();
            case "status":
                statusTest(accountName, count, qps, poolSize);
                exit();
            default:
                usage();
        }
    }

    public static void sendTransactionTest(
            String accountName, BigInteger count, BigInteger qps, int poolSize, String file) {
        Resource resource = loadResource("payment.bcos.transfer", accountName);
        if (resource != null) {
            try {
                DagUserMgr dagUserMgr = new DagUserMgr(file);
                dagUserMgr.loadDagTransferUser();
                BCOSSendTransactionSuite bcosSendTransactionSuite =
                        new BCOSSendTransactionSuite(resource);
                bcosSendTransactionSuite.setDagUserMgr(dagUserMgr);
                PerformanceManager performanceManager =
                        new PerformanceManager(bcosSendTransactionSuite, count, qps, poolSize);
                performanceManager.run();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void statusTest(
            String accountName, BigInteger count, BigInteger qps, int poolSize) {
        Resource resource = loadResource("payment.bcos.transfer", accountName);
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
