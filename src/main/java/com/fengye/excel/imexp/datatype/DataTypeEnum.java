package com.fengye.excel.imexp.datatype;

import com.fengye.excel.imexp.valid.CellTypeValidHandler;
import com.fengye.excel.imexp.valid.impl.*;

public enum DataTypeEnum {

	NUMBER("数字类型", new CellNumberTypeValidHandler()),  // 数字类型
	STRING("字符串类型", new CellStringTypeValidHandler()),  // 字符串类型
	DATE("日期类型", new CellDateTypeValidHandler()),    // 日期类型
	INTEGER("整数类型", new CellIntegerTypeValidHandler()),// 整数类型
	DATETIME("日期时间类型", new CellDateTimeTypeValidHandler());//日期时间类型

	private String name;

	private CellTypeValidHandler handler;

	public String getName() {
		return name;
	}

	public CellTypeValidHandler getHandler() {
		return handler;
	}

	DataTypeEnum(String name, CellTypeValidHandler handler) {
		this.name = name;
		this.handler = handler;
	}

	public static DataTypeEnum mapping(DataTypeEnum typeEnum){
		DataTypeEnum[] values = DataTypeEnum.values();
		for (DataTypeEnum value : values) {
			if(value == typeEnum){
				return typeEnum;
			}
		}

		return null;
	}
}
