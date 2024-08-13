package com.fengye.excel.imexp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/12 11:03
 * @Desc:数值校验
 * @Commend:
 */
public class ValidUtils {

    /**
     * @desc: 判断是否为日期格式
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static boolean isValidDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); // 设置为不宽松模式，不符合格式的会被认为是错误
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * @desf:数字校验
    */
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * @desf:整形校验
     */
    public static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
}
