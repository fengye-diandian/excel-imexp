package com.fengye.excel.imexp.responsibility.write;


import cn.hutool.core.map.MapUtil;
import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.csv.CsvWrite;
import com.fengye.excel.imexp.demo.model.CSVDemoVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 13:59
 * @Desc:csv行写处理器
 * @Commend:
 */
@Data
public class WriteCSVRowChain<T> {

    private WriteCSVRowChain<T> next;

    /**
     * @desc:列字段映射
     */

    private Map<String, ExcelModelProperty> titleFeildInfoMap;

    /**
     * @desc:添加处理
     */
    public final void addNext(WriteCSVRowChain<T> csvRowHandler){
        if(next == null){
            this.next = csvRowHandler;
            return;
        }
        next.addNext(csvRowHandler);
    }

    /**
     * @desc:写执行处理
     */
    public T execute(T row,long rowNum) throws NoSuchFieldException, IllegalAccessException {
        return row;
    }

    /**
     * @desc:写执行链路
     */
    public final void doExecuteLinks(T row, long rowNum) throws NoSuchFieldException, IllegalAccessException{
        this.execute(row,rowNum);
        if(this.next == null){
            return;
        }
        if(MapUtil.isNotEmpty(titleFeildInfoMap)){
            this.next.setTitleFeildInfoMap(titleFeildInfoMap);
        }
        this.next.doExecuteLinks(row,rowNum);
    }

    /**
     *测试
     */
    public static void main(String[] args) throws InterruptedException {




    }

}
