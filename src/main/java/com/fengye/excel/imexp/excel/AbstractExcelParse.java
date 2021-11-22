package com.fengye.excel.imexp.excel;

import com.fengye.excel.imexp.excel.utils.ImportExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author zhoufeng
 * @Description Excel解析抽象类
 * @Date 2021/11/22 13:48
 * @Param
 * @return
 **/
public abstract class AbstractExcelParse<T> {
	private static final Logger _logger = LoggerFactory.getLogger(AbstractExcelParse.class);

	/**
	 * @Author zhoufeng
	 * @Description excel导入：处理导入数据的抽象函数
	 * @Date 2021/11/22 15:04
	 * @Param [model]
	 * @return void
	 **/
	public abstract void dataHandle(List<T> model) throws Throwable ;
	
	@SuppressWarnings("unchecked")
	public void excelParesHandel(Class<T> clazz, MultipartFile file) throws Throwable {
		List result = ImportExcelUtil.importExcel(clazz, file.getInputStream());
		dataHandle(result);
	}

}
