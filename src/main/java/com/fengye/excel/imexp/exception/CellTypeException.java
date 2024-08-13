package com.fengye.excel.imexp.exception;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/12 10:23
 * @Desc: 列表为空异常
 * @Commend:
 */
public class CellTypeException extends RuntimeException{



    public CellTypeException(long rowNum, String type,String cellFeild, String cellTitle) {
        super(String.format("第%d行 %s ，字段类型错误，需使用: %s ;字段名: %s,标题为: %s",String.valueOf(rowNum),type,cellFeild,cellTitle));
    }


}
