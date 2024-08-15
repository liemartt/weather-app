package com.liemartt.service;

import com.liemartt.dto.location.LocationApiResponseDto;
import com.liemartt.exception.location.LocationApiException;
import com.liemartt.exception.location.LocationNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class WeatherApiServiceSearchLocationsTest {
    private static WeatherApiService weatherApiService;
    private static final HttpResponse<String> mockResponse = Mockito.mock(HttpResponse.class);
    
    private final static String VALID_LOCATION_RESPONSE_JSON =
            """
            [{"name":"Moscow","local_names":{"gv":"Moscow","mr":"मॉस्को","mi":"Mohikau","hu":"Moszkva","av":"Москва","qu":"Moskwa","gl":"Moscova - Москва","mn":"Москва","my":"မော်စကိုမြို့","is":"Moskva","tl":"Moscow","za":"Moscow","sm":"Moscow","se":"Moskva","ca":"Moscou","ku":"Moskow","fo":"Moskva","cy":"Moscfa","io":"Moskva","ms":"Moscow","tr":"Moskova","oc":"Moscòu","ty":"Moscou","ug":"Moskwa","fr":"Moscou","br":"Moskov","wa":"Moscou","la":"Moscua","ta":"மாஸ்கோ","feature_name":"Moscow","kv":"Мӧскуа","ur":"ماسکو","ie":"Moskwa","jv":"Moskwa","lt":"Maskva","zh":"莫斯科","kn":"ಮಾಸ್ಕೋ","hy":"Մոսկվա","ru":"Москва","ia":"Moscova","ga":"Moscó","gn":"Mosku","ky":"Москва","te":"మాస్కో","sw":"Moscow","no":"Moskva","it":"Mosca","vi":"Mát-xcơ-va","eo":"Moskvo","eu":"Mosku","sc":"Mosca","uz":"Moskva","iu":"ᒨᔅᑯ","sh":"Moskva","su":"Moskwa","sr":"Москва","de":"Moskau","fa":"مسکو","bg":"Москва","pt":"Moscou","tt":"Мәскәү","os":"Мæскуы","fy":"Moskou","ln":"Moskú","zu":"IMoskwa","hr":"Moskva","vo":"Moskva","kg":"Moskva","az":"Moskva","th":"มอสโก","be":"Масква","ka":"მოსკოვი","uk":"Москва","cu":"Москъва","gd":"Moscobha","lg":"Moosko","bs":"Moskva","mk":"Москва","wo":"Mosku","nl":"Moskou","sk":"Moskva","nb":"Moskva","st":"Moscow","sl":"Moskva","da":"Moskva","tk":"Moskwa","cv":"Мускав","bo":"མོ་སི་ཁོ།","af":"Moskou","ce":"Москох","sv":"Moskva","lv":"Maskava","co":"Moscù","el":"Μόσχα","dz":"མོསི་ཀོ","id":"Moskwa","ro":"Moscova","sq":"Moska","dv":"މޮސްކޯ","bi":"Moskow","et":"Moskva","bn":"মস্কো","fi":"Moskova","kl":"Moskva","mg":"Moskva","ba":"Мәскәү","ch":"Moscow","pl":"Moskwa","mt":"Moska","ascii":"Moscow","nn":"Moskva","ps":"مسکو","sg":"Moscow","he":"מוסקווה","tg":"Маскав","ht":"Moskou","am":"ሞስኮ","ab":"Москва","en":"Moscow","ml":"മോസ്കോ","so":"Moskow","ss":"Moscow","cs":"Moskva","ay":"Mosku","kk":"Мәскеу","kw":"Moskva","es":"Moscú","ar":"موسكو","an":"Moscú","hi":"मास्को","ja":"モスクワ","yo":"Mọsko","yi":"מאסקווע","ko":"모스크바","ak":"Moscow","na":"Moscow","li":"Moskou"},"lat":55.7504461,"lon":37.6174943,"country":"RU","state":"Moscow"}]
            """;
    private final static String WEATHER_API_NOT_AVAILABLE_JSON_RESPONSE =
            """
            {"cod":401, "message": "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."}
            """;
    private final static String INVALID_LOCATION_JSON_RESPONSE = "[]";
    
    @BeforeAll
    @SneakyThrows
    static void setup() {
        HttpClient client = Mockito.mock(HttpClient.class);
        Mockito.when(client.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(mockResponse);
        weatherApiService = new WeatherApiService(client);
    }
    
    
    @Test
    void searchLocationsByName_validName_successfulSearch() {
        Mockito.when(mockResponse.body()).thenReturn(VALID_LOCATION_RESPONSE_JSON);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        
        List<LocationApiResponseDto> locations = weatherApiService.searchLocationsByName("Moscow");
        
        Assertions.assertFalse(locations.isEmpty());
        Assertions.assertEquals("Moscow", locations.getFirst().getName());
    }
    
    @Test
    void searchLocationsByName_invalidName_failureSearch() {
        Mockito.when(mockResponse.body()).thenReturn(INVALID_LOCATION_JSON_RESPONSE);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        
        Assertions.assertThrows(LocationNotFoundException.class, ()->weatherApiService.searchLocationsByName("Mosco w"));
    }
    
    @Test
    void searchLocationsByName_weatherApiNotAvailable_failureSearch() {
        Mockito.when(mockResponse.body()).thenReturn(WEATHER_API_NOT_AVAILABLE_JSON_RESPONSE);
        Mockito.when(mockResponse.statusCode()).thenReturn(401);
        
        Assertions.assertThrows(LocationApiException.class, ()->weatherApiService.searchLocationsByName("Moscow"));
    }
    
}