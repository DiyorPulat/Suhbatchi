package com.example.suhbatchi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
public class SuhbatchiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuhbatchiApplication.class, args);
    }

}
