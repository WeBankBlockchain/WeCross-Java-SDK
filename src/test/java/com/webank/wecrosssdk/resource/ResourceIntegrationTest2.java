package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.account.Account;
import com.webank.wecrosssdk.account.Accounts;
import com.webank.wecrosssdk.account.AccountsFactory;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class ResourceIntegrationTest2 {
    public static void main(String[] args) throws Exception {
        String server = "127.0.0.1:8250";

        WeCrossService weCrossService = new WeCrossRPCService(server);
        WeCrossRPC weCrossRPC = WeCrossRPC.init(weCrossService);
        String iPath = "payment.fabric.HelloWeCross";

        Accounts accounts = AccountsFactory.build();
        Account bcos1 = accounts.getAccount("fabric2");

        Resource resource = ResourceFactory.load(weCrossRPC, iPath, bcos1);

        TransactionResponse response = resource.call(new String[] {"String"}, "query", "a");
        System.out.println(response.toString());

        response = resource.sendTransaction(new String[] {"String"}, "invoke", "b", "a", "1");
        System.out.println(response.toString());

        response = resource.call(new String[] {"String"}, "query", "a");
        System.out.println(response.toString());
    }
}
