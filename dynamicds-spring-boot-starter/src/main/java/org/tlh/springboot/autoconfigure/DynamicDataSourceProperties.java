package org.tlh.springboot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "org.tlh.datasource")
public class DynamicDataSourceProperties {

    /**
     * 数据源的别名
     */
    private String[] names;

    // todo 优化属性的设置，助于提示的编写

}
