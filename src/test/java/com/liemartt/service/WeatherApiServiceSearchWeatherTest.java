package com.liemartt.service;

import com.liemartt.dto.weather.WeatherApiResponseDto;
import com.liemartt.entity.Location;
import com.liemartt.exception.WeatherApiException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class WeatherApiServiceSearchWeatherTest {
    private final static String WEATHER_FOUND_RESPONSE_JSON =
            """
                    {"coord":{"lon":37.6156,"lat":55.7522},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":17.75,"feels_like":17.56,"temp_min":17.1,"temp_max":18.29,"pressure":1007,"humidity":76,"sea_level":1007,"grnd_level":988},"visibility":10000,"wind":{"speed":5.01,"deg":322,"gust":7.53},"clouds":{"all":100},"dt":1723547230,"sys":{"type":1,"id":9027,"country":"RU","sunrise":1723514284,"sunset":1723569051},"timezone":10800,"id":524901,"name":"Moscow","cod":200}
                    """;
    private final static String WEATHER_API_NOT_AVAILABLE_JSON_RESPONSE =
            """
                    {"cod":401, "message": "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."}
                    """;
    private static WeatherApiService weatherApiService;
    private static HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
    
    @BeforeAll
    @SneakyThrows
    static void setup() {
        HttpClient client = Mockito.mock(HttpClient.class);
        Mockito.when(client.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);
        weatherApiService = new WeatherApiService(client);
    }
    
    @SneakyThrows
    @Test
    void searchWeatherByLocation_validLocation_successfulSearch() {
        Mockito.when(mockResponse.body())
                .thenReturn(WEATHER_FOUND_RESPONSE_JSON);
        Mockito.when(mockResponse.statusCode())
                .thenReturn(200);
        
        Location location = new Location(BigDecimal.valueOf(37.6175), BigDecimal.valueOf(55.7504), "Moscow");
        WeatherApiResponseDto weatherApiResponseDto = weatherApiService.searchWeatherByLocation(location);
        Assertions.assertEquals(weatherApiResponseDto.getName(), "Moscow");
        Assertions.assertEquals(weatherApiResponseDto.getMain()
                .getTemp(), BigDecimal.valueOf(17.75));
    }
    
    @SneakyThrows
    @Test
    void searchWeatherByLocation_weatherApiNotAvailable_failureSearch() {
        Mockito.when(mockResponse.body())
                .thenReturn(WEATHER_API_NOT_AVAILABLE_JSON_RESPONSE);
        Mockito.when(mockResponse.statusCode())
                .thenReturn(401);
        Location location = new Location(BigDecimal.valueOf(37.6175), BigDecimal.valueOf(55.7504), "Moscow");
        
        Assertions.assertThrows(WeatherApiException.class, () -> weatherApiService.searchWeatherByLocation(location));
    }
}
