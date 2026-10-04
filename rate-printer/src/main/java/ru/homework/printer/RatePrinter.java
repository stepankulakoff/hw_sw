package ru.homework.printer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RatePrinter {
    private final RateClient client;
    private final String address;

    public RatePrinter(RateClient client, @Value("${provider.url}") String address) {
        this.client = client;
        this.address = address;
    }

    @Scheduled(fixedRate = 5000)
    public void printRate() {
        try {
            System.out.println("USD/RUB: " + client.getRate(address));
        } catch (Exception e) {
            System.out.println("Не удалось получить курс: " + e.getMessage());
        }
    }
}
