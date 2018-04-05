package org.tlh.springboot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "org.tlh.swagger")
public class SwaggerProperties {

    /**
     * title
     */
    private String title = "Swagger API";

    /**
     * description
     */
    private String description = "Swagger API";

    /**
     * termsOfServiceUrl
     */
    private String termsOfServiceUrl = "https://github.com/tlhhup";

    /**
     * contact
     */
    private String contact = "离歌笑";

    /**
     * version
     */
    private String version = "1.0";
}
