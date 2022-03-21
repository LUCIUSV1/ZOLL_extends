package org.lucius;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("org.lucius.mapper")
public class ZolllExtendsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZolllExtendsApplication.class, args);
    }

}
