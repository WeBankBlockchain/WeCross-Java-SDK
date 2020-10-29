package com.webank.wecrosssdk.performance.transfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DagUserMgr {
    private static final Logger logger = LoggerFactory.getLogger(DagUserMgr.class);

    public DagUserMgr(String file) {
        this.file = file;
    }

    private List<DagTransferUser> userList = new ArrayList<>();

    private String file = null;

    private String testType = "transfer";

    public List<DagTransferUser> getUserList() {
        return userList;
    }

    public void setUserList(List<DagTransferUser> userList) {
        this.userList = userList;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public synchronized void addUser(DagTransferUser user) {
        userList.add(user);
    }

    public boolean isEmpty() {
        return userList.isEmpty();
    }

    public DagTransferUser getFrom(int idx) {
        assert !isEmpty() : "Has no user.";
        return userList.get(idx % userList.size());
    }

    public DagTransferUser getTo(int idx) {
        assert !isEmpty() : "Has no user.";
        int mid = userList.size() / 2;
        return userList.get((idx + mid) % userList.size());
    }

    public DagTransferUser getNext(int idx) {
        return userList.get((idx + 1) % userList.size());
    }

    /**
     * Create the specified number of users
     *
     * @param userCount user number
     */
    public void createUser(int userCount) {
        userCount = (userCount > 0 ? userCount : 1);
        long seconds = System.currentTimeMillis() / 1000L;
        for (int i = 0; i < userCount; i++) {
            DagTransferUser dagTransferUser = new DagTransferUser();
            String user = Long.toHexString(seconds) + Integer.toHexString(i);
            BigInteger amount = BigInteger.valueOf(Long.parseLong("1000000000"));
            dagTransferUser.setUser(user);
            dagTransferUser.setAmount(amount);
            this.getUserList().add(dagTransferUser);
        }
    }

    public void writeDagTransferUser() throws IOException {
        if (file == null) {
            return;
        }
        logger.info("file {}, begin load.", file);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)))) {
            for (DagTransferUser dagTransferUser : userList) {
                bw.write(dagTransferUser.getUser() + "\n");
                logger.trace(" write user , user is {}", dagTransferUser.getUser());
            }

            bw.flush();
        }

        logger.info("file {}, load end, count is {}.", file, userList.size());

        System.out.println("Write DagTransferUser end, count is " + userList.size());
    }

    public void loadDagTransferUser() throws IOException {
        if (file == null) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(new File(file)))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    DagTransferUser user = new DagTransferUser();
                    user.setUser(line);
                    addUser(user);
                }
            }

        } catch (Exception e) {
            System.out.println(" load user failed, " + e);
            System.exit(0);
        }

        logger.info("file {}, load end, count is {}.", file, userList.size());

        System.out.println(" Load user end, count is " + userList.size());
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}
