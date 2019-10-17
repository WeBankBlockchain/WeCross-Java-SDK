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
}
