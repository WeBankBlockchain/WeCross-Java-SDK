package com.webank.wecrosssdk.performance.htlc;

import com.webank.wecrosssdk.common.StatusCode;
import com.webank.wecrosssdk.performance.PerformanceSuite;
import com.webank.wecrosssdk.performance.PerformanceSuiteCallback;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.common.Receipt;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTLCTransferSuite implements PerformanceSuite {
    private static Logger logger = LoggerFactory.getLogger(HTLCTransferSuite.class);
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private WeCrossRPC weCrossRPC;
    private String selfPath;
    private String senderAccount0;
    private String sender0;
    private String receiver0;
    private String counterpartyPath;
    private String senderAccount1;
    private String sender1;
    private String receiver1;

    public HTLCTransferSuite(
            WeCrossRPC weCrossRPC,
            String selfPath,
            String senderAccount0,
            String sender0,
            String receiver0,
            String counterpartyPath,
            String senderAccount1,
            String sender1,
            String receiver1) {
        this.weCrossRPC = weCrossRPC;
        this.selfPath = selfPath;
        this.senderAccount0 = senderAccount0;
        this.sender0 = sender0;
        this.receiver0 = receiver0;
        this.counterpartyPath = counterpartyPath;
        this.senderAccount1 = senderAccount1;
        this.sender1 = sender1;
        this.receiver1 = receiver1;
    }

    @Override
    public String getName() {
        return "HTLC Transfer Suite";
    }

    @Override
    public void call(PerformanceSuiteCallback callback, int index) {
        try {
            lock.writeLock().lock();
            long now = System.currentTimeMillis() / 1000;
            // use timelock as secret
            String secret = String.valueOf(now);
            Hash myHash = new Hash();
            String hash = myHash.sha256(secret);
            // generate timelock
            long t0 = now + 500;
            long t1 = now + 250;

            String[] args =
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
            newContract(counterpartyPath, senderAccount1, "null", args);
            logger.info("participant newContract: {}", Arrays.toString(args));

            args[1] = "true";
            // initiator
            newContract(selfPath, senderAccount0, secret, args);
            logger.info("initiator newContract: {}", Arrays.toString(args));

            if (checkStatus(selfPath, senderAccount0, hash, t0)) {
                callback.onSuccess("transfer successfully");
            } else {
                callback.onFailed("transfer unsuccessfully");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            callback.onFailed(e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /* create a htlc transfer contract
      secret: only initiator holds
      args: hash, role, sender0, receiver0, amount0, timelock0, sender1, receiver1, amount1, timelock1
    */
    private void newContract(String path, String account, String secret, String[] args)
            throws Exception {
        boolean done = false;
        int maxRound = 0;
        TransactionResponse response = new TransactionResponse();
        while (!done && maxRound++ < 5) {
            try {
                response = weCrossRPC.sendTransaction(path, "newContract", args).send();
                Receipt receipt = response.getReceipt();
                if (response.getErrorCode() != StatusCode.SUCCESS
                        || receipt.getErrorCode() != StatusCode.SUCCESS) {
                    if (receipt != null) {
                        throw new Exception(
                                "new transfer contract failed: " + receipt.getErrorMessage());
                    } else {
                        throw new Exception(
                                "new transfer contract failed: " + response.getMessage());
                    }
                }
                done = true;
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        if (!done) {
            throw new Exception("new transfer contract failed");
        }

        Receipt receipt = response.getReceipt();
        if (receipt.getResult()[0].trim().equalsIgnoreCase("success")) {
            String txHash = response.getReceipt().getHash();
            long blockNum = response.getReceipt().getBlockNumber();
            setNewContractTxInfo(path, account, args[0], txHash, blockNum);
            if (args[1].equalsIgnoreCase("true")) {
                setSecret(path, account, args[0], secret);
            }
        }
    }

    private void setNewContractTxInfo(
            String path, String account, String hash, String txHash, long blockNum)
            throws Exception {
        boolean done = false;
        int maxRound = 0;
        TransactionResponse response;
        while (!done && maxRound++ < 5) {
            try {
                response =
                        weCrossRPC
                                .sendTransaction(
                                        path,
                                        "setNewContractTxInfo",
                                        hash,
                                        txHash,
                                        String.valueOf(blockNum))
                                .send();

                Receipt receipt = response.getReceipt();
                if (response.getErrorCode() != StatusCode.SUCCESS
                        || receipt.getErrorCode() != StatusCode.SUCCESS) {
                    if (receipt != null) {
                        throw new Exception(
                                "failed to set transfer contract tx-info: "
                                        + receipt.getErrorMessage());
                    } else {
                        throw new Exception(
                                "failed to set transfer contract tx-info: "
                                        + response.getMessage());
                    }
                }
                done = true;
            } catch (Exception e) {
                logger.error("round: {}, error: {}", maxRound, e.getMessage());
            }
        }

        if (!done) {
            throw new Exception("set transfer contract tx-info failed");
        }
    }

    private void setSecret(String path, String account, String hash, String secret)
            throws Exception {
        boolean done = false;
        int maxRound = 0;
        TransactionResponse response;
        while (!done && maxRound++ < 5) {
            try {
                response =
                        weCrossRPC.sendTransaction(path, "setSecret", hash, secret).send();

                Receipt receipt = response.getReceipt();
                if (response.getErrorCode() != StatusCode.SUCCESS
                        || receipt.getErrorCode() != StatusCode.SUCCESS) {
                    if (receipt != null) {
                        throw new Exception("failed to set secret: " + receipt.getErrorMessage());
                    } else {
                        throw new Exception("failed to set secret: " + response.getMessage());
                    }
                }
                done = true;
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        if (!done) {
            throw new Exception("set secret failed");
        }
    }

    private boolean checkStatus(String path, String account, String hash, long timelock)
            throws Exception {
        while ((System.currentTimeMillis() / 1000) <= timelock) {
            if (!getStatus(path, account, hash)) {
                Thread.sleep(500);
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean getStatus(String path, String account, String hash) throws Exception {
        boolean done = false;
        int maxRound = 0;
        TransactionResponse response;
        Receipt receipt = new Receipt();
        while (!done && maxRound++ < 5) {
            try {
                response = weCrossRPC.call(path, "getSelfUnlockStatus", hash).send();

                receipt = response.getReceipt();
                if (response.getErrorCode() != StatusCode.SUCCESS
                        || receipt.getErrorCode() != StatusCode.SUCCESS) {
                    if (receipt != null) {
                        throw new Exception("failed to get balance: " + receipt.getErrorMessage());
                    } else {
                        throw new Exception("failed to get balance: " + response.getMessage());
                    }
                }
                done = true;
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        if (!done) {
            throw new Exception("get balance failed");
        }
        return receipt.getResult()[0].trim().equalsIgnoreCase("true");
    }
}
