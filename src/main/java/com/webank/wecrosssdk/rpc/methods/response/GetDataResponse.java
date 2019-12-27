package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.methods.Response;
import com.webank.wecrosssdk.rpc.methods.response.GetDataResponse.StatusAndValue;
import java.util.Objects;

public class GetDataResponse extends Response<StatusAndValue> {

    public GetDataResponse() {
        super();
    }

    public void setStatusAndValue(StatusAndValue statusAndValue) {
        setData(statusAndValue);
    }

    public StatusAndValue getStatusAndValue() {
        return getData();
    }

    public static class StatusAndValue {
        private Integer errorCode;
        private String errorMessage;
        private String value;

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof StatusAndValue)) {
                return false;
            }
            StatusAndValue that = (StatusAndValue) o;
            return Objects.equals(getErrorCode(), that.getErrorCode())
                    && Objects.equals(getErrorMessage(), that.getErrorMessage())
                    && Objects.equals(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getErrorCode(), getErrorMessage(), getValue());
        }

        @Override
        public String toString() {
            return "StatusAndValue{"
                    + "errorCode="
                    + errorCode
                    + ", errorMessage='"
                    + errorMessage
                    + '\''
                    + ", value='"
                    + value
                    + '\''
                    + '}';
        }
    }
}
