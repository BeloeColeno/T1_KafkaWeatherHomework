package com.java.petrovsm.t1_kafkaweatherhomework.Kafka;

import com.java.petrovsm.t1_kafkaweatherhomework.Entity.WeatherData;
import com.java.petrovsm.t1_kafkaweatherhomework.Service.WeatherAnalytics;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherConsumer {

    private final WeatherAnalytics weatherAnalytics;
    private int messageCount = 0;

    @PostConstruct
    public void init() {
        log.info("WeatherConsumer инициализирован");
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(WeatherData weatherData) {
        log.info("Получены данные о погоде: {}", weatherData);
        weatherAnalytics.addWeatherData(weatherData);

        messageCount++;

        // Настройка периодичности вывода аналитики раз в 10 сообщений
        if (messageCount % 10 == 0) {
            log.info("Обработано {} сообщений о погоде", messageCount);
            printAnalytics();
        }
    }

    private void printAnalytics() {
        log.info("=== Аналитика погодных данных ===");
        List<String> analytics = weatherAnalytics.getWeatherAnalytics();
        analytics.forEach(log::info);
        log.info("================================");
    }
}
