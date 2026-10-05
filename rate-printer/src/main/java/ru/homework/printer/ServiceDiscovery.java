package ru.homework.printer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ServiceDiscovery {
    private final CuratorFramework zooKeeper;
    private final String path;

    public ServiceDiscovery(@Value("${discovery.address}") String address,
                            @Value("${discovery.path}") String path) {
        zooKeeper = CuratorFrameworkFactory.newClient(address, 10000, 5000,
                new ExponentialBackoffRetry(500, 3));
        this.path = path;
    }

    @PostConstruct
    public void connect() throws InterruptedException {
        zooKeeper.start();
        if (!zooKeeper.blockUntilConnected(10, TimeUnit.SECONDS)) {
            zooKeeper.close();
            throw new IllegalStateException("ZooKeeper недоступен");
        }
    }

    public List<String> addresses() throws Exception {
        List<String> result = new ArrayList<>();
        try {
            List<String> servers = zooKeeper.getChildren().forPath(path);
            Collections.sort(servers);
            for (String server : servers) {
                try {
                    byte[] data = zooKeeper.getData().forPath(path + "/" + server);
                    result.add(new String(data, StandardCharsets.UTF_8));
                } catch (KeeperException.NoNodeException ignored) {
                }
            }
        } catch (KeeperException.NoNodeException ignored) {
        }
        return result;
    }

    @PreDestroy
    public void close() {
        zooKeeper.close();
    }
}
