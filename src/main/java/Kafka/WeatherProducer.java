package Kafka;

import Entity.WeatherData;
import Service.WeatherDataGenerator;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        log.info("WeatherProducer инициализирован");
    }

    @Scheduled(fixedRate = 2000)
    public void sendWeatherData() {
        WeatherData weatherData = weatherDataGenerator.generateWeatherData();

        log.info("Отправляю данные о погоде: {}", weatherData);

        kafkaTemplate.send(topic, weatherData.getCity(), weatherData)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Успешно отправлено сообщение: {}", weatherData);
                    } else {
                        log.error("Ошибка при отправке сообщения: {}", ex.getMessage());
                    }
                });
    }
}

