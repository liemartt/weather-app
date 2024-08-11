package com.liemartt.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liemartt.dto.WeatherDto;
import com.liemartt.entity.Location;
import com.liemartt.exception.CityNotFoundException;
import com.liemartt.exception.InvalidCityNameException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class OpenWeatherApiService {
    private final String API_KEY = System.getenv("API_KEY");
    
    public Location searchLocation(String city) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        try {
            request =
                    HttpRequest
                            .newBuilder(new URI(
                                    String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                                            city,
                                            API_KEY)))
                            .timeout(Duration.of(10, SECONDS))
                            .GET()
                            .build();
        } catch (URISyntaxException e) {
            throw new InvalidCityNameException();
        }
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new CityNotFoundException();
        }
        return getLocationFromJson(response.body());
    }
    
    public WeatherDto searchWeather(Location location) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest
                        .newBuilder(new URI(
                                String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        API_KEY)))
                        .timeout(Duration.of(10, SECONDS))
                        .GET()
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return getWeatherFromJson(response.body());
    }
    
    private Location getLocationFromJson(String json) {
        Location location = new Location();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        
        String name = jsonObject.get("name").getAsString();
        JsonObject coord = jsonObject.getAsJsonObject("coord");
        BigDecimal latitude = coord.get("lat").getAsBigDecimal();
        BigDecimal longitude = coord.get("lon").getAsBigDecimal();
        
        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
    
    private WeatherDto getWeatherFromJson(String json) {
        WeatherDto weatherDto = new WeatherDto();
        
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject wind = jsonObject.getAsJsonObject("wind");
        JsonObject weather = (JsonObject) jsonObject.getAsJsonArray("weather").get(0);
        JsonObject main = jsonObject.getAsJsonObject("main");
        weatherDto.setMain(weather.get("main").getAsString());
        weatherDto.setDescription(weather.get("description").getAsString());
        weatherDto.setTemp(main.get("temp").getAsDouble());
        weatherDto.setWind_speed(wind.get("speed").getAsDouble());
        return weatherDto;
    }
}
