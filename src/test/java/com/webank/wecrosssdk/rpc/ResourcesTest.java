package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.ResourcesResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class ResourcesTest {

    public static WeCrossRPC weCrossRPC;

    public static void main(String[] args) throws Exception {

        String server = "127.0.0.1:8080";

        WeCrossService weCrossService = new WeCrossRPCService(server);
        weCrossRPC = WeCrossRPC.init(weCrossService);

        // test exists
        System.out.println("*************** exists ***************");
        Response response = weCrossRPC.exists("payment.bcos2.HelloWorldContract").send();
        System.out.println("Should exists: " + response.toString());

        response = weCrossRPC.exists("test.test.test").send();
        System.out.println("Not exists: " + response.toString());

        // list test
        System.out.println("*************** list ***************");
        ResourcesResponse resourcesResponse = weCrossRPC.list(true).send();
        System.out.println("Local resources: " + resourcesResponse.toString());

        resourcesResponse = weCrossRPC.list(false).send();
        System.out.println("With remote resources: " + resourcesResponse.toString());
    }
}
