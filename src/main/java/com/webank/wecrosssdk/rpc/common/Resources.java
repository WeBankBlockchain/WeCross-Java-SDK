package com.webank.wecrosssdk.rpc.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class Resources {
    private int errorCode;
    private String errorMessage;

    @JsonProperty("resources")
    private List<WeCrossResource> resourceList;

    public Resources() {}

    public Resources(Integer errorCode, String errorMessage, List<WeCrossResource> resourceList) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.resourceList = resourceList;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<WeCrossResource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<WeCrossResource> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resources)) {
            return false;
        }
        Resources resources1 = (Resources) o;
        return Objects.equals(getErrorCode(), resources1.getErrorCode())
                && Objects.equals(getErrorMessage(), resources1.getErrorMessage())
                && Objects.equals(getResourceList(), resources1.getResourceList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getErrorMessage(), getResourceList());
    }

    @Override
    public String toString() {
        return "Resources{"
                + "errorCode="
                + errorCode
                + ", errorMessage='"
                + errorMessage
                + '\''
                + ", resourceList="
                + resourceList
                + '}';
    }
}
