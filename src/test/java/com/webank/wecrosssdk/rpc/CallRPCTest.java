package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.mock.MockWeCrossService;
import com.webank.wecrosssdk.rpc.common.account.BCOSAccount;
import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.*;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CallRPCTest {

    private WeCrossRPC weCrossRPC;

    @Before
    public void initializer() throws Exception {
        WeCrossService service = new MockWeCrossService();
        weCrossRPC = WeCrossRPCFactory.build(service);
    }

    @Test
    public void supportedStubsTest() throws Exception {
        StubResponse stubResponse = weCrossRPC.supportedStubs().send();
        Assert.assertEquals(stubResponse.getErrorCode(), 0);
        Assert.assertNotNull(stubResponse.getStubs());
    }

    @Test
    public void listAccountsTest() throws Exception {
        AccountResponse accountResponse = weCrossRPC.listAccount().send();
        Assert.assertEquals(accountResponse.getErrorCode(), 0);
        Assert.assertNotNull(accountResponse.getAccount());
    }

    @Test
    public void listResourcesTest() throws Exception {
        ResourceResponse resourceResponse = weCrossRPC.listResources(true).send();
        Assert.assertEquals(resourceResponse.getErrorCode(), 0);
        Assert.assertNotNull(resourceResponse.getResources());
    }

    @Test
    public void statusTest() throws Exception {
        Response response = weCrossRPC.status("test.test.test").send();
        Assert.assertEquals(response.getErrorCode(), 0);
    }

    @Test
    public void detailTest() throws Exception {
        ResourceDetailResponse resourceDetailResponse = weCrossRPC.detail("test.test.test").send();
        Assert.assertEquals(resourceDetailResponse.getErrorCode(), 0);
    }

    @Test
    public void callTest() throws Exception {
        TransactionResponse transactionResponse =
                weCrossRPC.call("test.test.test", "test", "test").send();
        Assert.assertEquals(transactionResponse.getErrorCode(), 0);
    }

    @Test
    public void sendTransactionTest() throws Exception {
        TransactionResponse transactionResponse =
                weCrossRPC.sendTransaction("test.test.test", "test", "test").send();
        Assert.assertEquals(transactionResponse.getErrorCode(), 0);
    }

    @Test
    public void callTransactionTest() throws Exception {
        TransactionResponse transactionResponse =
                weCrossRPC.callTransaction("001", "test.test.test", "test", "test").send();
        Assert.assertEquals(transactionResponse.getErrorCode(), 0);
    }

    @Test
    public void execTransactionTest() throws Exception {
        TransactionResponse transactionResponse =
                weCrossRPC.execTransaction("001", "0", "test.test.test", "test", "test").send();
        Assert.assertEquals(transactionResponse.getErrorCode(), 0);
    }

    @Test
    public void startTransactionTest() throws Exception {
        RoutineResponse routineResponse =
                weCrossRPC.startTransaction("001", new String[] {"test.test.test"}).send();
        Assert.assertEquals(routineResponse.getErrorCode(), 0);
    }

    @Test
    public void commitTransactionTest() throws Exception {
        RoutineResponse routineResponse =
                weCrossRPC.commitTransaction("001", new String[] {"test.test.test"}).send();
        Assert.assertEquals(routineResponse.getErrorCode(), 0);
    }

    @Test
    public void rollbackTransactionTest() throws Exception {
        RoutineResponse routineResponse =
                weCrossRPC.rollbackTransaction("001", new String[] {"test.test.test"}).send();
        Assert.assertEquals(routineResponse.getErrorCode(), 0);
    }

    @Test
    public void getTransactionInfoTest() throws Exception {
        RoutineInfoResponse routineInfoResponse =
                weCrossRPC.getTransactionInfo("001", new String[] {"test.test.test"}).send();
        Assert.assertEquals(routineInfoResponse.getErrorCode(), 0);
    }

    @Test
    public void customCommandTest() throws Exception {
        CommandResponse commandResponse =
                weCrossRPC.customCommand("test", "test.test.test", "test").send();
        Assert.assertEquals(commandResponse.getErrorCode(), 0);
    }

    @Test
    public void getTransactionIDsTest() throws Exception {
        RoutineIDResponse routineIDResponse =
                weCrossRPC.getTransactionIDs("test.test.test", 0).send();
        Assert.assertEquals(new String[] {"001"}, routineIDResponse.getIDs());
    }

    @Test
    public void registerTest() throws Exception {
        UAResponse uaResponse = weCrossRPC.register("hello", "world").send();
        Assert.assertEquals(uaResponse.getUAReceipt().getErrorCode(), 0);
    }

    @Test
    public void loginTest() throws Exception {
        UAResponse uaResponse = weCrossRPC.login("hello", "world").send();
        Assert.assertEquals(uaResponse.getUAReceipt().getCredential(), "token");
        Assert.assertEquals(uaResponse.getUAReceipt().getErrorCode(), 0);
    }

    @Test
    public void addChainAccountTest() throws Exception {
        BCOSAccount bcosAccount = new BCOSAccount();
        UAResponse uaResponse = weCrossRPC.addChainAccount("BCOS2.0", bcosAccount).send();
        Assert.assertEquals(uaResponse.getUAReceipt().getErrorCode(), 0);
    }

    @Test
    public void setDefaultAccountTest() throws Exception {
        UAResponse uaResponse = weCrossRPC.setDefaultAccount("BCOS2.0", 0).send();
        Assert.assertEquals(uaResponse.getUAReceipt().getErrorCode(), 0);
    }

    @Test
    public void logoutTest() throws Exception {
        UAResponse uaResponse = weCrossRPC.logout().send();
        Assert.assertEquals(uaResponse.getUAReceipt().getErrorCode(), 0);
    }
}
