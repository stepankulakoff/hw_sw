package ru.homework.printer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@Component
public class RatePrinter {
    private final RateClient client;
    private final ServiceDiscovery discovery;
    private int nextServer;
    private boolean stopping;

    public RatePrinter(RateClient client, ServiceDiscovery discovery) {
        this.client = client;
        this.discovery = discovery;
    }

    @EventListener(ContextClosedEvent.class)
    public synchronized void stop() {
        stopping = true;
    }

    @Scheduled(fixedRate = 5000)
    public synchronized void printRate() {
        if (stopping) return;
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
