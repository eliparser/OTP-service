package com.example.OTP_Exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OtpExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtpExerciseApplication.class, args);
    }
}
