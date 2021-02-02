package com.webank.wecrosssdk.performance.htlc;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.PerformanceManager;
import com.webank.wecrosssdk.performance.WeCrossPerfRPCFactory;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import java.math.BigInteger;

public class HTLCPerformanceTest {

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.htlc.HTLCPerformanceTest [selfPath] [senderAccount0] [sender0] [receiver0] [counterpartyPath] [senderAccount1] [sender1] [receiver1] [count] [qps]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.htlc.HTLCPerformanceTest payment.bcos.htlc bcos 0x55f934bcbe1e9aef8337f5551142a442fdde781c 0x2b5ad5c4795c026514f8317c7a215e218dccd6cf payment.fabric.htlc fabric Admin@org1.example.com User1@org1.example.com 10 1");
        System.out.println(
                "======================================================================================================================================");
        System.out.println(
                "Notification: 1. the router connected by sdk should config both senders account\n"
                        + "              2. all htlc contracts should have different asset contracts\n"
                        + "              3. bcos with bcos, bcos with fabric, fabric with fabric use the same command\n");
        exit();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 10 && args.length != 11) {
            usage();
        }

        String selfPath = args[0];
        String senderAccount0 = args[1];
        String sender0 = args[2];
        String receiver0 = args[3];
        String counterpartyPath = args[4];
        String senderAccount1 = args[5];
        String sender1 = args[6];
        String receiver1 = args[7];
        BigInteger count = new BigInteger(args[8]);
        BigInteger qps = new BigInteger(args[9]);

        System.out.println(
                "FabricPerformanceTest: selfPath is "
                        + selfPath
                        + ", senderAccount0 is "
                        + senderAccount0
                        + ", sender0 is "
                        + sender0
                        + ", receiver0 is "
                        + receiver0
                        + ", counterpartyPath is "
                        + counterpartyPath
                        + ", senderAccount1 is "
                        + senderAccount1
                        + ", sender1 is "
                        + sender1
                        + ", receiver1 is "
                        + receiver1
                        + ", count is "
                        + count
                        + ", qps is "
                        + qps);
        try {
            WeCrossRPC weCrossRPC = loadWeCrossRPC();
            HTLCTransferSuite suite =
                    new HTLCTransferSuite(
                            weCrossRPC,
                            selfPath,
                            senderAccount0,
                            sender0,
                            receiver0,
                            counterpartyPath,
                            senderAccount1,
                            sender1,
                            receiver1);
            PerformanceManager performanceManager = new PerformanceManager(suite, count, qps, 2);
            performanceManager.run();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        exit();
    }

    private static WeCrossRPC loadWeCrossRPC() throws WeCrossSDKException {
        try {
            return WeCrossPerfRPCFactory.build();
        } catch (Exception e) {
            throw new WeCrossSDKException(
                    ErrorCode.RPC_ERROR, "Error: Init wecross service failed: " + e.getMessage());
        }
    }

    private static void exit() {
        System.exit(0);
    }
}
