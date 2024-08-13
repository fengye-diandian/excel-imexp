package com.fengye.excel.imexp.handler;

import java.util.List;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 16:30
 * @Desc:
 * @Commend:
 */
@FunctionalInterface
public interface ReadCSVRowHandler<T> {

    void doHanler(List<T> rows,long offset);
}
