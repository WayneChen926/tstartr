package com.example.demo.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import com.example.demo.util.ConstantUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LocalSettingsEnvironmentPostProcessor implements EnvironmentPostProcessor{
    
    private static final String LOCATION = ConstantUtil.EXTERNALPATH;

    // 外部設定檔 yml版本
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
            SpringApplication application) {
        File file = new File(LOCATION);
        log.trace("file={}",file);
        
        if(file.exists()) {
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            Properties properties = loadProperties(file);
            yaml.setResources(new FileSystemResource(file));
            MutablePropertySources propertySources = environment.getPropertySources();
            propertySources.addFirst(new PropertiesPropertySource("Config", yaml.getObject()));
            log.trace("properties={}",properties);
        }
    }
    
    private Properties loadProperties(File f) {
        FileSystemResource resource = new FileSystemResource(f);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        }catch(IOException e) {
            throw new IllegalStateException("Failed to load local setting from "+ f.getAbsolutePath(),e);
        }
    }
}
