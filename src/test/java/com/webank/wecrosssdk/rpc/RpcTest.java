package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.methods.Response;
import org.junit.Test;

public class RpcTest {
    @Test
    public void existsTest() {
        String server = "127.0.0.1:8080";

        Service service = new Service(server);
        WeCrossRpc weCrossRpc = WeCrossRpc.init(service);
        Response response = weCrossRpc.exists("payment.bcos1.HelloWorldContract").send();
        System.out.println("Test exists: " + response.getData());
    }

    @Test
    public void callTest() {
        String server = "127.0.0.1:8080";

        Service service = new Service(server);
        WeCrossRpc weCrossRpc = WeCrossRpc.init(service);
        Response response =
                weCrossRpc
                        .call(
                                "payment.bcos1.HelloWorldContract",
                                "setAndgetConstant",
                                "hello world",
                                10086)
                        .send();
        System.out.println("Test call: " + response.getData());
    }

    @Test
    public void sendTransactionTest() {
        String server = "127.0.0.1:8080";

        Service service = new Service(server);
        WeCrossRpc weCrossRpc = WeCrossRpc.init(service);
        Response response =
                weCrossRpc
                        .sendTransaction(
                                "payment.bcos1.HelloWorldContract",
                                "setAndget",
                                "hello world",
                                10086)
                        .send();
        System.out.println("Test sendTransaction: " + response.getData());
    }
}
