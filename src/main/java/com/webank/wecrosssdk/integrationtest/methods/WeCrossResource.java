package com.webank.wecrosssdk.integrationtest.methods;

import java.util.Objects;

public class WeCrossResource {
    public String type;
    public Integer distance;
    public String path;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeCrossResource)) {
            return false;
        }
        WeCrossResource that = (WeCrossResource) o;
        return Objects.equals(getType(), that.getType())
                && Objects.equals(getDistance(), that.getDistance())
                && Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getDistance(), getPath());
    }

    @Override
    public String toString() {
        return "WeCrossResource{"
                + "type='"
                + type
                + '\''
                + ", distance="
                + distance
                + ", path='"
                + path
                + '\''
                + '}';
    }
}
