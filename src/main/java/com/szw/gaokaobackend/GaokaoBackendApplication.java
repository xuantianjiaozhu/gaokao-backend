package com.szw.gaokaobackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.szw.gaokaobackend.mapper")
public class GaokaoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaokaoBackendApplication.class, args);
	}

}
