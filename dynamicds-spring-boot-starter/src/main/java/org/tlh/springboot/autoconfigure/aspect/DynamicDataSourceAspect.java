package org.tlh.springboot.autoconfigure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tlh.springboot.autoconfigure.annotation.TargetDataSource;
import org.tlh.springboot.autoconfigure.beans.DynamicDataSourceContextHolder;

@Slf4j
@Aspect
@Order(-10)//保证在@Transational执行之前
public class DynamicDataSourceAspect {

    @Before("@annotation(targetDataSource)")
    public void before(JoinPoint joinpoint, TargetDataSource targetDataSource) {
        //获取目标数据源
        String target = targetDataSource.value();
        log.debug(String.format("DynamicDataSourceAspect--->target:[%s]\t joinpoint:[%s]>", target, joinpoint.getSignature()));
        if (DynamicDataSourceContextHolder.containsDataSource(target)) {
            DynamicDataSourceContextHolder.setDataSourceType(target);
        }
    }

    @After("@annotation(targetDataSource)")
    public void after(JoinPoint joinpoint, TargetDataSource targetDataSource) {
        log.debug(
                String.format("DynamicDataSourceAspect revert datasource-->target:[%s]\t joinpoint:[%s]>", targetDataSource.value(), joinpoint.getSignature()));
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        DynamicDataSourceContextHolder.clearDataSourceType();
    }


}
