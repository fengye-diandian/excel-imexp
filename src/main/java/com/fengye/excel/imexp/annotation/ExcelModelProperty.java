package com.fengye.excel.imexp.annotation;

import com.fengye.excel.imexp.datatype.DataTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel对应属性注解类
 * @author zhoufeng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelModelProperty{
    // 列头名字
    String name();
    // 列坐标
    int colIndex() default -1;
    // 是否必须 true必须；false 非必须
    boolean required() default false; // 是否可空
    // 是否是下拉框 导出模板时会设置下拉框
    boolean selectbox() default false;
    // 数据类型
    DataTypeEnum dataType() default DataTypeEnum.STRING;
    // 表头单元格注释
    String  comment() default  "";
    // 导出数据时，是否需要设置下拉框，防止导出数据列和下拉框数据列是同一列（有时候导出模板有下拉框，导出数据的时候不需要下拉框）
    boolean exportDataShowBox() default false;
}
