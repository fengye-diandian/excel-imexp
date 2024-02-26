package com.fengye.excel.imexp.demo.web;


import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fengye.excel.imexp.annotation.ExcelModelTitle;
import com.fengye.excel.imexp.demo.CustomerExcelOutput;
import com.fengye.excel.imexp.demo.CustomerExcelParse;
import com.fengye.excel.imexp.demo.model.CustomerVO;
import com.fengye.excel.imexp.excel.AbstractExcelOutput;
import com.fengye.excel.imexp.excel.AbstractExcelParse;
import com.fengye.excel.imexp.demo.ParesResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/excel")
public class ImportEmployeeController {
	
	 private static final Logger logger = LoggerFactory.getLogger(ImportEmployeeController.class);
    /**
     *  导入excel表
     */
    @PostMapping("/importEmployee")
    @ResponseBody
    public ParesResult uploadExcel(@RequestParam("file") MultipartFile file) throws Throwable {
        try{
        	AbstractExcelParse<CustomerVO> base = SpringUtil.getBean(CustomerExcelParse.class);
        	base.excelParesHandel(CustomerVO.class, file);
            return ParesResult.success();
        }catch(Exception e){
            logger.error("导入失败",e);
            return ParesResult.error("9999","导入异常");
        }
    }
    
    /**
     * 导出excel模版
     */
    @GetMapping(path = "/download/template")
    public String downloadEmployeeModel(HttpServletResponse response) throws Throwable {
        try{
            Class<CustomerVO> clazz = CustomerVO.class;
        	// 获取文件名称
        	ExcelModelTitle excelModelTitle = clazz.getAnnotation(ExcelModelTitle.class);
        	String fileName =  excelModelTitle.fileName()+ DateUtil.today();
            response.setContentType("application/xlsx");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("UTF-8"),"iso-8859-1")+".xlsx");
            
            AbstractExcelOutput<CustomerVO> output =  SpringUtil.getBean(CustomerExcelOutput.class);
            BufferedInputStream input = output.outPutTemplate(CustomerVO.class, null);
            
            byte buffBytes[] = new byte[1024];
            OutputStream os = response.getOutputStream();
            int read = 0;
            while ((read = input.read(buffBytes)) != -1) {
                os.write(buffBytes, 0, read);
            }
            os.flush();
            os.close();
            input.close();
            return "下载成功！";
        }catch(Exception e){
            logger.error("downloadEmployeeModel() catch Exception ",e);
            return "下载失败！";
        }
    }

    /**
     * 导出数据
     */
    @GetMapping(path = "/exportDataExcel")
    public String outPutExcel(HttpServletResponse response) throws Throwable {
        try{
            long start = System.currentTimeMillis();
            // 获取文件名称
            ExcelModelTitle excelModelTitle = CustomerVO.class.getAnnotation(ExcelModelTitle.class);
            String fileName =  excelModelTitle.fileName()+DateUtil.today();
            response.setContentType("application/xlsx");
            response.addHeader("Content-Disposition", "attachment;filename="+new String((fileName).getBytes("UTF-8"),"iso-8859-1")+".xlsx");

            AbstractExcelOutput<CustomerVO> output =  SpringUtil.getBean(CustomerExcelOutput.class);
            BufferedInputStream input = output.outPutExcel(CustomerVO.class, null);

            byte buffBytes[] = new byte[1024];
            OutputStream os = response.getOutputStream();
            int read = 0;
            while ((read = input.read(buffBytes)) != -1) {
                os.write(buffBytes, 0, read);
            }
            os.flush();
            os.close();
            input.close();
            long end = System.currentTimeMillis();

            System.out.println(">>>>>>>>导出文件耗时："+(end - start));
            return "下载成功！";
        }catch(Exception e){
            logger.error("downloadEmployeeModel() catch Exception ",e);
            return "下载失败！";
        }
    }
    
}