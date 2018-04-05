package org.tlh.springboot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.tlh.springboot.autoconfigure.aspect.DynamicDataSourceAspect;
import org.tlh.springboot.autoconfigure.beans.DynamicDataSourceRegister;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(DataSource.class)
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@Import(DynamicDataSourceRegister.class)
public class DynamicDataSourceAutoConfiguration {

    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect(){
        return new DynamicDataSourceAspect();
    }

}
