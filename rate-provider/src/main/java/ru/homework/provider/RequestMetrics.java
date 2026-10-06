package ru.homework.provider;

import io.micrometer.common.KeyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RequestMetrics extends DefaultServerRequestObservationConvention {
    private final List<String> clients;

    public RequestMetrics(@Value("${metrics.clients}") String clients) {
        this.clients = List.of(clients.split(","));
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(ServerRequestObservationContext context) {
        String client = context.getCarrier().getHeader("X-Client-Id");
        if (client == null || !clients.contains(client)) client = "other";
        return super.getLowCardinalityKeyValues(context).and("client", client);
    }
}
