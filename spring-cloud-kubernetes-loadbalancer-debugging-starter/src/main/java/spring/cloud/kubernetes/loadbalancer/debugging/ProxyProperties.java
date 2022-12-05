package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wxl
 */
@ConfigurationProperties("proxy.config")
public class ProxyProperties {

    private String host;

    private int port;

    private String k8sNetPrefix;

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

    public String getK8sNetPrefix() {
        return k8sNetPrefix;
    }

    public void setK8sNetPrefix(String k8sNetPrefix) {
        this.k8sNetPrefix = k8sNetPrefix;
    }
}
