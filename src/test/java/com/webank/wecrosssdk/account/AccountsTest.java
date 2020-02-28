package com.webank.wecrosssdk.account;

import org.junit.Assert;
import org.junit.Test;

public class AccountsTest {

    @Test
    public void allTest() throws Exception {
        Accounts accounts = AccountsFactory.build();

        Assert.assertTrue(accounts.size() >= 5);
        Assert.assertNotNull(accounts.getAccount("bcos1"));
        Assert.assertNotNull(accounts.getAccount("bcos2"));
        Assert.assertNotNull(accounts.getAccount("bcos3"));
        Assert.assertNotNull(accounts.getAccount("bcos4"));
        Assert.assertNotNull(accounts.getAccount("fabric1"));

        try {
            accounts.getAccount("null"); // throw exception
            Assert.assertTrue(false); // Assume never goes here
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}
