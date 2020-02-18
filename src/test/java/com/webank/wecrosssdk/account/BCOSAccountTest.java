package com.webank.wecrosssdk.account;

import com.webank.wecrosssdk.config.Default;
import com.webank.wecrosssdk.utils.ConfigUtils;
import java.io.File;
import java.util.Arrays;
import org.fisco.bcos.web3j.crypto.Sign;
import org.junit.Assert;
import org.junit.Test;

public class BCOSAccountTest {
    @Test
    public void buildFromPemTest() throws Exception {
        String name = "bcos1";
        String accountPath = "classpath:accounts" + File.separator + name;
        String address = getAddressFromConfig(accountPath);
        Account account = BCOSAccountFactory.build(name, accountPath);

        Assert.assertEquals(name, account.getName());
        Assert.assertEquals(address, account.getAddress());

        System.out.println(account.getAddress());
    }

    @Test
    public void buildFromP12Test() throws Exception {
        String name = "bcos2";
        String accountPath = "classpath:accounts" + File.separator + name;
        String address = getAddressFromConfig(accountPath);
        Account account = BCOSAccountFactory.build(name, accountPath);

        Assert.assertEquals(name, account.getName());
        Assert.assertEquals(address, account.getAddress());

        System.out.println(account.getAddress());
    }

    @Test
    public void buildFromPemGMTest() throws Exception {
        String name = "bcos3";
        String accountPath = "classpath:accounts" + File.separator + name;
        String address = getAddressFromConfig(accountPath);
        Account account = BCOSAccountFactory.buildGM(name, accountPath);

        Assert.assertEquals(name, account.getName());
        Assert.assertEquals(address, account.getAddress());

        System.out.println(account.getAddress());
    }

    @Test
    public void buildFromP12GMTest() throws Exception {
        String name = "bcos4";
        String accountPath = "classpath:accounts" + File.separator + name;
        String address = getAddressFromConfig(accountPath);
        Account account = BCOSAccountFactory.buildGM(name, accountPath);

        Assert.assertEquals(name, account.getName());
        Assert.assertEquals(address, account.getAddress());

        System.out.println(account.getAddress());
    }

    @Test
    public void signTest() throws Exception {
        String name = "bcos1";
        String accountPath = "classpath:accounts" + File.separator + name;
        String address = getAddressFromConfig(accountPath);
        BCOSAccount account = BCOSAccountFactory.build(name, accountPath);

        byte[] message = {1, 2, 3, 4, 5};
        byte[] signature = account.sign(message);

        System.out.println(Arrays.toString(signature));
    }

    @Test
    public void signGMTest() throws Exception {
        String name = "bcos1";
        String accountPath = "classpath:accounts" + File.separator + name;
        String address = getAddressFromConfig(accountPath);
        BCOSAccount account = BCOSAccountFactory.buildGM(name, accountPath);

        byte[] message = new byte[] {1, 2, 3, 4, 5};
        byte[] signature = account.sign(message);

        System.out.println(Arrays.toString(signature));
    }

    @Test
    public void SignatureEncoderTest() throws Exception {
        byte[] encodedBytes = {
            0, -18, -99, -83, 84, -67, 109, 84, 78, -68, 26, -110, 94, 44, -33, 54, -69, -32, 69,
            102, -15, 25, -36, 61, 33, -51, 25, 108, 14, 55, -27, 77, 97, -118, 3, -63, 5, 109, -87,
            61, 19, 49, 50, 19, 124, 35, -65, 19, -1, 39, 113, 103, 63, -12, 51, -99, -78, -21, -51,
            123, -67, -125, 72, -33, -15, -77, 19, -79, -102, -120, 86, 66, -61, -92, -20, -94, 16,
            74, -26, 34, 35, 10, -36, 50, -55, -93, -24, 76, -6, 27, -60, 56, -33, 53, -108, -123,
            -63, 51, 92, -51, 29, -12, -26, 102, 76, 60, -106, -48, -68, -43, 110, 102, 84, 113,
            -117, -7, -19, 36, -5, -108, 30, -3, -37, -6, 113, 20, 83, -61, 113
        };
        Sign.SignatureData sign = BCOSSignatureEncoder.decode(encodedBytes);
        byte[] cmp = BCOSSignatureEncoder.encode(sign);
        Assert.assertTrue(Arrays.equals(encodedBytes, cmp));
    }

    private String getAddressFromConfig(String accountPath) throws Exception {
        String accountFile =
                ConfigUtils.getToml(accountPath + File.separator + Default.ACCOUNT_CONFIG_NAME)
                        .getTable("account")
                        .getString("accountFile");
        String address = accountFile.split("\\.")[0];
        return address;
    }
}
