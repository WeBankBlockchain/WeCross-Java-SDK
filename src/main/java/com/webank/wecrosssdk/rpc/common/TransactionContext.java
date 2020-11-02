package com.webank.wecrosssdk.rpc.common;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionContext implements AutoCloseable {
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

    public static boolean isPathInTransaction(String path) throws WeCrossSDKException {
        if (txThreadLocal.get() == null) {
            return false;
        } else {
            List<String> paths = pathInTransactionThreadLocal.get();
            if (paths == null) {
                throw new WeCrossSDKException(
                        ErrorCode.FIELD_MISSING,
                        "isPathInTransaction: TransactionID exist, but pathList doesn't.");
            }
            return paths.contains(path);
        }
    }

    @Override
    public void close() {
        txThreadLocal.remove();
    }
}
