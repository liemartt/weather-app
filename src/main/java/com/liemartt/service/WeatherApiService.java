package com.liemartt.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liemartt.dto.location.LocationApiResponseDto;
import com.liemartt.dto.weather.WeatherApiResponseDto;
import com.liemartt.entity.Location;
import com.liemartt.exception.LocationApiException;
import com.liemartt.exception.LocationNotFoundException;
import com.liemartt.exception.WeatherApiException;
import lombok.SneakyThrows;

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
    private static WeatherApiService INSTANCE;
    private final HttpClient client;
    private final String API_KEY = System.getenv("API_KEY");
    private final String BASE_URI = "https://api.openweathermap.org/";
    private final String SUFFIX_WEATHER_URI = "data/2.5/weather";
    private final String SUFFIX_LOCATION_URI = "geo/1.0/direct";
    
    public WeatherApiService(HttpClient client) {
        this.client = client;
        INSTANCE = this;
    }
    private WeatherApiService() {
        client = HttpClient.newHttpClient();
    }
    public static WeatherApiService getINSTANCE(){
        if(INSTANCE==null){
            INSTANCE = new WeatherApiService();
        }
        return INSTANCE;
    }
    
    @SneakyThrows
    public List<LocationApiResponseDto> searchLocationsByName(String locationName) throws LocationNotFoundException, WeatherApiException {
        
        URI uri = createUriForLocationSearch(locationName);
        HttpRequest request = createRequestFromURI(uri);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new LocationApiException();
        }
        
        List<LocationApiResponseDto> locationsFromJson = getLocationsFromJson(response.body());
        if (locationsFromJson.isEmpty()) {
            throw new LocationNotFoundException("Nothing found");
        }
        return locationsFromJson;
    }
    
    @SneakyThrows
    public WeatherApiResponseDto searchWeatherByLocation(Location location) throws LocationNotFoundException, WeatherApiException {
        URI uri = createUriForWeatherSearch(location);
        HttpRequest request = createRequestFromURI(uri);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new WeatherApiException("Error fetching weather");
        }
        return getWeatherFromJson(response.body());
    }
    
    public List<LocationApiResponseDto> getLocationsFromJson(String json) {
        Gson gson = new Gson();
        
        Type listType = new TypeToken<ArrayList<LocationApiResponseDto>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
    
    public WeatherApiResponseDto getWeatherFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, WeatherApiResponseDto.class);
    }
    
    public URI createUriForLocationSearch(String locationName) throws URISyntaxException {
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
    
    public URI createUriForWeatherSearch(Location location) throws URISyntaxException {
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
    
    public HttpRequest createRequestFromURI(URI uri) {
        return HttpRequest.newBuilder(uri)
                .GET()
                .build();
    }
}
