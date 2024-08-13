package com.fengye.excel.imexp.build;

import com.fengye.excel.imexp.responsibility.read.ReadCSVRowChain;
import lombok.Data;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 14:59
 * @Desc: 读责任链构建器
 * @Commend:
 */
@Data
public abstract class AbstractReadCSVChainBuildier<T> {

    private ReadCSVRowChain<T> csvRowHandler = new ReadCSVRowChain<>();

    /**
     * @desc:构建链式调用
     * @return
     */
    public ReadCSVRowChain build(){
        return csvRowHandler;
    }

    /**
     * @desc:增加处理链路
     * @param csvRowHandler
     * @return
     */
    public AbstractReadCSVChainBuildier<T> addLink(ReadCSVRowChain<T> csvRowHandler){
        this.csvRowHandler.addNext(csvRowHandler);
        return this;
    }

}
