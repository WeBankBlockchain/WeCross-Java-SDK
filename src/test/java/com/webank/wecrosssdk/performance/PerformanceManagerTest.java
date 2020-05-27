package com.webank.wecrosssdk.performance;

import org.junit.Assert;
import org.junit.Test;

public class PerformanceManagerTest {
    public static class MockPerformanceSuite implements PerformanceSuite {

        @Override
        public String getName() {
            return "MockPerformanceSuite";
        }

        @Override
        public void call(PerformanceSuiteCallback callback) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                Assert.assertTrue(false);
            } finally {
                if (System.currentTimeMillis() % 10 != 0) {
                    callback.onSuccess("Ok: Success happen");
                } else {
                    callback.onFailed("Ok: Failed happen");
                }
            }
        }
    }

    @Test
    public void test() {
        try {
            PerformanceSuite suite = new MockPerformanceSuite();
            PerformanceManager manager = new PerformanceManager(suite, 10000, 1000);
            manager.run();
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            Assert.assertTrue(false);
        }
    }
}
