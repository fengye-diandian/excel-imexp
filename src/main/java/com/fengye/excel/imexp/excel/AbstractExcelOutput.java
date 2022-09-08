package com.fengye.excel.imexp.excel;

import com.fengye.excel.imexp.excel.utils.ImportExcelUtil;
import com.fengye.excel.imexp.model.OutputHandlerVO;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.Map;


/**
 * @Author zhoufeng
 * @Description Excel导出抽象工具类
 * @Date 2021/11/22 11:54
 * @Param
 * @return
 **/
public abstract class AbstractExcelOutput<T> {

	/**
	 * @Author zhoufeng
	 * @Description Excel导出执行数据查询抽象函数
	 * @Date 2021/11/22 15:01
	 * @Param [object]
	 * @return com.fengye.excel.imexp.model.OutputHandlerVO<T>
	 **/
	public abstract OutputHandlerVO<T> queryOutputData(Object object) throws Throwable;

	/**
	 * @Author zhoufeng
	 * @Description 下载模板：模板下拉框数据查询抽象函数
	 * @Date 2021/11/22 15:02
	 * @Param [object]
	 * @return com.fengye.excel.imexp.model.OutputHandlerVO<T>
	 **/
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
		OutputHandlerVO<T> voBox = queryTemplateComboBox(object);
		Map<String, String[]> paramMap = voBox.getSelectMap();
		BufferedInputStream input = new BufferedInputStream(ImportExcelUtil.exportDataExcel(clazz, datas, paramMap));
		return input;
	}

}