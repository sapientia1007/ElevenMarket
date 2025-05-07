package com.wid.elevenmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ElevenMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElevenMarketApplication.class, args);
    }

}
