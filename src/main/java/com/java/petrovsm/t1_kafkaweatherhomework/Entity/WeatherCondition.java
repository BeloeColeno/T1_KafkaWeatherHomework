package com.java.petrovsm.t1_kafkaweatherhomework.Entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum WeatherCondition {
    SUNNY("солнечно"),
    CLOUDY("облачно"),
    RAINY("дождь");

    final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static WeatherCondition fromDisplayName(String displayName) {
        for (WeatherCondition condition : values()) {
            if (condition.displayName.equals(displayName)) {
                return condition;
            }
        }
        throw new IllegalArgumentException("Неизвестное состояние погоды: " + displayName);
    }
}
