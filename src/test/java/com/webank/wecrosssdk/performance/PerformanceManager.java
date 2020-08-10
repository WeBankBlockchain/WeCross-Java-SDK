package com.webank.wecrosssdk.performance;

import com.google.common.util.concurrent.RateLimiter;
import java.math.BigInteger;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class PerformanceManager {
    private Logger logger = LoggerFactory.getLogger(PerformanceManager.class);
    private PerformanceSuite suite;
    private BigInteger count;
    private BigInteger qps;
    private ThreadPoolTaskExecutor threadPool;
    private RateLimiter limiter;
    private Integer area;

    private static int poolSize = 16;
    private int maxConcurrentNumber = 800;
    private transient Semaphore concurrentLimiter = new Semaphore(maxConcurrentNumber, true);

    public PerformanceManager(PerformanceSuite suite, String count, String qps) {
        this(suite, new BigInteger(count), new BigInteger(qps), 200);
    }

    public PerformanceManager(PerformanceSuite suite, long count, long qps) {
        this(
                suite,
                new BigInteger(Long.toString(count, 10)),
                new BigInteger(Long.toString(qps, 10)),
                200);
    }

    public PerformanceManager(
            PerformanceSuite suite, BigInteger count, BigInteger qps, int maxConcurrentNumber) {
        if (count.compareTo(new BigInteger(String.valueOf(10))) < 0) {
            System.out.println("Require: count >= 10");
            System.exit(1);
        }

        this.maxConcurrentNumber = maxConcurrentNumber;
        this.concurrentLimiter = new Semaphore(this.maxConcurrentNumber, true);

        this.suite = suite;
        this.count = count;
        this.qps = qps;

        this.threadPool = new ThreadPoolTaskExecutor();
        this.threadPool.setCorePoolSize(poolSize);
        this.threadPool.setMaxPoolSize(poolSize * 2);
        this.threadPool.setQueueCapacity(count.intValue());
        this.threadPool.initialize();

        this.limiter = RateLimiter.create(qps.intValue());
        this.area = count.intValue() / 10;
        this.area = this.area == 0 ? 1 : this.area;
    }

    public void run() {
        try {
            PerformanceCollector collector = new PerformanceCollector(count.intValue());

            System.out.println("Performance Test: " + suite.getName());
            System.out.println(
                    "===================================================================");
            long startTime = System.currentTimeMillis();
            AtomicInteger sended = new AtomicInteger(0);

            for (Integer i = 0; i < count.intValue(); ++i) {
                Integer finalI = i;
                threadPool.execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                PerformanceSuiteCallback callback = buildCallback(collector);
                                limiter.acquire();
                                suite.call(callback, finalI);
                                int current = sended.incrementAndGet();

                                if (current >= area && ((current % area) == 0)) {
                                    long elapsed = System.currentTimeMillis() - startTime;
                                    double sendSpeed = current / ((double) elapsed / 1000);
                                    System.out.println(
                                            "Already sended: "
                                                    + current
                                                    + "/"
                                                    + count
                                                    + " transactions"
                                                    + ",QPS="
                                                    + sendSpeed);
                                }
                            }
                        });
            }

            // end or not
            while (!collector.isEnd()) {
                Thread.sleep(3000);
                logger.info(
                        " received: {}, total: {}",
                        collector.getReceived().intValue(),
                        collector.getTotal());
            }

            // dump summary
            collector.dumpSummary();

        } catch (Exception e) {
            logger.error("Run exception: " + e);
        }
    }

    private PerformanceSuiteCallback buildCallback(PerformanceCollector collector) {
        try {
            concurrentLimiter.acquire(1);
        } catch (Exception e) {
            System.out.println("Error: concurrentLimiter could not acquire: " + e);
            System.exit(1);
        }
        return new PerformanceSuiteCallback() {
            private Long startTimestamp = System.currentTimeMillis();

            @Override
            public void onSuccess(String message) {
                Long cost = System.currentTimeMillis() - this.startTimestamp;
                collector.onMessage(PerformanceCollector.Status.SUCCESS, cost);
                concurrentLimiter.release(1);
            }

            @Override
            public void onFailed(String message) {
                Long cost = System.currentTimeMillis() - this.startTimestamp;
                System.out.println("On failed: " + message);
                collector.onMessage(PerformanceCollector.Status.FAILED, cost);
                concurrentLimiter.release(1);
            }

            @Override
            public void releaseLimiter() {
                concurrentLimiter.release(1);
            }

            @Override
            public void onSuccessWithoutRelease(String message) {
                Long cost = System.currentTimeMillis() - this.startTimestamp;
                collector.onMessage(PerformanceCollector.Status.SUCCESS, cost);
            }

            @Override
            public void onFailedWithoutRelease(String message) {
                Long cost = System.currentTimeMillis() - this.startTimestamp;
                System.out.println("On failed: " + message);
                collector.onMessage(PerformanceCollector.Status.FAILED, cost);
            }
        };
    }
}
