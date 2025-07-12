package com.java.petrovsm.t1_kafkaweatherhomework;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class T1KafkaWeatherHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(T1KafkaWeatherHomeworkApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            CountDownLatch latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));
            latch.await();
        };
    }

    @PostConstruct
    public void initLog() {
        log.info("==== ПРИЛОЖЕНИЕ ЗАПУЩЕНО ====");
        System.out.println("==== SOUT: ПРИЛОЖЕНИЕ ЗАПУЩЕНО ====");
    }
}
