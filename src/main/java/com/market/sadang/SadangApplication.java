package com.market.sadang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SadangApplication {

    public static void main(String[] args) {
        SpringApplication.run(SadangApplication.class, args);
    }
//branch test
}
