package com.main.web.siwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication

// DB 연결 없이 실행하게 하는 설정
// 이거 있으면 JPA의 구현체로 Repo를 Bean객체로 등록 못함
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

public class SiwaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiwaProjectApplication.class, args);
    }

}
