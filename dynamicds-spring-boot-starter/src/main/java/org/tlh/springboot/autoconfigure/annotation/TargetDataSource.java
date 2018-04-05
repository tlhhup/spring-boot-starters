package org.tlh.springboot.autoconfigure.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {

    /**
     * 设置目标数据源
     * @return
     */
    String value();

}
