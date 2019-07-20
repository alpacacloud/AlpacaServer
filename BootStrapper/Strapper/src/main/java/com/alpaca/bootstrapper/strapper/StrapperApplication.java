package com.alpaca.bootstrapper.strapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan(basePackages = "com.alpaca.*")
@MapperScan("com.alpaca.**.mapper")
@EnableSwagger2
@SpringBootApplication
public class StrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrapperApplication.class, args);
	}
}
