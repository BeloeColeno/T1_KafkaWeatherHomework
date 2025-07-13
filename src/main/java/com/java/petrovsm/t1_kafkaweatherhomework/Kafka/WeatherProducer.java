package com.java.petrovsm.t1_kafkaweatherhomework.Kafka;

import com.java.petrovsm.t1_kafkaweatherhomework.Entity.WeatherData;
import com.java.petrovsm.t1_kafkaweatherhomework.Service.WeatherDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherProducer {

    private final KafkaTemplate<String, WeatherData> kafkaTemplate;
    private final WeatherDataGenerator weatherDataGenerator;

    @Value("${app.kafka.topic}")
    String topic;

    @Scheduled(fixedRate = 2000)
    public void sendWeatherData() {
        WeatherData weatherData = weatherDataGenerator.generateWeatherData();

        log.info("""
                Отправляю данные о погоде:\s
                -----------------------------
                {}
                -----------------------------""", weatherData);

        kafkaTemplate.send(topic, weatherData.getCity(), weatherData)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Сообщение успешно отправлено");
                    } else {
                        log.error("Ошибка при отправке сообщения: {}", ex.getMessage());
                    }
                });
    }
}

