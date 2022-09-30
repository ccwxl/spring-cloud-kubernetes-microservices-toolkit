package spring.cloud.kubernetes.discovery.ext;


import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.kubernetes.commons.KubernetesClientProperties;
import org.springframework.cloud.kubernetes.commons.PodUtils;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryProperties;
import org.springframework.core.env.Environment;

/**
 * @author wxl
 */
public class KubernetesAutoServiceRegistration extends AbstractAutoServiceRegistration<KubernetesRegistration> {
    private final KubernetesDiscoveryProperties properties;
    private final KubernetesRegistration registration;
    private final PodUtils<Pod> podUtils;

    KubernetesAutoServiceRegistration(ServiceRegistry<KubernetesRegistration> serviceRegistry,
                                      AutoServiceRegistrationProperties autoServiceRegistrationProperties,
                                      KubernetesRegistration registration, KubernetesDiscoveryProperties properties,
                                      PodUtils<Pod> podUtils) {
        super(serviceRegistry, autoServiceRegistrationProperties);
        this.properties = properties;
        this.registration = registration;
        this.podUtils = podUtils;
    }

    @Override
    protected Object getConfiguration() {
        return properties;
    }

    @Override
    protected boolean isEnabled() {
        return !podUtils.isInsideKubernetes();
    }

    @Override
    protected KubernetesRegistration getRegistration() {
        return registration;
    }

    @Override
    protected KubernetesRegistration getManagementRegistration() {
        return registration;
    }

}

