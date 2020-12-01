package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;

public class PubResponse extends Response<PubResponse.Pub> {
    public static class Pub {
        private String pub;

        public String getPub() {
            return pub;
        }

        public void setPub(String pub) {
            this.pub = pub;
        }
    }
}
