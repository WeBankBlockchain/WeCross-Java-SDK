package com.webank.wecrosssdk.rpc.common;

import java.util.Arrays;

public class Resources {
    private ResourceDetail[] resourceDetails;

    public ResourceDetail[] getResourceDetails() {
        return resourceDetails;
    }

    public void setResourceDetails(ResourceDetail[] resourceDetails) {
        this.resourceDetails = resourceDetails;
    }

    @Override
    public String toString() {
        return "Resources{" + "resourceDetails=" + Arrays.toString(resourceDetails) + '}';
    }
}
