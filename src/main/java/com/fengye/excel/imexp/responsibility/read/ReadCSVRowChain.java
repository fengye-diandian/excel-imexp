package com.fengye.excel.imexp.responsibility.read;


import cn.hutool.core.map.MapUtil;
import com.fengye.excel.imexp.build.impl.ReadCSVDefaultChainBuild;
import com.fengye.excel.imexp.csv.CsvReader;
import com.fengye.excel.imexp.demo.model.CSVDemoVO;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 13:59
 * @Desc:csv行读处理器
 * @Commend:
 */
@Data
public class ReadCSVRowChain<T> {

    private ReadCSVRowChain<T> next;

    /**
     * @desc:列字段映射
     */

    private Map<String,String> cellFeildMap;

    /**
     * @desc:添加处理
     */
    public final void addNext(ReadCSVRowChain<T> csvRowHandler){
        if(next == null){
            this.next = csvRowHandler;
            return;
        }
        next.addNext(csvRowHandler);
    }

    /**
     * @desc:读执行处理
     */
    public T execute(T row,Field[] fields,long rowNum) throws NoSuchFieldException, IllegalAccessException{
        return row;
    }

    /**
     * @desc:读执行链路
     */
    public final void doExecuteLinks(T row, Field[] fields,long rowNum) throws NoSuchFieldException, IllegalAccessException{
        this.execute(row,fields,rowNum);
        if(this.next == null){
            return;
        }
        if(MapUtil.isNotEmpty(cellFeildMap)){
            this.next.setCellFeildMap(cellFeildMap);
        }
        this.next.doExecuteLinks(row,fields,rowNum);
    }


    public static void main(String[] args) {


    }

}
