package com.fengye.excel.imexp.excel.utils;

import cn.hutool.core.date.DateUtil;

import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.annotation.ExcelModelTitle;
import com.fengye.excel.imexp.datatype.DataTypeEnum;
import com.fengye.excel.imexp.utils.PinyinTool;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhoufeng
 * @Description excel导入导出工具类的实现
 * @Date 2020/8/13 15:20
 * @Param
 * @return
 **/
//@Service
public class ImportExcelUtil {
	private static final Logger _logger = LoggerFactory.getLogger(ImportExcelUtil.class);
    final static String notnullerror = "请填入第{%s}行的{%s},{%s}不能为空";
    final static String errormsg = "第{%s}行的{%s}数据解析错误";

    private final static int colsizeN = 630;
    private final static int colsizeM = 1000;
    private static final int MAX_SELECT_BOX = 255;


    /**
     * 导入Excel
     * 
     * @param clazz
     * @param xls
     * @return
     * @throws Exception
     */
    @SuppressWarnings({  "resource", "rawtypes" })
    public static List importExcel(Class<?> clazz, InputStream xls) throws Throwable {
        try {
            // 取得Excel
            Workbook wb = new XSSFWorkbook(xls);
            Sheet sheet = wb.getSheetAt(0);
            Field[] fields = clazz.getDeclaredFields();
            List<Field> fieldList = new ArrayList(fields.length);
            for (Field field : fields) {
            	// 判断是否是自定义Annotation
                if (field.isAnnotationPresent(ExcelModelProperty.class)) {
                    ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                    // 判断数据下标是否有效
                    if (excelModelProperty.colIndex() != -1) {
                    	// 获取自定义注解属性
                        fieldList.add(field);
                    }
                }
            }
            // 缓存Excel的数据行
            List<Object> modelList = new ArrayList(sheet.getPhysicalNumberOfRows() * 2);
            // 从第三行开始读取数据
            sheet.getColumnBreaks();
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                // 数据模型
                Object model = (Object) clazz.newInstance();
                // 获取行
                Row row = sheet.getRow(i);
                // 判断行是否为空
                if(null == row){
                	_logger.info("行为空");
                	continue;
                }
                for (Field field : fieldList) {
                    if (field.isAnnotationPresent(ExcelModelProperty.class)) {
                        // 获取属性注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        // 根据属性下标获取对应cell
                        Cell cell = row.createCell(excelModelProperty.colIndex());
                        try {
                            if (cell == null || StringUtils.isEmpty(cell.toString().trim())) {
                                // 判断是否允许为空   false=可为空；true=不可为空
                                if (excelModelProperty.required()) {
                                    // 禁止为空项为空，异常
                                    throw new Exception(String.format(notnullerror, (1 + i), excelModelProperty.name(), excelModelProperty.name()));
                                }
                            } else if (field.getType().equals(Date.class)) {
                                if (CellType.STRING.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(), new Date(parseDate(parseString(cell))));
                                } else {
                                    BeanUtils.setProperty(model, field.getName(), new Date(cell.getDateCellValue().getTime()));
                                }
                            } else if (field.getType().equals(Long.class)) {

                                if (CellType.STRING.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(), new Long(parseString(cell)));
                                } else {
                                    BeanUtils.setProperty(model, field.getName(), new Long((long) cell.getNumericCellValue()));
                                }
                            }  else if (field.getType().equals(Integer.class)) {
                                if (CellType.NUMERIC.equals( cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(), (int) cell.getNumericCellValue());
                                } else if (CellType.STRING.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(), parseString(cell));
                                }
                            } else if (field.getType().equals(BigDecimal.class)) {
                                if (CellType.NUMERIC.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(),
                                            new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                                } else if (CellType.STRING.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(), new BigDecimal(parseString(cell)).setScale(2, BigDecimal.ROUND_HALF_UP));
                                }
                            } else if (field.getType().equals(Double.class)) {
                                if (CellType.NUMERIC.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(),
                                            new Double(cell.getNumericCellValue()));
                                } else if (CellType.STRING.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(), new Double(parseString(cell)));
                                }
                            }else {
                                // 属性 数据类型为String
                                if (CellType.NUMERIC.equals(cell.getCellType())) {
                                    BeanUtils.setProperty(model, field.getName(),
                                            new BigDecimal(cell.getNumericCellValue()));
                                } else if (CellType.STRING.equals(cell.getCellType())) {
                                    String cellVal = parseString(cell);
                                    // 判断是否下拉框值
                                    if(excelModelProperty.selectbox()){
//                                        cellVal = PinyinTool.fetchZiMu(cellVal);
                                        if(StringUtils.isEmpty(cellVal)){
                                            cellVal = parseString(cell);
                                        }
                                    }
                                    BeanUtils.setProperty(model, field.getName(), cellVal);
                                }
                            }
                        } catch (Exception e) {
                            throw new Exception(String.format(errormsg, (1 + i), excelModelProperty.name()));
                        }
                    }
                }
                modelList.add(model);
            }
            return modelList;
        } finally {
            xls.close();
        }
    }


    /**
     * 下载模板
     * @param clazz
     * @param map
     * @return
     */
    public static InputStream dowloadTemplate(Class<?> clazz, Map<String, String[]> map){
        return excelModelbyClass(clazz, map, null, true);
    }

    public static InputStream exportDataExcel(Class<?> clazz, List<?> datas, Map<String, String[]> map){
        return excelModelbyClass(clazz, map, datas, false);
    }


    /**
     * 生成Excel导出文件
     *
     * @param clazz
     * @param map
     * @return
     */
    @SuppressWarnings("resource")
	private static InputStream excelModelbyClass(Class<?> clazz, Map<String, String[]> map, List<?> datas, boolean dowTemplate) {
        try {
            if (!clazz.isAnnotationPresent(ExcelModelTitle.class)) {
                throw new Exception("请在此类型中加上ModelTitle注解");
            }
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet();
            // 设置标题样式
            setExcelTitle(wb, sheet, clazz);

            // 获取属性
            Field[] fields = clazz.getDeclaredFields();
            Row headRow = sheet.createRow(1);
            // 记录sheet有多少列
            int colSzie = 0;

            List<ExcelModelProperty> cells = new ArrayList<ExcelModelProperty>();

            for (Field field : fields) {
            	// 判断属性是否加了指定注解
                if (field.isAnnotationPresent(ExcelModelProperty.class)) {
                	// 获取注解
                    ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                    // 判断注解下标，为-1不出现在Excel中
                    if (excelModelProperty.colIndex() == -1)
                        continue;
                    cells.add(excelModelProperty);
                    // 创建一个单元格
                    Cell cell = headRow.createCell(excelModelProperty.colIndex());
                    // 设置单元格内容
                    cell.setCellValue(new XSSFRichTextString(excelModelProperty.name()));
                    // 设置表头样式
                    CellStyle headStyle = setHeadStyle(wb);
                    // 是否不可为空字段
                    if(excelModelProperty.required() == true){
                        headStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());// 设置背景色
                        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    }
                    // 设置单元格样式
                    cell.setCellStyle(headStyle);
                    // 设置单元格注释
                    Comment hssfComment = setComment(sheet, excelModelProperty);
                    if(null != hssfComment){
                        cell.setCellComment(hssfComment);
                    }

                    colSzie++;
                    sheet.autoSizeColumn((short) excelModelProperty.colIndex());
                    sheet.setColumnWidth(excelModelProperty.colIndex(), excelModelProperty.name().length() * colsizeN + colsizeM);
                    // 判断列是否是下拉框
                    setExcelSelectbox(wb, sheet, excelModelProperty,map,field, dowTemplate);

                }
            }

            // 设置单元格样式
            excelColumnStyle(sheet, cells, wb);
            // 设置第一行夸 [colSzie - 1] 列
            sheet.addMergedRegion(new CellRangeAddress(0/*从第几行开始*/, 0/*第几行结束*/,
            		                                   0/*从第几列开始*/, colSzie - 1/*到第几列结束*/));
            // 加载导出的数据
            if(!dowTemplate){
                writExcelCellData(sheet, datas);
            }

            //向外输出数据
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                wb.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] b = os.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(b);
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置Excel title
     * @param wb
     * @param sheet
     * @param clazz
     */
    private static void setExcelTitle(Workbook wb, Sheet sheet, Class<?> clazz){
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeight((short) 400);
        titleStyle.setFont(font);
        Cell titleCell = sheet.createRow(0).createCell(0); // 创建第一行，并在该行创建单元格，设置内容，做为标题行
        /**
         * 获取标题
         */
        ExcelModelTitle excelModelTitle = clazz.getAnnotation(ExcelModelTitle.class);
        titleCell.setCellValue(new XSSFRichTextString(excelModelTitle.name()));
        titleCell.setCellStyle(titleStyle);
    }

    /**
     * 设置表头样式
     * @param wb
     * @return
     */
    private static CellStyle  setHeadStyle(Workbook wb){
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        Font headFont = wb.createFont();
        headFont.setBold(true);
        headFont.setFontHeight((short) 240);
        headStyle.setFont(headFont);
        return headStyle;
    }


    /**
     * 判断列是否是下拉框
     * 只在下载模板时裁判的
     * @param sheet
     * @param excelModelProperty
     * @param map
     * @param field
     */
    private static void setExcelSelectbox(Workbook workbook, Sheet sheet, ExcelModelProperty excelModelProperty, Map<String, String[]> map, Field field, boolean dowTemplate){

        if(dowTemplate){
            // 设置列为下拉框格式   Map的key就是需要设定下拉框单元格的名称
            if (excelModelProperty.selectbox() && map != null && map.get(field.getName()) != null) {
                // 下拉框数据
                String[] box = map.get(field.getName());
                StringBuilder arg = new StringBuilder();
                for (String s : box) {
                    arg.append(s);
                }

                XSSFDataValidationHelper helper = new XSSFDataValidationHelper((XSSFSheet) sheet);
                XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint)helper.createExplicitListConstraint(box);
                //参数顺序：开始行、结束行、开始列、结束列
                // 设定下拉框区域
                CellRangeAddressList regions = new CellRangeAddressList(2/*从第几行开始*/, SpreadsheetVersion.EXCEL2007.getLastRowIndex()/*第几行结束*/,
                        excelModelProperty.colIndex()/*从第几列开始*/,
                        excelModelProperty.colIndex()/*到第几列结束*/);
                XSSFDataValidation validation = (XSSFDataValidation)helper.createValidation(constraint, regions);
                validation.setSuppressDropDownArrow(true);
                validation.setShowErrorBox(true);
                sheet.addValidationData(validation);
            }
        }
    }

    /**
     * 将下拉框值超过255个的值放到隐藏sheet中
     * @param workbook
     * @param values
     * @param field
     */
    private static void selectBoxHiddenValue(Workbook workbook, String[] values, Field field, String hiddenSheetName){
        Sheet hidden = workbook.createSheet(hiddenSheetName);
        //创建单元格对象
        Cell cell = null;
        for(int i=0; i < values.length; i++){
            //根据i创建相应的行对象（说明我们将会把每个元素单独放一行）
            Row row = hidden.createRow(i);
            //创建每一行中的第一个单元格
            cell = row.createCell(0);
            //然后将数组中的元素赋值给这个单元格
            cell.setCellValue(values[i]);
        }
        // 获取当前sheet下标
        int sheetIndex = workbook.getSheetIndex(hidden);
        if(sheetIndex > 0){
            // 设置当前sheet隐藏
            workbook.setSheetHidden(sheetIndex, true);
        }
    }

    /**
     * 设置单元格的注释
     * @param sheet
     * @return
     */
    private static Comment setComment(Sheet sheet, ExcelModelProperty excelModelProperty){
        String cellComment = excelModelProperty.comment();
        if(StringUtils.isNotEmpty(cellComment)){
            // 7 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器.
            Drawing patr = sheet.createDrawingPatriarch();
            // 定义注释的大小和位置,详见文档
            Comment comment = patr.createCellComment(new XSSFClientAnchor(255, 125, 1023, 150, (short)4, 2, (short) 6, 5));
            // 设置注释内容
            comment.setString(new XSSFRichTextString(cellComment));
            // 设置注释作者. 当鼠标移动到单元格上是可以在状态栏中看到该内容.
            comment.setAuthor("admin");
            return comment;
        }
        return null;
    }

    /**
     * 设置Excel单元格样式
     * @param sheet
     * @param cells
     * @param wb
     */
    private static void excelColumnStyle(Sheet sheet, List<ExcelModelProperty> cells, Workbook wb){
        for (ExcelModelProperty index : cells) {
            CellStyle cellStyle = wb.createCellStyle();
            DataFormat format = wb.createDataFormat();
            DataTypeEnum dataType = index.dataType();
            switch (dataType) {
                case DATE:
                    cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
                    break;
                case NUMBER:
                    cellStyle.setDataFormat(format.getFormat("#,##.00"));
                    break;
                case INTEGER:
                    cellStyle.setDataFormat(format.getFormat("0"));
                    break;
                default:
                    cellStyle.setDataFormat(format.getFormat("@"));
                    break;
            }
            // 设置每列默认样式
            sheet.setDefaultColumnStyle(index.colIndex()/*列下标*/, cellStyle);
        }
    }

    /**
     * 向Excel写入导出的数据
     * @param sheet
     */
    private static void writExcelCellData(Sheet sheet, List<?> datas) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        for(Object obj : datas){
            // 导出数据写入Excel
            Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                // 判断属性是否加了指定注解
                if (field.isAnnotationPresent(ExcelModelProperty.class)) {
                    Type type = field.getGenericType();
                    String name = field.getName();
                    // 将属性的首字符大写，方便构造get，set方法
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    // 获取参数get方法
                    Method m = clazz.getMethod("get" + name);
                    if (type.equals(String.class)) {
                        // 调用getter方法获取属性值
                        String value = (String) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value);
                        continue;
                    }
                    if(type.equals(Integer.class)) {
                        // 调用getter方法获取属性值
                        Integer value = (Integer) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value);
                        continue;
                    }

                    if(type.equals(Date.class)){
                        // 调用getter方法获取属性值
                        Date value = (Date) m.invoke(obj);
                        String formatDate = DateUtil.format(value,"yyyy-MM-dd HH:mm:ss");
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(formatDate);
                        continue;
                    }

                    if (type.equals(Boolean.class)) {
                        Boolean value = (Boolean) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value);
                        continue;
                    }

                    if(type.equals(BigDecimal.class)){
                        // 调用getter方法获取属性值
                        BigDecimal value = (BigDecimal) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value.doubleValue());
                        continue;
                    }

                    if(type.equals(Double.class)) {
                        // 调用getter方法获取属性值
                        Double value = (Double) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value.doubleValue());
                        continue;
                    }

                    if(type.equals(Long.class)) {
                        // 调用getter方法获取属性值
                        Long value = (Long) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value.doubleValue());
                        continue;
                    }

                    if(type.equals(Byte.class)) {
                        // 调用getter方法获取属性值
                        Byte value = (Byte) m.invoke(obj);
                        // 获取注解
                        ExcelModelProperty excelModelProperty = field.getAnnotation(ExcelModelProperty.class);
                        dataRow.createCell(excelModelProperty.colIndex()).setCellValue(value.doubleValue());
                        continue;
                    }

                }

            }

        }

    }

    private static String parseString(Cell cell) {
        return String.valueOf(cell.getStringCellValue()).trim();
    }

    private static long parseDate(String dateString) throws java.text.ParseException {
        if (dateString.indexOf("/") == 4) {
            return new SimpleDateFormat("yyyy/MM/dd").parse(dateString).getTime();
        } else if (dateString.indexOf("-") == 4) {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString).getTime();
        } else if (dateString.indexOf("年") == 4) {
            return new SimpleDateFormat("yyyy年MM月dd").parse(dateString).getTime();
        } else if (dateString.length() == 8) {
            return new SimpleDateFormat("yyyyMMdd").parse(dateString).getTime();
        } else {
            return new Date().getTime();
        }
    }

}
