package com.fengye.excel.imexp.utils;

import com.fengye.excel.imexp.annotation.ExcelModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 14:12
 * @Desc: 对象映射器
 * @Commend:
 */
public class CSVMapperUtils {

    /**
     * @desc: 获取标题映射
     * @param clazz
     * @return
     */
    public static Map<String,String> titleFeildMap(Class clazz){
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }
        Map<String,String> titleFeildMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            ExcelModelProperty annotation = declaredField.getAnnotation(ExcelModelProperty.class);
            if(annotation == null){
                continue;
            }

            titleFeildMap.put(annotation.name(),declaredField.getName());
            titleFeildMap.put(declaredField.getName(),annotation.name());
        }
        return titleFeildMap;
    }


    /**
     * @desc: 字段与属性配置映射
     * @param clazz
     */
    public static Map<String,ExcelModelProperty> titleFeildInfoMap(Class clazz){
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }
        Map<String,ExcelModelProperty> titleFeildInfoMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            ExcelModelProperty annotation = declaredField.getAnnotation(ExcelModelProperty.class);
            if(annotation == null){
                continue;
            }

            titleFeildInfoMap.put(declaredField.getName(),annotation);
        }
        return titleFeildInfoMap;
    }
}
