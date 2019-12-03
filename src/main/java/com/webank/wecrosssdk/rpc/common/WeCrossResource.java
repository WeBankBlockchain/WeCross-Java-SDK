package com.webank.wecrosssdk.rpc.common;

import java.util.Objects;

public class WeCrossResource {
    private String checksum;
    private String type;
    private int distance;
    private String path;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
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
        return Objects.equals(getChecksum(), that.getChecksum())
                && Objects.equals(getType(), that.getType())
                && Objects.equals(getDistance(), that.getDistance())
                && Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChecksum(), getType(), getDistance(), getPath());
    }

    @Override
    public String toString() {
        return "WeCrossResource{"
                + "checksum='"
                + checksum
                + '\''
                + ", type='"
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
