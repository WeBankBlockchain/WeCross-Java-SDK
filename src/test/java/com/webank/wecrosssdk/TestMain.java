package com.webank.wecrosssdk;

public class TestMain {
    public static void main(String[] args) throws Exception {
        System.out.println("This is WeCross Java SDK");
        System.out.println("For performance test, please run the command for more info:");
        System.out.println(
                "BCOS:\tjava -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.BCOS.BCOSPerformanceTest");
        System.out.println(
                "BCOS:\tjava -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.transfer.BCOSPerformanceTest");
        System.out.println(
                "Fabric:\tjava -cp conf/:lib/*:apps/* com.webank.wecrosssdk.performance.Fabric.FabricPerformanceTest");
        System.out.println(
                "HTLC:\tjava -cp 'conf/:lib/*:apps/*' com.webank.wecrosssdk.performance.htlc.HTLCPerformanceTest");
    }
}
