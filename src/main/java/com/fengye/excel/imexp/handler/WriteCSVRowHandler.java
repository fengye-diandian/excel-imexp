package com.fengye.excel.imexp.handler;

import java.util.List;

/**
 * @desc:写批量处理器
 */
@FunctionalInterface
public interface WriteCSVRowHandler<T> {

    List<T> doGetRows();
}
