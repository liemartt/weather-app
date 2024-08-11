package com.liemartt.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class OpenWeatherApiService {
    private final String API_KEY = System.getenv("API_KEY");
    
    public String search(String city) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest
                        .newBuilder(new URI(
                                String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, API_KEY)))
                        .timeout(Duration.of(10, SECONDS))
                        .GET()
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
        
    }
}
