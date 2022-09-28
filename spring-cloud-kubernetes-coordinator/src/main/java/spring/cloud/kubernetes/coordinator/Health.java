package spring.cloud.kubernetes.coordinator;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * <a href="https://blog.softwaremill.com/whats-the-proper-kubernetes-health-check-for-a-kafka-streams-application-c9c00a112581">健康检查</a>
 *
 * @author wxl
 */
public class Health {

    private static final int OK = 200;
    private static final int ERROR = 500;

    private HttpServer server;

    Health() {
    }

    void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not setup http server: ", ioe);
        }
        server.createContext("/health", exchange -> {
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        });
        server.start();
    }

    void stop() {
        server.stop(0);
    }
}