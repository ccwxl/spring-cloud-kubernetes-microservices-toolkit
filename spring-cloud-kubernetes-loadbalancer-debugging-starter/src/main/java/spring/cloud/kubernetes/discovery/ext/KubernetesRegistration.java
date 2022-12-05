package spring.cloud.kubernetes.discovery.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.kubernetes.commons.discovery.DefaultKubernetesServiceInstance;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxl
 * KubernetesServiceInstance
 */
@ConfigurationProperties("spring.cloud.kubernetes.discovery.register")
public class KubernetesRegistration implements Registration {

    private boolean enabled = true;

    private String namespace;

    private String instanceId;

    private String serviceId;

    private String host;

    private int port;

    private boolean secure;

    private String scheme;

    private URI uri;

    private Map<String, String> metadata = new HashMap<>();

    public KubernetesRegistration() {
    }

    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getInstanceId() {
        return this.instanceId;
    }

    @Override
    public String getServiceId() {
        return this.serviceId;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public boolean isSecure() {
        return this.secure;
    }

    @Override
    public URI getUri() {
        return this.uri;
    }

    @Override
    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}

