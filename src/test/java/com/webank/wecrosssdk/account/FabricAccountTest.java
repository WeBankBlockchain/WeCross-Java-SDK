package com.webank.wecrosssdk.account;

import java.io.File;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class FabricAccountTest {
    @Test
    public void allTest() throws Exception {
        String name = "fabric1";
        String accountPath = "classpath:accounts" + File.separator + name;

        FabricAccount account = FabricAccountFactory.build(name, accountPath);
        Assert.assertEquals(name, account.getName());

        byte[] message = {1, 2, 3, 4, 5};
        byte[] signature = account.sign(message);

        System.out.println(Arrays.toString(signature));

        // Assert.assertTrue(account.getSigner().verifySignature(message, signature));
    }
}
