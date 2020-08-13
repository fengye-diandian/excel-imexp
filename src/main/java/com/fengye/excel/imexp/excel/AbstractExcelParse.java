package com.fengye.excel.imexp.excel;

import com.fengye.excel.imexp.excel.utils.ImportExcelUtil;
import com.fengye.excel.imexp.model.ParesResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public abstract class AbstractExcelParse<T> {
	private static final Logger _logger = LoggerFactory.getLogger(AbstractExcelParse.class);

	public abstract void dataHandle(List<T> model) throws Throwable ;
	
	@SuppressWarnings("unchecked")
	public ParesResult excelParesHandel(Class<T> clazz, MultipartFile file) throws Throwable {
		try {
			ParesResult<List> result = ImportExcelUtil.importExcel(clazz, file.getInputStream());
			if(result.getCode().equals("0000")){
				dataHandle(result.getData());
			}else{
				return result;
			}
		}catch (Throwable e){
			_logger.error("EXCEL PARSE ERROR", e);
			return ParesResult.error("9999",e.getMessage());
		}
		return ParesResult.success();
	}

}
