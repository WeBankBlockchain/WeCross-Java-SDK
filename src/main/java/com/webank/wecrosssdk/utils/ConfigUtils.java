package com.webank.wecrosssdk.utils;

import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ConfigUtils {

    public static Toml getToml(String fileName) throws WeCrossSDKException {
        try {
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            return new Toml().read(resolver.getResource(fileName).getInputStream());
        } catch (Exception e) {
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR,
                    "Something wrong with parsing " + fileName + ": " + e);
        }
    }
}
