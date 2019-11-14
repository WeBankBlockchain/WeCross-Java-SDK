package com.webank.wecrosssdk.rpc.methods;

import java.util.Objects;

public class SetDataResponse extends Response<SetDataResponse.Status> {

    public SetDataResponse() {
        super();
    }

    public void setStatus(Status status) {
        setData(status);
    }

    public Status getStatus() {
        return getData();
    }

    public static class Status {
        private Integer errorCode;
        private String errorMessage;

        public Integer getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Status)) {
                return false;
            }
            Status status = (Status) o;
            return Objects.equals(getErrorCode(), status.getErrorCode())
                    && Objects.equals(getErrorMessage(), status.getErrorMessage());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getErrorCode(), getErrorMessage());
        }

        @Override
        public String toString() {
            return "Status{"
                    + "errorCode="
                    + errorCode
                    + ", errorMessage='"
                    + errorMessage
                    + '\''
                    + '}';
        }
    }
}
