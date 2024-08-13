package com.fengye.excel.imexp.valid.impl;

import cn.hutool.core.util.StrUtil;
import com.fengye.excel.imexp.utils.ValidUtils;
import com.fengye.excel.imexp.valid.CellTypeValidHandler;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/12 11:13
 * @Desc:数字类型校验
 * @Commend:
 */
public class CellIntegerTypeValidHandler implements CellTypeValidHandler {

    @Override
    public boolean valid(Object o) {
        return ValidUtils.isInteger(StrUtil.toString(o));
    }
}
