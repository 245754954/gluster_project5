package cn.edu.nudt.hycloudclient.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyUtil {
    private static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties props;

    static {
        String fileName = "client.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertyUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if (StringUtils.isNotBlank(value)){
            return defaultValue;
        }
        return value.trim();
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if (StringUtils.isNotBlank(value)){
            return value.trim();
        }
        return value;
    }
}
