package com.fengye.excel.imexp.demo.model;

import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.datatype.DataTypeEnum;
import lombok.Data;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 16:47
 * @Desc:
 * @Commend:
 */
@Data
public class CSVDemoVO {

    @ExcelModelProperty(colIndex = 0,name = "编码",dataType = DataTypeEnum.NUMBER)
    private String id;

    @ExcelModelProperty(colIndex = 0,name = "名称",required = true)
    private String name;

    @ExcelModelProperty(colIndex = 0,name = "时间日期",dataType = DataTypeEnum.DATE)
    private String number;

    public CSVDemoVO(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public CSVDemoVO(String id) {
        this.id = id;
    }
}
