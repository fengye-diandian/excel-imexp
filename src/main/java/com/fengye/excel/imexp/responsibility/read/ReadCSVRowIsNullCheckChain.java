package com.fengye.excel.imexp.responsibility.read;

import cn.hutool.core.util.ObjectUtil;
import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.exception.CellIsNullException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/12 09:34
 * @Desc:
 * @Commend:
 */
@Slf4j
public class ReadCSVRowIsNullCheckChain<T> extends ReadCSVRowChain<T> {

    @Override
    public T execute(T row, Field[] fields,long rowNum) throws NoSuchFieldException, IllegalAccessException{
        // 遍历字段
        for (Field field : fields) {
            // 获取字段名
            String fieldName = field.getName();
            // 获取字段的注解
            ExcelModelProperty annotation = field.getAnnotation(ExcelModelProperty.class);
            if (annotation == null) {
                continue;
            }
            // 判断是否必传
            boolean required = annotation.required();
            if(!required){
                continue;
            }


            // 如果需要访问私有字段的值，需要先设置可访问
            field.setAccessible(true);
            // 这里可以根据需要读取或修改字段的值，但示例中没有展示

            Object o = field.get(row);
            if(ObjectUtil.isEmpty(o)){
                if(!getCellFeildMap().containsKey(fieldName)){
                    continue;
                }
                String s = getCellFeildMap().get(fieldName);
                log.error("行数={},列={},字段名={}为空",rowNum,s,fieldName);
                throw new CellIsNullException(rowNum,fieldName,getCellFeildMap().get(fieldName));
            }

        }

        return row;
    }
}
