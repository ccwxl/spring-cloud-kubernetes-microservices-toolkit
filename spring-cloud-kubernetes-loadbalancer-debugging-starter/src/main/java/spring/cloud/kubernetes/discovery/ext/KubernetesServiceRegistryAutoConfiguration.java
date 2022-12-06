package spring.cloud.kubernetes.discovery.ext;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxl
 * k8s external service register service.
 */
@Configuration
@ConditionalOnProperty(name = "spring.cloud.kubernetes.discovery.register.enabled", havingValue = "true")
@AutoConfigureBefore(ServiceRegistryAutoConfiguration.class)
public class KubernetesServiceRegistryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KubernetesServiceRegistry serviceRegistry(KubernetesClient client, InetUtils inetUtils) {
        return new KubernetesServiceRegistry(client, inetUtils);
    }
}
