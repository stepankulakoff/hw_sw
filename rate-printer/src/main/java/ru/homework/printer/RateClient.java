package ru.homework.printer;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RateClient {
    private static final Logger log = LoggerFactory.getLogger(RateClient.class);
    private final RestClient http;
    @Value("${client.id:rate-printer}")
    private String clientId = "rate-printer";

    public RateClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        http = RestClient.builder().requestFactory(factory).build();
    }

    public double getRate(String address) {
        var request = Map.of("jsonrpc", "2.0", "method", "getRate", "id", 1);
        log.info("Request: POST {} client={} body={}", address, clientId, request);
        JsonNode response;
        try {
            var reply = http.post().uri(address)
                    .header("X-Client-Id", clientId)
                    .contentType(MediaType.APPLICATION_JSON).body(request)
                    .retrieve().toEntity(JsonNode.class);
            response = reply.getBody();
            log.info("Response: status={} body={}", reply.getStatusCode().value(), response);
        } catch (RestClientResponseException e) {
            log.warn("Response: status={} body={}", e.getStatusCode().value(),
                    e.getResponseBodyAsString().replace('\n', ' ').replace('\r', ' '));
            throw e;
        } catch (RuntimeException e) {
            log.warn("Request failed: {}", e.toString());
            throw e;
        }

        if (response == null || !response.path("result").isNumber()) {
            throw new IllegalStateException("Сервер не вернул курс");
        }
        return response.get("result").doubleValue();
    }
}
