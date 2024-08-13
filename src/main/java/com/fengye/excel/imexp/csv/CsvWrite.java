package com.fengye.excel.imexp.csv;

import cn.hutool.core.lang.Assert;
import com.fengye.excel.imexp.annotation.ExcelModelProperty;
import com.fengye.excel.imexp.build.impl.WriteCSVDefaultChainBuild;
import com.fengye.excel.imexp.responsibility.write.WriteCSVRowChain;
import com.fengye.excel.imexp.utils.CSVMapperUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhoufeng
 * @Description:csv文件导出
 * @date 2020-09-30 15:21
 */
@Slf4j
@Getter
public class CsvWrite<T> {

    /**
     * @desc: 文件路径
     */
    private String outPutPath;

    /**
     * @desc: 文件名称
     */
    private String fileName;

    /**
     * @desc: 类型
     */
    private Class<T> tClass;

    /**
     * @desc: 注解映射
     */
    private Map<String, ExcelModelProperty> titleFeildInfoMap;

    /**
     * @desc: 链是处理
     */
    private WriteCSVRowChain<T> writeCSVRowChain;

    protected CsvWrite() {
    }

    CsvWrite tClass(Class<T> tClass){
        this.tClass = tClass;
        titleFeildInfoMap = CSVMapperUtils.titleFeildInfoMap(tClass);
        return this;
    }

    CsvWrite chain(WriteCSVRowChain<T> writeCSVRowChain){
        this.writeCSVRowChain = writeCSVRowChain;
        return this;
    }

    CsvWrite fileName(String fileName){
        this.fileName = fileName;
        return this;
    }

    CsvWrite outPutPath(String outPutPath){
        this.outPutPath = outPutPath;
        return this;
    }

    private volatile Integer lock = 0;



    /****************************逻辑***************************************/

    /**
     * @desc:数据组装
     */
    public void assembly(List<T> allRow,List<List<String>> allRows){

        int size = titleFeildInfoMap.size();
        //内容数据
        for (T allDatum : allRow) {
            List<String> feilds = assemblyData(allDatum,size);
            allRows.add(feilds);
        }
    }

    /**
     * @desc:数据组装
     */
    protected List<String> assemblyTitle(){
        Assert.notNull(titleFeildInfoMap,"未能解析列头信息");
        int size = titleFeildInfoMap.size();
        List<String> title = new ArrayList<>(size);
        //标题
        Collection<ExcelModelProperty> values = titleFeildInfoMap.values();
        for (ExcelModelProperty entry : values) {
            title.add(entry.colIndex(),entry.name());
        }
        return title;

    }

    /**
     * @desc:获取数据
     */
    protected List<String> assemblyData(T row,int size){

        //todo：增加数据处理链路
        try {
            writeCSVRowChain.setTitleFeildInfoMap(titleFeildInfoMap);
            writeCSVRowChain.doExecuteLinks(row, size);
        }catch (NoSuchFieldException | IllegalAccessException e){
            log.error("写csv时执行链路异常,错误message:{}",e);
        }
        List<String> rowCellData = new ArrayList<>(size);
        Set<String> feildNames = titleFeildInfoMap.keySet();
        for (String feildName : feildNames) {
            try {
                Field declaredField = row.getClass().getDeclaredField(feildName);
                declaredField.setAccessible(true);
                ExcelModelProperty excelModelProperty = titleFeildInfoMap.get(feildName);
                Object o = declaredField.get(row);
                String val = o != null ? o.toString() : "";
                rowCellData.add(excelModelProperty.colIndex(), val);
            }catch (NoSuchFieldException e){
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return rowCellData;

    }

    /**
     * 将allData 生成csv文件
     * @return
     */
    public void writeAll(List<T> allData) {
       Assert.notNull(tClass,"tClass 不能为空");
       List<List<String>> allRows = new ArrayList<>();
       List<String> title = assemblyTitle();
       assembly(allData,allRows);
       doWrite(allRows,title);
    }

    /**
     * 生成为CSV文件
     * @param exportData 源数据List
     * @return
     */
    protected void doWrite(List<List<String>> exportData,List<String> title) {
        // 指定 CSV 文件路径
        String csvFile = outPutPath + fileName;
        String[] titleArray = title.toArray(new String[0]);
        // 使用 try-with-resources 语句自动关闭资源
        try (
                // 创建 FileWriter
                FileWriter writer = new FileWriter(csvFile);
                // 将 FileWriter 包装成 BufferedWriter
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                // 创建 CSVPrinter，使用默认的 CSVFormat.DEFAULT 格式
                CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT
                        .withHeader(titleArray)) // 带有表头的 CSV
        ) {
            // 写入一些数据
            for (List<String> exportDatum : exportData) {
                String[] dataRow = exportDatum.toArray(new String[0]);
                csvPrinter.printRecord(dataRow);
            }
            // 刷新输出流以确保所有数据都被写入文件
            csvPrinter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * CSV文件数据追加
     * @param exportData 源数据List
     * @return
     */
    protected void doWrite(List<List<String>> exportData) {
// 指定 CSV 文件路径
        String csvFile = outPutPath + fileName;
        // 使用 try-with-resources 语句自动关闭资源
        try (
                FileWriter writer = new FileWriter(csvFile, true);
                // 将 FileWriter 包装成 BufferedWriter
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                // 创建 CSVPrinter，使用默认的 CSVFormat.DEFAULT 格式
                // 注意：这里不设置表头，因为通常不会在追加数据时再次添加表头
                CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT)
        ) {
            // 写入一些数据
            for (List<String> exportDatum : exportData) {
                String[] dataRow = exportDatum.toArray(new String[0]);
                csvPrinter.printRecord(dataRow);
            }
            // 刷新输出流以确保所有数据都被写入文件
            csvPrinter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    int i = 0;

    /**
     * 批量写入
     */
    public void writeBatch(List<T> allData) {
        Assert.notNull(tClass,"tClass 不能为空");

        synchronized (lock){
            List<List<String>> allRows = new ArrayList<>();
            assembly(allData,allRows);
            System.err.println(i++);
            List<String> title = null;
            if(lock == 0){
                //第一个抢占锁的线程可以写入标题
                title = assemblyTitle();
                lock = 1;
                doWrite(allRows,title);
                return;
            }
            doWrite(allRows);
        }
    }



    /*********************************************************************建造者******************************************************************************************/

    public static CsvWriteBuilder builder() {
       return new CsvWriteBuilder();
    }

    public static class CsvWriteBuilder extends CsvWrite {

        CsvWriteBuilder() {
        }

        /**
         * @desc：文件路径
         */
        private String outPutPath;

        /**
         * @desc：文件名
         */
        private String fileName;

        /**
         * @desc: 链是处理
         */
        private WriteCSVRowChain writeCSVRowChain = new WriteCSVDefaultChainBuild().build();

        public CsvWriteBuilder chain(WriteCSVRowChain writeCSVRowChain){
            this.writeCSVRowChain = writeCSVRowChain;
            return this;
        }

        public CsvWriteBuilder fileName(String fileName){
            this.fileName = fileName;
            return this;
        }

        public CsvWriteBuilder outPutPath(String outPutPath){
            this.outPutPath = outPutPath;
            return this;
        }


        public <T> CsvWrite<T> build(Class<T> tClass){
            return new CsvWrite<T>()
                    .tClass(tClass)
                    .fileName(fileName)
                    .chain(writeCSVRowChain)
                    .outPutPath(outPutPath);
        }
    }



}
