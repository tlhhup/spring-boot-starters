package org.tlh.springboot.autoconfigure;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.DispatcherServlet;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Order(Integer.MAX_VALUE-10)
@ConditionalOnClass({DispatcherServlet.class,Docket.class})
@ConditionalOnWebApplication
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    //创建API信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()//
                .title(this.swaggerProperties.getTitle())//设置标题
                .description(this.swaggerProperties.getDescription())//设置描述信息
                .termsOfServiceUrl(this.swaggerProperties.getTermsOfServiceUrl())//设置服务URL地址
                .contact(this.swaggerProperties.getContact())//设置联系人
                .version(this.swaggerProperties.getVersion())//设置版本号
                .build();
    }


}

