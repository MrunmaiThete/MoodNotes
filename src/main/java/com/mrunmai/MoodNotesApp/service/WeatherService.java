package com.mrunmai.MoodNotesApp.service;

import com.mrunmai.MoodNotesApp.api.response.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private static final String WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}";

    public WeatherResponse getWeather(String city) {
        try {
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(
                    WEATHER_URL,
                    HttpMethod.GET,
                    null,
                    WeatherResponse.class,
                    city, apiKey
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching weather for city: {}", city, e);
            return null;
        }
    }
}