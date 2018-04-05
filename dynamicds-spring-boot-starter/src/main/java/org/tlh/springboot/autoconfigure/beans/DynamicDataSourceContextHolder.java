package org.tlh.springboot.autoconfigure.beans;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder {

    //通过threadlocal来存储该线程操作的数据源对象
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static List<String> dataSourceIds = new ArrayList<String>();


    /**
     * 设置操作的数据源
     * @param dataSourceType
     */
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }


    /**
     * 获取操作的数据源
     * @return
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }


    /**
     * 清空
     */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }


    /**
     * 判定目标数据源是否存在
     * @param dataSourceId
     * @return
     */
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
