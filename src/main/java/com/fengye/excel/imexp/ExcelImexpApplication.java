package com.fengye.excel.imexp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.hutool.extra.spring","com.fengye.excel.imexp"})
public class ExcelImexpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelImexpApplication.class, args);
	}

}
