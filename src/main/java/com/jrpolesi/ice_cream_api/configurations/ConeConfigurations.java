package com.jrpolesi.ice_cream_api.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jrpolesi.ice_cream_api.configurations.properties.ConeSearchProperties;

@Configuration
public class ConeConfigurations {
    
    @Bean
    @ConfigurationProperties(prefix = "cone.search")
    public ConeSearchProperties coneSearchProperties() {
        return new ConeSearchProperties();
    }
}
