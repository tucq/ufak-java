package com.ufak.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

@Slf4j
public class ApplicationYmlUtil {

    /**
     * 获取配置文件里面的内容
     * @param key  属性的key值
     * @return
     */
    public static Object getValue(String key) {
        String profiles = getProfilesActive();
        Resource resource = new ClassPathResource("application-" + profiles + ".yml");
        Properties properties = null;
        try {
            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
            yamlFactory.setResources(resource);
            properties = yamlFactory.getObject();
        } catch (Exception e) {
            log.error("获取application-" + profiles + ".yml配置文件参数异常：",e);
            e.printStackTrace();
            return null;
        }
        return properties.get(key);
    }

    private static String getProfilesActive() {
        Resource resource = new ClassPathResource("application.yml");
        Properties properties = null;
        try {
            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
            yamlFactory.setResources(resource);
            properties = yamlFactory.getObject();
        } catch (Exception e) {
            log.error("获取application.yml配置文件参数异常：",e);
            return null;
        }
        return (String)properties.get("spring.profiles.active");
    }

}
