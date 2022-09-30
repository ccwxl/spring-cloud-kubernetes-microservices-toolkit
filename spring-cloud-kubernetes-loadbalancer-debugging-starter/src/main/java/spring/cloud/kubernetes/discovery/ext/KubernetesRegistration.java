package spring.cloud.kubernetes.discovery.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesServiceInstance;

/**
 * @author wxl
 * KubernetesServiceInstance
 */
@ConfigurationProperties("spring.cloud.kubernetes.discovery")
public class KubernetesRegistration extends KubernetesServiceInstance implements Registration {

}

