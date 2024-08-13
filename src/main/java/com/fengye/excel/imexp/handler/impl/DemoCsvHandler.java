package com.fengye.excel.imexp.handler.impl;

import com.fengye.excel.imexp.demo.model.CSVDemoVO;
import com.fengye.excel.imexp.handler.ReadCSVRowHandler;

import java.util.List;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 16:55
 * @Desc:
 * @Commend:
 */
public class DemoCsvHandler implements ReadCSVRowHandler<CSVDemoVO> {

    @Override
    public void doHanler(List<CSVDemoVO> rows, long offset) {
        if(rows.size() == 3){
            System.err.println(offset);
            rows.clear();
        }

    }
}
