package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;

public class Resources {
    private ResourceInfo[] resourceInfos;

    public ResourceInfo[] getResourceInfos() {
        return resourceInfos;
    }

    public void setResourceInfos(ResourceInfo[] resourceInfos) {
        this.resourceInfos = resourceInfos;
    }

    @Override
    public String toString() {
        return "Resources{" + "resourceInfos=" + Arrays.toString(resourceInfos) + '}';
    }
}
