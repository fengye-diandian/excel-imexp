package com.fengye.excel.imexp.demo;

import com.fengye.excel.imexp.demo.model.CustomerVO;

import com.fengye.excel.imexp.excel.AbstractExcelParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Excel导入
 */
@Component
public class CustomerExcelParse extends AbstractExcelParse<CustomerVO> {
	private static final Logger _logger = LoggerFactory.getLogger(CustomerExcelParse.class);

	@Override
	public void dataHandle(List<CustomerVO> model) {
		for (CustomerVO vo : model) {
			String shoppNamePy = vo.getShopName();
			// 处理从Excel导出的数据

		}
	}

}
