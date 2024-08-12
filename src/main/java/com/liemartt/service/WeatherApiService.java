package com.liemartt.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liemartt.dto.LocationResponseDto;
import com.liemartt.dto.WeatherResponseDto;
import com.liemartt.entity.Location;
import com.liemartt.exception.InvalidLocationNameException;
import com.liemartt.exception.LocationNotFoundException;
import com.liemartt.exception.WeatherApiException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WeatherApiService {
    private final String API_KEY = System.getenv("API_KEY");
    private final String BASE_URI = "https://api.openweathermap.org/";
    private final String SUFFIX_WEATHER_URI = "data/2.5/weather";
    private final String SUFFIX_LOCATION_URI = "geo/1.0/direct";
    
    public List<LocationResponseDto> searchLocationsByName(String locationName) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        
        try {
            URI uri = createUriForLocationSearch(locationName);
            request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new InvalidLocationNameException();
        }
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new WeatherApiException("Error fetching weather");
        }
        
        List<LocationResponseDto> locationsFromJson = getLocationsFromJson(response.body());
        if (locationsFromJson.isEmpty()) {
            throw new LocationNotFoundException();
        }
        return locationsFromJson;
    }
    
    public WeatherResponseDto searchWeatherByLocation(Location location) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = createUriForWeatherSearch(location);
        HttpRequest request =
                HttpRequest.newBuilder(uri)
                        .GET()
                        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return getWeatherFromJson(response.body());
    }
    
    private List<LocationResponseDto> getLocationsFromJson(String json) {
        Gson gson = new Gson();
        
        Type listType = new TypeToken<ArrayList<LocationResponseDto>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
    
    private WeatherResponseDto getWeatherFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, WeatherResponseDto.class);
    }
    
    private URI createUriForLocationSearch(String locationName) throws URISyntaxException {
        String locationNameEncoded = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URI)
                .append(SUFFIX_LOCATION_URI)
                .append("?q=")
                .append(locationNameEncoded)
                .append("&appid=")
                .append(API_KEY)
                .append("&limit=5");
        return new URI(sb.toString());
    }
    
    private URI createUriForWeatherSearch(Location location) throws URISyntaxException {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URI)
                .append(SUFFIX_WEATHER_URI)
                .append("?lat=")
                .append(location.getLatitude()
                        .toString())
                .append("&lon=")
                .append(location.getLongitude()
                        .toString())
                .append("&units=metric")
                .append("&appid=")
                .append(API_KEY);
        return new URI(sb.toString());
    }
}
