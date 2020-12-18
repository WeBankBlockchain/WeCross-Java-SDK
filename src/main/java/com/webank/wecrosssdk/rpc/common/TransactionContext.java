package com.webank.wecrosssdk.rpc.common;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionContext implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(TransactionContext.class);
    public static final ThreadLocal<String> txThreadLocal = new ThreadLocal<>();
    public static final ThreadLocal<AtomicInteger> seqThreadLocal = new ThreadLocal<>();
    public static final ThreadLocal<List<String>> pathInTransactionThreadLocal =
            new ThreadLocal<>();

    public TransactionContext(String txID) {
        txThreadLocal.set(txID);
    }

    public static String currentXATransactionID() {
        return txThreadLocal.get();
    }

    public static long currentXATransactionSeq() {
        return System.currentTimeMillis();
    }

    public static boolean isPathInTransaction(String path) {
        if (txThreadLocal.get() == null) return false;
        else {
            List<String> paths = pathInTransactionThreadLocal.get();
            if (paths == null) {
                logger.error("isPathInTransaction: TransactionID exist, but pathList doesn't.");
                return false;
            }
            return paths.contains(path);
        }
    }

    @Override
    public void close() {
        txThreadLocal.remove();
    }
}
