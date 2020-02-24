package com.webank.wecrosssdk.utils;

import com.moandjiezana.toml.Toml;
import com.webank.wecrosssdk.config.Default;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.exception.WeCrossSDKException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ConfigUtils {

    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    public static void checkPath(String path) throws WeCrossSDKException {
        String templateUrl = Default.TEMPLATE_URL + path.replace('.', '/');

        try {
            new URL(templateUrl);
        } catch (Exception e) {
            throw new WeCrossSDKException(ErrorCode.ILLEGAL_SYMBOL, "Invalid path: " + path);
        }
    }

    public static Toml getToml(String fileName) throws WeCrossSDKException {
        try {
            if (!fileIsExists(fileName)) {
                throw new Exception(fileName + " does not exists");
            }

            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();
            Path path = Paths.get(resolver.getResource(fileName).getURI());
            String encryptedSecret = new String(Files.readAllBytes(path));
            return new Toml().read(encryptedSecret);
        } catch (Exception e) {
            throw new WeCrossSDKException(
                    ErrorCode.INTERNAL_ERROR,
                    "Something wrong with parse " + fileName + ": " + e.getMessage());
        }
    }

    public static Map<String, Object> getTomlMap(String fileName) throws WeCrossSDKException {
        return getToml(fileName).toMap();
    }

    // Check if the file exists or not
    public static boolean fileIsExists(String path) {
        try {
            PathMatchingResourcePatternResolver resolver_temp =
                    new PathMatchingResourcePatternResolver();
            resolver_temp.getResource(path).getFile();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
