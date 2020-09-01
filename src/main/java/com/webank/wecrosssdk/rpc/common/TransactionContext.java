package com.webank.wecrosssdk.rpc.common;

import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionContext implements AutoCloseable {
    public static ThreadLocal<String> txThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<AtomicInteger> seqThreadLocal = new ThreadLocal<>();

    public TransactionContext(String txID) {
        txThreadLocal.set(txID);
    }

    public static String currentTXID() {
        return txThreadLocal.get();
    }

    public static String currentSeq() throws WeCrossSDKException {
        if (seqThreadLocal.get() == null) {
            throw new WeCrossSDKException(
                    ErrorCode.FIELD_MISSING, "Transaction Seq is null, please check again.");
        }
        return String.valueOf(seqThreadLocal.get().getAndIncrement());
    }

    @Override
    public void close() {
        txThreadLocal.remove();
    }
}
