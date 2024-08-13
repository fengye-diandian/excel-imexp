package com.fengye.excel.imexp.exception;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/12 10:23
 * @Desc: 列表为空异常
 * @Commend:
 */
public class CellIsNullException extends RuntimeException{


    public CellIsNullException(long rowNum,String cellFeild,String cellTitle) {
        super(String.format("第%d行 %s ，字段为空，字段名: %s,标题为: %s",String.valueOf(rowNum),cellFeild,cellTitle));
    }


}
