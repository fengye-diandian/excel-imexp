package com.fengye.excel.imexp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出Excel表头
 * @author zhoufeng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelModelTitle {
    // 导出Excel表格表头名称
     public String name();
     // 导出Excel文件名称
     public String fileName();
     // 是否需要导出Excel表格表头
     public boolean titleShow() default true;
 }