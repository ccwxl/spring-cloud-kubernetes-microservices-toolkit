package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wxl
 */
@ConfigurationProperties("proxy.config")
public class ProxyProperties {
    private String proxyHost;

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }
}
