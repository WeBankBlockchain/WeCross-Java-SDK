package com.webank.wecrosssdk.rpc.common;

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

    public static String currentSeq() {
        return String.valueOf(seqThreadLocal.get().getAndIncrement());
    }

    @Override
    public void close() {
        txThreadLocal.remove();
    }
}
