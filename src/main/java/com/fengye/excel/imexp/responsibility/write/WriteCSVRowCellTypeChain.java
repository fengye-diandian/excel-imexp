package com.fengye.excel.imexp.responsibility.write;


import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.datatype.DataTypeEnum;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 13:59
 * @Desc:csv行写 处理数字和日期类型防止数据转换
 * @Commend:
 */
@Data
public class WriteCSVRowCellTypeChain<T> extends WriteCSVRowChain<T>{

    /**
     * @desc:写执行处理
     */
    @Override
    public T execute(T row,  long rowNum) throws NoSuchFieldException, IllegalAccessException {

        Set<Map.Entry<String, ExcelModelProperty>> entries = getTitleFeildInfoMap().entrySet();
        for (Map.Entry<String, ExcelModelProperty> entry : entries) {
            ExcelModelProperty value = entry.getValue();
            String key = entry.getKey();
            if(value.dataType() == DataTypeEnum.NUMBER
                    || value.dataType() == DataTypeEnum.INTEGER
                    || value.dataType() == DataTypeEnum.DATETIME
                    || value.dataType() == DataTypeEnum.DATE){

                    Field declaredField = row.getClass().getDeclaredField(key);
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(row);
                    String val = o != null ? o + "\t" : "";
                    declaredField.set(row,val);

            }
        }
        return row;
    }


}
