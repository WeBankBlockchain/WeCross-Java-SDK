package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.account.Account;
import com.webank.wecrosssdk.rpc.WeCrossRPC;

public class ResourceFactory {

    public static Resource load(WeCrossRPC weCrossRPC, String iPath, Account account)
            throws Exception {
        if (account == null) {
            throw new Exception("Account is null");
        }
        Resource resource = new Resource(weCrossRPC, iPath, account);
        resource.load();
        return resource;
    }

    public static Resource load(WeCrossRPC weCrossRPC, String iPath) throws Exception {
        Resource resource = new Resource(weCrossRPC, iPath);
        resource.load();
        return resource;
    }
}
