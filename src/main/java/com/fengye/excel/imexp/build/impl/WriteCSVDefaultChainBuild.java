package com.fengye.excel.imexp.build.impl;

import com.fengye.excel.imexp.build.AbstractWriteCSVChainBuilder;
import com.fengye.excel.imexp.responsibility.write.WriteCSVRowCellTypeChain;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 15:05
 * @Desc:默认csv处理器
 * @Commend:
 */
public class WriteCSVDefaultChainBuild extends AbstractWriteCSVChainBuilder<T> {

    {
        this.addLink(new WriteCSVRowCellTypeChain());
    }
}
