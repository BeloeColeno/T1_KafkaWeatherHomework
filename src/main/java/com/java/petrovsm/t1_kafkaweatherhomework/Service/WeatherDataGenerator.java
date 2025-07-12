package com.java.petrovsm.t1_kafkaweatherhomework.Service;

import com.java.petrovsm.t1_kafkaweatherhomework.Entity.WeatherCondition;
import com.java.petrovsm.t1_kafkaweatherhomework.Entity.WeatherData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class WeatherDataGenerator {
    private static final List<String> CITIES = List.of(
            "Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Нижний Новгород",
            "Казань", "Челябинск", "Омск", "Самара", "Ростов-на-Дону"
    );
    private static final List<WeatherCondition> CONDITIONS = Arrays.asList(
            WeatherCondition.SUNNY,
            WeatherCondition.CLOUDY,
            WeatherCondition.RAINY
    );

    private final Random random = new Random();

    public WeatherData generateWeatherData() {
        String city = getRandomCity();
        // Настройка рандомного дня в неделе
        LocalDateTime dateTime = LocalDateTime.now().minusDays(random.nextInt(7));
        double temperature = getRandomTemperature();
        WeatherCondition condition = getRandomCondition();

        return new WeatherData(city, dateTime, temperature, condition);
    }

    private String getRandomCity() {
        return CITIES.get(random.nextInt(CITIES.size()));
    }

    private double getRandomTemperature() {
        // Настройка рандомной температуры от 0 до 35
        return random.nextInt(36);
    }

    private WeatherCondition getRandomCondition() {
        return CONDITIONS.get(random.nextInt(CONDITIONS.size()));
    }
}
