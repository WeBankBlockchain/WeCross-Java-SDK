package com.webank.wecrosssdk.rpc.methods.response;

public class AuthCodeReceipt {

    private int errorCode = -1;
    private String message;
    private AuthCodeInfo authCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthCodeInfo getAuthCode() {
        return authCode;
    }

    public void setAuthCode(AuthCodeInfo authCode) {
        this.authCode = authCode;
    }

    public static class AuthCodeInfo {
        private String randomToken;
        private String imageBase64;

        public String getRandomToken() {
            return randomToken;
        }

        public void setRandomToken(String randomToken) {
            this.randomToken = randomToken;
        }

        public String getImageBase64() {
            return imageBase64;
        }

        public void setImageBase64(String imageBase64) {
            this.imageBase64 = imageBase64;
        }

        @Override
        public String toString() {
            return "AuthCodeInfo{"
                    + "randomToken='"
                    + randomToken
                    + '\''
                    + ", imageBase64='"
                    + imageBase64
                    + '\''
                    + '}';
        }
    }
}
