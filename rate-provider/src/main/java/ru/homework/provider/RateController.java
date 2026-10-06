package ru.homework.provider;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class RateController {
    @PostMapping("/rpc")
    public Map<String, Object> getRate(@RequestBody JsonNode request) {
        if (!"2.0".equals(request.path("jsonrpc").asText())
                || !request.path("id").isIntegralNumber() || request.has("params")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный запрос");
        }
        if (!"getRate".equals(request.path("method").asText())) {
            return Map.of("jsonrpc", "2.0", "id", request.get("id"),
                    "error", Map.of("code", -32601, "message", "Method not found"));
        }
        return Map.of("jsonrpc", "2.0", "id", request.get("id"), "result", 85 + Math.random() * 10);
    }
}
