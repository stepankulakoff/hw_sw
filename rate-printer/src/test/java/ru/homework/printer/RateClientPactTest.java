package ru.homework.printer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@PactConsumerTest
@PactTestFor(providerName = "rate-provider", pactVersion = PactSpecVersion.V3)
public class RateClientPactTest {
    @Pact(consumer = "rate-printer", provider = "rate-provider")
    public RequestResponsePact contract(PactDslWithProvider builder) {
        return builder
                .uponReceiving("getRate returns a numeric rate")
                .path("/rpc").method("POST")
                .headers(Map.of("Content-Type", "application/json"))
                .body("{\"jsonrpc\":\"2.0\",\"method\":\"getRate\",\"id\":1}")
                .willRespondWith().status(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(new PactDslJsonBody()
                        .stringValue("jsonrpc", "2.0")
                        .numberValue("id", 1)
                        .numberType("result", 89.42))
                .toPact();
    }

    @Test
    void readsRate(MockServer server) {
        double rate = new RateClient().getRate(server.getUrl() + "/rpc");
        assertEquals(89.42, rate, 0.000001);
    }
}
