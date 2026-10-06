package ru.homework.printer;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RateClient {
    private final RestClient http;

    public RateClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        http = RestClient.builder().requestFactory(factory).build();
    }

    public double getRate(String address) {
        JsonNode response = http.post().uri(address)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("jsonrpc", "2.0", "method", "getRate", "id", 1))
                .retrieve().body(JsonNode.class);

        if (response == null || !response.path("result").isNumber()) {
            throw new IllegalStateException("Сервер не вернул курс");
        }
        return response.get("result").doubleValue();
    }
}
