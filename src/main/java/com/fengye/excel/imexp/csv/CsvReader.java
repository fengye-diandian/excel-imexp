package com.fengye.excel.imexp.csv;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fengye.excel.imexp.build.impl.ReadCSVDefaultChainBuild;
import com.fengye.excel.imexp.handler.ReadCSVRowHandler;
import com.fengye.excel.imexp.responsibility.read.ReadCSVRowChain;
import com.fengye.excel.imexp.utils.CSVMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/9 11:54
 * @Desc: 读取csv
 * @Commend:
 */
@Slf4j
public class CsvReader<T> {

    protected CsvReader(){}

    private Class<T> tClass;

    /**
     * @desc:需要获取所有行数据，传入集合
     */
    private List<T> allRows;

    /**
     * @desc:处理链路 如加密等
     */
    private ReadCSVRowChain<T> csvRowChain ;

    /**
     * @desc:标题字段映射
     */
    private Map<String,String> titleFildMap;

    /**
     * @desc:数据处理,获得的数据需进行什么操作，处理器执行
     */
    private ReadCSVRowHandler<T> csvRowHandler;

    CsvReader rows(List<T> allRows){
        this.allRows = allRows;
        return this;
    }

    CsvReader csvRowHandler(ReadCSVRowHandler<T> csvRowHandler) {
        this.csvRowHandler = csvRowHandler;
        return this;
    }

    /**
     * @desc:数据处理器
     */

    CsvReader tClass(Class<T> tClass) {
        this.titleFildMap = CSVMapperUtils.titleFeildMap( tClass);
        this.tClass = tClass;
        return this;
    }

    CsvReader csvRowChain(ReadCSVRowChain<T> csvRowChain) {
        this.csvRowChain = csvRowChain;
        return this;
    }

    public ReadCSVRowChain<T> getCsvRowChain(){
        return this.csvRowChain;
    }



    public List<T> getRows() {
        return allRows;
    }

    /**
     * @desc:读取csv handle
     * isBatch true 批量处理，false 全量处理
     */
    public CsvReader<T> read(String csvFile,boolean isBatch){
        List<T> rows = new ArrayList<>();
        try (
                // 使用FileReader来读取文件
                Reader reader = new FileReader(csvFile);
                // 创建CSVParser对象，这里假设CSV文件是以逗号分隔的，并且具有表头
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        ) {
            List<String> headerNames = csvParser.getHeaderNames();
            Map<String,String> row = new HashMap<>();
            // 遍历CSV文件中的每一行（CSVRecord）
            for (CSVRecord csvRecord : csvParser) {
                for (String headerName : headerNames) {
                    if(!titleFildMap.containsKey(headerName)){
                        continue;
                    }
                    String feild = titleFildMap.get(headerName);
                    String val = csvRecord.get(headerName);
                    val = StrUtil.isEmpty(val) ? "": val.trim();
                    row.put(feild,val);
                }
                String jsonStr = JSONObject.toJSONString(row);
                T  bean = JSONObject.parseObject(jsonStr, (Type)tClass);


                //执行处理链路
                if(csvRowChain != null){
                    Class<?> aClass = bean.getClass();
                    Field[] declaredFields = aClass.getDeclaredFields();
                    csvRowChain.setCellFeildMap(titleFildMap);
                    try {
                        csvRowChain.doExecuteLinks(bean, declaredFields, csvRecord.getRecordNumber());
                    }catch (NoSuchFieldException | IllegalAccessException e){
                        log.error("读csv时执行链路异常,错误message:{}",e);
                    }

                }
                rows.add(bean);

                //记录行
                if(allRows != null){
                    allRows.add(bean);
                }

                if(isBatch){
                    csvRowHandler.doHanler(rows,csvParser.getRecordNumber());
                }
            }

            if(!isBatch){
                csvRowHandler.doHanler(rows,csvParser.getRecordNumber());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }



    public static CSVReaderBuilder builder(ReadCSVRowHandler csvRowHandler){
        return new CSVReaderBuilder(csvRowHandler);
    }


    /**
     * @desc:创造构建器
     */
    public static class CSVReaderBuilder extends CsvReader{

        /**
         * @desc:需要获取所有行数据，传入集合
         */
        private List allRows;

        /**
         * @desc:处理链路 如加密等
         */
        private ReadCSVRowChain csvRowChain = new ReadCSVDefaultChainBuild().build();

        /**
         * @desc:数据处理,获得的数据需进行什么操作，处理器执行
         */
        private ReadCSVRowHandler csvRowHandler;

        CSVReaderBuilder(ReadCSVRowHandler csvRowHandler) {
            this.csvRowHandler = csvRowHandler;
        }

        public CSVReaderBuilder AllRows(List allRows) {
            this.allRows = allRows;
            return this;
        }

        public CSVReaderBuilder CsvRowChain(ReadCSVRowChain csvRowChain) {
            this.csvRowChain = csvRowChain;
            return this;
        }

        public <T> CsvReader<T> build(Class<T> tClass){
            return new CsvReader<T>()
                    .rows(allRows)
                    .tClass(tClass)
                    .csvRowHandler(csvRowHandler)
                    .csvRowChain(csvRowChain);
        }
    }




}
