package com.webank.wecrosssdk.performance.htlc;

import com.webank.wecrosssdk.common.StatusCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.performance.WeCrossPerfRPCFactory;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.util.concurrent.Semaphore;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class HTLCTest {
    private static final Logger logger = LoggerFactory.getLogger(HTLCTest.class);

    private static WeCrossRPC weCrossRPC;
    private static ThreadPoolTaskExecutor threadPool;

    public static void usage() {
        System.out.println("Usage:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.htlc.HTLCTest [selfPath] [senderAccount0] [sender0] [receiver0] [counterpartyPath] [senderAccount1] [sender1] [receiver1] [count]");
        System.out.println("Example:");
        System.out.println(
                " \t java -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.htlc.HTLCTest payment.bcos.htlc bcos 0x55f934bcbe1e9aef8337f5551142a442fdde781c 0x2b5ad5c4795c026514f8317c7a215e218dccd6cf payment.bcos1.htlc bcos 0x55f934bcbe1e9aef8337f5551142a442fdde781c 0x2b5ad5c4795c026514f8317c7a215e218dccd6cd 100");
        System.out.println(
                "======================================================================================================================================");
        System.out.println(
                "Notification: 1. the router connected by sdk should config both senders account\n"
                        + "              2. all htlc contracts should have different asset contracts\n"
                        + "              3. bcos with bcos, bcos with fabric, fabric with fabric use the same command\n");
        exit();
    }

    public static void main(String[] args) {
        try {
            if (args.length != 9) {
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
            int count = Integer.parseInt(args[8]);
            if (count > 1024) {
                System.out.println("count should be less than 300");
                exit();
            }

            threadPool = new ThreadPoolTaskExecutor();
            threadPool.setCorePoolSize(count);
            threadPool.setMaxPoolSize(count * 2);
            threadPool.setQueueCapacity(count);
            threadPool.initialize();

            weCrossRPC = loadWeCrossRPC();
            long now = System.currentTimeMillis() / 1000;
            Semaphore semaphore = new Semaphore(count, true);
            try {
                semaphore.acquire(count);
            } catch (InterruptedException e) {
                logger.warn("Interrupted,", e);
                Thread.currentThread().interrupt();
            }
            int before = getBalance(counterpartyPath, senderAccount1, receiver1);

            for (int i = 0; i < count; ++i) {
                int finalI = i;
                threadPool.execute(
                        () -> {
                            try {
                                // use timelock as secret
                                String secret = String.valueOf(now) + finalI;
                                String hash = DigestUtils.sha256Hex(secret);
                                // generate timelock
                                long t0 = now + 5000000;
                                long t1 = now + 2500000;

                                String[] proposalArgs =
                                        new String[] {
                                            hash,
                                            "false",
                                            sender0,
                                            receiver0,
                                            "1",
                                            String.valueOf(t0),
                                            sender1,
                                            receiver1,
                                            "1",
                                            String.valueOf(t1)
                                        };

                                // participant
                                newContract(counterpartyPath, senderAccount1, "null", proposalArgs);

                                proposalArgs[1] = "true";
                                // initiator
                                newContract(selfPath, senderAccount0, secret, proposalArgs);
                                semaphore.release();
                                System.out.println("create proposal " + finalI + " done");
                            } catch (Exception e) {
                                semaphore.release();
                                System.out.println("Error: " + e.getMessage());
                            }
                        });
            }
            try {
                // wait until job is finished
                semaphore.acquire(count);
                System.out.println("waiting for result...");
                long startTime = System.currentTimeMillis();
                while (before + count != getBalance(counterpartyPath, senderAccount1, receiver1)) {
                    Thread.sleep(500);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("TPS: " + count * 1000.0 / (endTime - startTime));
                System.out.println("Time: " + (endTime - startTime) / 1000.0 + "s");
                exit();
            } catch (InterruptedException e) {
                logger.warn("Interrupted,", e);
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            exit();
        }
    }

    /* create a htlc transfer contract
      secret: only initiator holds
      args: hash, role, sender0, receiver0, amount0, timelock0, sender1, receiver1, amount1, timelock1
    */
    private static void newContract(String path, String account, String secret, String[] args)
            throws Exception {
        TransactionResponse response = weCrossRPC.sendTransaction(path, "newProposal", args).send();
        Receipt receipt = response.getReceipt();
        if (response.getErrorCode() != StatusCode.SUCCESS
                || receipt.getErrorCode() != StatusCode.SUCCESS) {
            if (receipt != null) {
                throw new Exception("new transfer contract failed: " + receipt.getMessage());
            } else {
                throw new Exception("new transfer contract failed: " + response.getMessage());
            }
        }

        if (receipt.getResult()[0].trim().equalsIgnoreCase("success")) {
            String txHash = response.getReceipt().getHash();
            long blockNum = response.getReceipt().getBlockNumber();
            setNewContractTxInfo(path, account, args[0], txHash, blockNum);
            if (args[1].equalsIgnoreCase("true")) {
                setSecret(path, account, args[0], secret);
            }
        }
    }

    private static void setNewContractTxInfo(
            String path, String account, String hash, String txHash, long blockNum)
            throws Exception {
        TransactionResponse response =
                weCrossRPC
                        .sendTransaction(
                                path,
                                "setNewProposalTxInfo",
                                hash,
                                txHash,
                                String.valueOf(blockNum))
                        .send();

        Receipt receipt = response.getReceipt();
        if (response.getErrorCode() != StatusCode.SUCCESS
                || receipt.getErrorCode() != StatusCode.SUCCESS) {
            if (receipt != null) {
                throw new Exception(
                        "failed to set transfer contract tx-info: " + receipt.getMessage());
            } else {
                throw new Exception(
                        "failed to set transfer contract tx-info: " + response.getMessage());
            }
        }
    }

    private static void setSecret(String path, String account, String hash, String secret)
            throws Exception {
        TransactionResponse response =
                weCrossRPC.sendTransaction(path, "setSecret", hash, secret).send();
        Receipt receipt = response.getReceipt();
        if (response.getErrorCode() != StatusCode.SUCCESS
                || receipt.getErrorCode() != StatusCode.SUCCESS) {
            if (receipt != null) {
                throw new Exception("failed to set secret: " + receipt.getMessage());
            } else {
                throw new Exception("failed to set secret: " + response.getMessage());
            }
        }
    }

    private static WeCrossRPC loadWeCrossRPC() throws Exception {
        try {
            return WeCrossPerfRPCFactory.build();
        } catch (WeCrossSDKException e) {
            throw new Exception("Error: Init wecross service failed: {}" + e.getMessage());
        }
    }

    private static int getBalance(String path, String account, String address) {
        try {
            TransactionResponse response = weCrossRPC.call(path, "balanceOf", address).send();
            if (response.getErrorCode() != 0 || response.getReceipt().getErrorCode() != 0) {
                return -1;
            } else {
                return Integer.parseInt(response.getReceipt().getResult()[0].trim());
            }
        } catch (Exception exception) {
            return -1;
        }
    }

    private static void exit() {
        System.exit(0);
    }
}
