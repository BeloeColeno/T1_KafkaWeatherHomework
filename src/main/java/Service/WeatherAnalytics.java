package Service;

import Entity.WeatherCondition;
import Entity.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeatherAnalytics {
    private final List<WeatherData> weatherDataList = new CopyOnWriteArrayList<>();

    public void addWeatherData(WeatherData data) {
        weatherDataList.add(data);
        log.info("Добавлены данные о погоде: {}", data);
    }

    public String getMostRainyCity() {
        Map<String, Long> rainyCounts = weatherDataList.stream()
                .filter(data -> WeatherCondition.RAINY.equals(data.getCondition()))
                .collect(Collectors.groupingBy(WeatherData::getCity, Collectors.counting()));

        if (rainyCounts.isEmpty()) {
            return "Нет данных о дождливых днях";
        }

        Map.Entry<String, Long> mostRainy = Collections.max(
                rainyCounts.entrySet(), Map.Entry.comparingByValue());

        return String.format("Наибольшее количество дождливых дней в городе %s: %d",
                mostRainy.getKey(), mostRainy.getValue());
    }

    public String getSunnyDaysCount(String city) {
        long sunnyDays = weatherDataList.stream()
                .filter(data -> data.getCity().equals(city) && WeatherCondition.SUNNY.equals(data.getCondition()))
                .map(data -> data.getDateTime().toLocalDate())
                .distinct()
                .count();

        return String.format("Количество солнечных дней в городе %s: %d", city, sunnyDays);
    }

    public String getHottestDay() {
        Optional<WeatherData> hottest = weatherDataList.stream()
                .max(Comparator.comparingDouble(WeatherData::getTemperature));

        return hottest.map(data -> String.format(
                        "Самая жаркая погода была %s в городе %s: %.1f°C",
                        data.getDateTime().toLocalDate(),
                        data.getCity(),
                        data.getTemperature()))
                .orElse("Нет данных о температуре");
    }

    public String getLowestAverageTemperatureCity() {
        Map<String, Double> avgTemps = weatherDataList.stream()
                .collect(Collectors.groupingBy(WeatherData::getCity,
                        Collectors.averagingDouble(WeatherData::getTemperature)));

        if (avgTemps.isEmpty()) {
            return "Нет данных о температуре";
        }

        Map.Entry<String, Double> lowestAvg = Collections.min(
                avgTemps.entrySet(), Map.Entry.comparingByValue());

        return String.format("Самая низкая средняя температура в городе %s: %.1f°C",
                lowestAvg.getKey(), lowestAvg.getValue());
    }

    public List<String> getWeatherAnalytics() {
        List<String> analytics = new ArrayList<>();
        analytics.add(getMostRainyCity());
        analytics.add(getHottestDay());
        analytics.add(getLowestAverageTemperatureCity());
        analytics.add(getSunnyDaysCount("Екатеринбург"));
        analytics.add(getSunnyDaysCount("Москва"));
        return analytics;
    }
}
