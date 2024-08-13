package com.fengye.excel.imexp.build.impl;

import com.fengye.excel.imexp.build.AbstractReadCSVChainBuildier;
import com.fengye.excel.imexp.responsibility.read.ReadCSVRowIsNullCheckChain;
import com.fengye.excel.imexp.responsibility.read.ReadCSVRowCellTypeChain;
import org.apache.poi.ss.formula.functions.T;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 15:05
 * @Desc:默认csv处理器
 * @Commend:
 */
public class ReadCSVDefaultChainBuild extends AbstractReadCSVChainBuildier<T> {

    {
        this.addLink(new ReadCSVRowCellTypeChain());
        this.addLink(new ReadCSVRowIsNullCheckChain());
    }
}
