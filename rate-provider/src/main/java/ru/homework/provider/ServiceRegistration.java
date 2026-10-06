package ru.homework.provider;

import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
public class ServiceRegistration {
    @Value("${discovery.address}")
    private String address;
    @Value("${discovery.path}")
    private String path;
    @Value("${discovery.host}")
    private String host;
    private CuratorFramework zooKeeper;
    private PersistentNode registration;

    @EventListener(WebServerInitializedEvent.class)
    public void register(WebServerInitializedEvent event) throws Exception {
        zooKeeper = CuratorFrameworkFactory.newClient(address, 10000, 5000,
                new ExponentialBackoffRetry(500, 3));
        zooKeeper.start();
        if (!zooKeeper.blockUntilConnected(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("ZooKeeper недоступен");
        }
        String url = "http://" + host + ":" + event.getWebServer().getPort() + "/rpc";
        registration = new PersistentNode(zooKeeper, CreateMode.EPHEMERAL_SEQUENTIAL,
                true, path + "/server-", url.getBytes(StandardCharsets.UTF_8));
        registration.start();
        if (!registration.waitForInitialCreate(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Не удалось зарегистрировать сервер");
        }
        System.out.println("Сервер зарегистрирован: " + url);
    }

    @PreDestroy
    public void close() throws Exception {
        try {
            if (registration != null) registration.close();
        } finally {
            if (zooKeeper != null) zooKeeper.close();
        }
    }
}
