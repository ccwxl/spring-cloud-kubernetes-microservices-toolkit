package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wxl
 */
@ConfigurationProperties("proxy.config")
public class ProxyProperties {

    private String host;

    private int port;

    private String prefix="/apisixporxy";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
