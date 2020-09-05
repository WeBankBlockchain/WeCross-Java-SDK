package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.config.RPCConfig;
import com.webank.wecrosssdk.rpc.annotation.Account;
import com.webank.wecrosssdk.rpc.annotation.Path;
import com.webank.wecrosssdk.rpc.annotation.Transactional;
import com.webank.wecrosssdk.rpc.methods.response.TransactionResponse;
import java.util.Arrays;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

public class AnnotationTest {
    private TransactionalTest transactionalTest;
    private static final String[] ACCOUNT = {"bcos_user1"};
    private static final String[] PATH = {"payment.bcos.evidence"};

    @Before
    public void getContextBean() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RPCConfig.class);
        transactionalTest = context.getBean(TransactionalTest.class);
    }

    @Test
    public void transactionalTest() {
        try {
            transactionalTest.AnnotationTests(ACCOUNT, PATH);
            String[] result = transactionalTest.sendTransactionWithoutTransactional(ACCOUNT, PATH);
            Assert.assertTrue(Arrays.asList(result).contains("helloWorld"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Component
class TransactionalTest {

    @Resource private WeCrossRPC weCrossRPC;

    @Transactional(rollbackFor = Exception.class)
    public void AnnotationTests(@Account String[] accounts, @Path String[] paths) throws Exception {

        weCrossRPC.invoke(paths[0], accounts[0], "newEvidence", "evidence0", "").send();
        weCrossRPC.invoke(paths[0], accounts[0], "newEvidence", "evidence0", "helloWorld").send();
    }

    public String[] sendTransactionWithoutTransactional(String[] accounts, String[] paths)
            throws Exception {
        TransactionResponse transactionResponse =
                weCrossRPC.invoke(paths[0], accounts[0], "queryEvidence", "evidence0").send();
        return transactionResponse.getReceipt().getResult();
    }
}
