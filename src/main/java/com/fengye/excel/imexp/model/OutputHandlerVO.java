package com.fengye.excel.imexp.model;

import java.util.List;
import java.util.Map;

/**
 * Excel导出数据传输类
 * dataArray 需要导出的数据
 * selectMap Excel 需要选择列的下拉数据 key 属性  val 下拉值
 * @author zhoufeng
 *
 * @param <T>
 */
public class OutputHandlerVO<T> {

	/**
	 * 需要导出的数据
	 */
	private List<T> dataArray;

	/**
	 * 下载模板列的下拉框
	 */
	private Map<String,String[]> selectMap;


	public List<T> getDataArray() {
		return dataArray;
	}
	public void setDataArray(List<T> dataArray) {
		this.dataArray = dataArray;
	}
	public Map<String, String[]> getSelectMap() {
		return selectMap;
	}
	public void setSelectMap(Map<String, String[]> selectMap) {
		this.selectMap = selectMap;
	}
	
}
