package spring.cloud.kubernetes.discovery.ext;

import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.kubernetes.commons.PodUtils;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxl
 */
@Configuration
@ConditionalOnProperty(name = "spring.cloud.kubernetes.discovery.register", havingValue = "true")
@EnableConfigurationProperties(KubernetesRegistration.class)
@AutoConfigureAfter({AutoServiceRegistrationConfiguration.class, KubernetesServiceRegistryAutoConfiguration.class})
public class KubernetesAutoServiceRegistrationAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public KubernetesAutoServiceRegistration autoServiceRegistration(
            @Qualifier("serviceRegistry") KubernetesServiceRegistry registry,
            AutoServiceRegistrationProperties autoServiceRegistrationProperties,
            KubernetesDiscoveryProperties properties,
            KubernetesRegistration registration, PodUtils<Pod> podUtils) {
        return new KubernetesAutoServiceRegistration(registry,
                autoServiceRegistrationProperties, registration, properties, podUtils);
    }

    @Bean
    public KubernetesAutoServiceRegistrationListener listener(KubernetesAutoServiceRegistration registration) {
        return new KubernetesAutoServiceRegistrationListener(registration);
    }

}
