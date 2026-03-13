package com.todo.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Todo应用启动类
 */
@EnableSwagger2
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.todo.app", "com.todo.infrastructure", "com.todo.domain"})
@MapperScan("com.todo.infrastructure.mapper")
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}
