package com.webank.wecrosssdk.account;

import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.config.Default;
import com.webank.wecrosssdk.utils.ConfigUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountsFactory {
    static Logger logger = LoggerFactory.getLogger(AccountsFactory.class);

    private static final String TYPE_BCOS = "BCOS";
    private static final String TYPE_BCOS_GM = "BCOS_SMCRYPTO";
    private static final String TYPE_FABRIC = "Fabric";

    public static Accounts build() {
        Accounts accounts = new Accounts(buildAccountsMap());
        return accounts;
    }

    private static Map<String, Account> buildAccountsMap() {
        Map<String, Account> accountsMap = new HashMap<>();
        try {
            Toml config = ConfigUtils.getToml(Default.ACCOUNTS_CONFIG_FILE);
            List<Map<String, String>> accountConfigList = config.getList("accounts");
            for (Map<String, String> accountConfig : accountConfigList) {
                String name = accountConfig.get("name");
                String type = accountConfig.get("type");

                if (accountsMap.containsKey(name)) {
                    throw new Exception("Duplicate account name: " + name);
                }

                Account account = buildAccount(name, type);
                accountsMap.put(name, account);
            }
        } catch (Exception e) {
            String errorMsg = "Load accounts exception: " + e.getMessage();
            logger.error(errorMsg);
            System.out.println(errorMsg);
            System.exit(1); // If any config failed, just exit!
        }
        return accountsMap;
    }

    private static Account buildAccount(String name, String type) throws Exception {
        if (name == null) {
            throw new Exception(
                    "\"name\" field has not configured in " + Default.ACCOUNTS_CONFIG_FILE);
        }
        if (type == null) {
            throw new Exception(
                    "\"type\" field has not configured in " + Default.ACCOUNTS_CONFIG_FILE);
        }

        String accountPath = Default.ACCOUNTS_BASE + name;
        Account account;
        switch (type) {
            case TYPE_BCOS:
                account = BCOSAccountFactory.build(name, accountPath);
                break;
            case TYPE_BCOS_GM:
                account = BCOSAccountFactory.buildGM(name, accountPath);
                break;
            case TYPE_FABRIC:
                account = FabricAccountFactory.build(name, accountPath);
                break;
            default:
                throw new Exception("Unrecognized account type: " + type);
        }
        return account;
    }
}
