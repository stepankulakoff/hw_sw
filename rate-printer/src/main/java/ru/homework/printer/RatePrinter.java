package ru.homework.printer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatePrinter {
    private final RateClient client;
    private final ServiceDiscovery discovery;
    private int nextServer;

    public RatePrinter(RateClient client, ServiceDiscovery discovery) {
        this.client = client;
        this.discovery = discovery;
    }

    @Scheduled(fixedRate = 5000)
    public void printRate() {
        try {
            List<String> servers = discovery.addresses();
            if (servers.isEmpty()) {
                System.out.println("Нет доступных серверов");
                return;
            }
            nextServer = nextServer % servers.size();
            String address = servers.get(nextServer);
            nextServer++;
            System.out.println("Серверов: " + servers.size() + " | " + address
                    + " | USD/RUB: " + client.getRate(address));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Не удалось получить курс: " + e.getMessage());
        }
    }
}
