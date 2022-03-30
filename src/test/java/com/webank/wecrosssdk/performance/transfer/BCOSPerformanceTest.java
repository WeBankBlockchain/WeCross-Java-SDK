package com.webank.wecrosssdk.performance.transfer;

import com.webank.wecrosssdk.performance.PerformanceManager;
import com.webank.wecrosssdk.performance.WeCrossPerfRPCFactory;
import com.webank.wecrosssdk.resource.Resource;
import com.webank.wecrosssdk.resource.ResourceFactory;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import java.math.BigInteger;

public class BCOSPerformanceTest {

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest sendTransaction [count] [qps] [poolSize] [transfer] [userFile]");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest userAdd [count] [qps] [poolSize] [transfer] [userFile]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest sendTransaction 100 ./user 10 500 payment.bcos.transfer");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest userAdd 100 ./user");
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
        if (args.length < 5) {
            usage();
        }

        String command = args[0];
        BigInteger count = new BigInteger(args[1]);
        BigInteger qps = new BigInteger(args[2]);
        int poolSize = Integer.parseInt(args[3]);
        String transferResource = args[4];
        String file = args[5];

        System.out.println(
                "BCOSPerformanceTest: command is "
                        + command
                        + ", transferResource is "
                        + transferResource
                        + ", count is "
                        + count
                        + ", qps is "
                        + qps
                        + ", file is "
                        + file);

        switch (command) {
            case "userAdd":
                createUser(count, qps, poolSize, file, transferResource);
                exit();
                break;
            case "sendTransaction":
                sendTransactionTest(count, qps, poolSize, file, transferResource);
                exit();
                break;
            default:
                usage();
        }
    }

    public static void createUser(
            BigInteger count, BigInteger qps, int poolSize, String file, String resourceName) {
        Resource resource = loadResource(resourceName);
        try {
            DagUserMgr dagUserMgr = new DagUserMgr(file);
            dagUserMgr.createUser(count.intValue());
            BCOSUserAddSuite bcosUserAddSuite = new BCOSUserAddSuite(resource);
            bcosUserAddSuite.setDagUserMgr(dagUserMgr);
            PerformanceManager performanceManager =
                    new PerformanceManager(bcosUserAddSuite, count, qps, poolSize);
            performanceManager.run();
            dagUserMgr.writeDagTransferUser();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void sendTransactionTest(
            BigInteger count, BigInteger qps, int poolSize, String file, String resourceName) {
        Resource resource = loadResource(resourceName);
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

    private static Resource loadResource(String path) {
        try {
            WeCrossRPC weCrossRPC = WeCrossPerfRPCFactory.build();
            return ResourceFactory.build(weCrossRPC, path);
        } catch (Exception e) {
            System.out.println("Error: Init wecross service failed: {}" + e);
            return null;
        }
    }

    private static void exit() {
        System.exit(0);
    }
}
