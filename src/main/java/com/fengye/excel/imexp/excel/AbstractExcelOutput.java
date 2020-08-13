package com.fengye.excel.imexp.excel;

import com.fengye.excel.imexp.excel.utils.ImportExcelUtil;
import com.fengye.excel.imexp.model.OutputHandlerVO;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.Map;

/**
 * Excel导出抽象工具类
 * @param <T>
 */

public abstract class AbstractExcelOutput<T> {
	
	public abstract OutputHandlerVO<T> queryOutputData(Object object) throws Throwable;

	public abstract OutputHandlerVO<T> queryTemplateComboBox(Object object) throws Throwable;

    /**
     * 导出模板
	 * 设置下拉框数据
	 * @param clazz
     * @return
     */
	public BufferedInputStream outPutTemplate(Class<T> clazz, Object object) throws Throwable {
		OutputHandlerVO<T> vo = queryTemplateComboBox(object);
		Map<String, String[]> paramMap = vo.getSelectMap();
		BufferedInputStream input = new BufferedInputStream(ImportExcelUtil.dowloadTemplate(clazz , paramMap));
		return input;
	}

	/**
	 * 导出数据
	 * @param clazz
	 * @return
	 */
	public BufferedInputStream outPutExcel(Class<T> clazz, Object object) throws Throwable {
		OutputHandlerVO<T> vo = queryOutputData(object);
		List<T> datas = vo.getDataArray();
		BufferedInputStream input = new BufferedInputStream(ImportExcelUtil.exportDataExcel(clazz ,  datas));
		return input;
	}

}