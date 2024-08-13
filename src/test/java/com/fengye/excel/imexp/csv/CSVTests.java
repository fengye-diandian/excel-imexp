package com.fengye.excel.imexp.csv;

import com.fengye.excel.imexp.build.impl.ReadCSVDefaultChainBuild;
import com.fengye.excel.imexp.demo.model.CSVDemoVO;
import com.fengye.excel.imexp.responsibility.read.ReadCSVRowChain;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Outhor: yangyonghuang
 * @Created: 2024/8/13 09:31
 * @Desc:
 * @Commend:
 */
@SpringBootTest
public class CSVTests {

    /**
     * @desc: 读
     */
    @Test
    public void read() {
        ReadCSVRowChain build1 = new ReadCSVDefaultChainBuild()
                .build();


        List<CSVDemoVO> csvDemoVOS = new ArrayList<>();
        CsvReader<CSVDemoVO> build = CsvReader.builder((e,a) ->{
                    if(a % 100 == 0){
                        System.err.println(e);
                    }

                })
                .AllRows(csvDemoVOS)
                .build(CSVDemoVO.class);

        build.read("/Users/yangyonghuang/Downloads/test/lalallala.thread_batch4.csv", true);
    }

    /**
     * @desc: 多线程写入
     */
    @Test
    public void writeThreadBatch() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(10);

        CsvWrite<CSVDemoVO> build = CsvWrite.builder()
                .outPutPath("/Users/yangyonghuang/Downloads/test/")
                .fileName("lalallala.thread_batch4.csv")
                .build(CSVDemoVO.class);
        for(int j = 0; j< 100; j++){
            List<CSVDemoVO> testList = new ArrayList<>();
            for (int i =0;i< 100;i++){
                CSVDemoVO csvDemoVO = new CSVDemoVO(j + "" + i,"test"+i,"2024-08-15" + "\t");
                testList.add(csvDemoVO);
            }
//            build.writeBatch(testList);
            new Thread(()->{
                build.writeBatch(testList);
                count.countDown();
            }).start();
        }

        count.await(1000, TimeUnit.HOURS);
    }

    /**
     * @desc: 批量写入
     */
    @Test
    public void writeBatch() {


        CsvWrite<CSVDemoVO> build = CsvWrite.builder()
                .outPutPath("/Users/yangyonghuang/Downloads/test/")
                .fileName("lalallala.thread_batch4.csv")
                .build(CSVDemoVO.class);
        for(int j = 0; j< 100; j++){
            List<CSVDemoVO> testList = new ArrayList<>();
            for (int i =0;i< 100;i++){
                CSVDemoVO csvDemoVO = new CSVDemoVO(j + "" + i,"test"+i,"2024-08-15" + "\t");
                testList.add(csvDemoVO);
            }
//            build.writeBatch(testList);

             build.writeBatch(testList);

        }


    }


    /**
     * @desc: 单线程全量
     */
    @Test
    public void write() {
        List<CSVDemoVO> testList = new ArrayList<>();
        for (int i =0;i< 100;i++){
            CSVDemoVO csvDemoVO = new CSVDemoVO( "" + i,"test"+i,"2024-08-15" + "\t");
            testList.add(csvDemoVO);
        }
        CsvWrite<CSVDemoVO> build = CsvWrite.builder()
                .outPutPath("/Users/yangyonghuang/Downloads/test/")
                .fileName("lalallala.thread_batch4.csv")
                .build(CSVDemoVO.class);

         build.writeAll(testList);

    }
}
