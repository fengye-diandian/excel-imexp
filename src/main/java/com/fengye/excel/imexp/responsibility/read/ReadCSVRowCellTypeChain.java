package com.fengye.excel.imexp.responsibility.read;

import cn.hutool.core.util.ObjectUtil;
import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.datatype.DataTypeEnum;
import com.fengye.excel.imexp.exception.CellIsNullException;
import com.fengye.excel.imexp.exception.CellTypeException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 15:23
 * @Desc:校验行列数据类型
 * @Commend:
 */
@Slf4j
public class ReadCSVRowCellTypeChain<T> extends ReadCSVRowChain<T> {

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

            DataTypeEnum dataTypeEnum = annotation.dataType();
            if(dataTypeEnum == null){
                continue;
            }


            // 如果需要访问私有字段的值，需要先设置可访问
            field.setAccessible(true);
            // 这里可以根据需要读取或修改字段的值，但示例中没有展示
            Object o = field.get(row);
            if(ObjectUtil.isEmpty(o)){
                continue;
            }

            if(!getCellFeildMap().containsKey(fieldName)){
                continue;
            }

            if(!dataTypeEnum.getHandler().valid(o)){
                String s = getCellFeildMap().get(fieldName);
                log.error("行数={},列={},字段名={},不是指定={}类型",rowNum,s,fieldName,dataTypeEnum.getName());
                throw new CellTypeException(rowNum,dataTypeEnum.getName(),fieldName,getCellFeildMap().get(fieldName));
            }

        }
        return row;
    }
}
