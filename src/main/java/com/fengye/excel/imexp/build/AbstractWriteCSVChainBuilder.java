package com.fengye.excel.imexp.build;

import com.fengye.excel.imexp.responsibility.read.ReadCSVRowChain;
import com.fengye.excel.imexp.responsibility.write.WriteCSVRowChain;
import lombok.Data;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 14:59
 * @Desc: 写责任链构建器
 * @Commend:
 */
@Data
public abstract class AbstractWriteCSVChainBuilder<T> {

    private WriteCSVRowChain<T> csvRowHandler = new WriteCSVRowChain<>();

    /**
     * @desc:构建链式调用
     * @return
     */
    public WriteCSVRowChain build(){
        return csvRowHandler;
    }

    /**
     * @desc:增加处理链路
     * @param csvRowHandler
     * @return
     */
    public AbstractWriteCSVChainBuilder<T> addLink(WriteCSVRowChain<T> csvRowHandler){
        this.csvRowHandler.addNext(csvRowHandler);
        return this;
    }

}
