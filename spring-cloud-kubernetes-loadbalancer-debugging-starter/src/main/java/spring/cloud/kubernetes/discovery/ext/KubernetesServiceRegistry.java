package spring.cloud.kubernetes.discovery.ext;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryProperties;

import java.util.Collections;
import java.util.List;

/**
 * @author wxl
 * k8s external service register.
 */
public class KubernetesServiceRegistry implements ServiceRegistry<KubernetesRegistration> {

    private static final Logger LOG = LoggerFactory.getLogger(KubernetesServiceRegistry.class);

    private final KubernetesClient client;

    private KubernetesDiscoveryProperties properties;

    public KubernetesServiceRegistry(KubernetesClient client, KubernetesDiscoveryProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @Override
    public void register(KubernetesRegistration registration) {
        LOG.info("Registering service with kubernetes: " + registration.getServiceId());
        Resource<Endpoints> resource = client.endpoints()
                .inNamespace(registration.getMetadata().get("namespace"))
                .withName(registration.getMetadata().get("name"));
        Endpoints endpoints = resource.get();
        if (endpoints == null) {
            Service service = new ServiceBuilder()
                    .withNewMetadata()
                    .withName(registration.getMetadata().get("name"))
                    .withLabels(Collections.singletonMap("app", registration.getMetadata().get("name")))
                    .endMetadata()
                    .withNewSpec()
                    .withSelector(Collections.singletonMap("app", registration.getMetadata().get("name")))
                    .addNewPort()
                    .withName("tcp-port")
                    .withProtocol("TCP")
                    .withPort(registration.getPort())
                    .withTargetPort(new IntOrString(registration.getPort()))
                    .endPort()
                    .withType("LoadBalancer")
                    .endSpec()
                    .build();
            service = client.services().create(service);
            LOG.info("New service: {}", service);
            Endpoints endpoints1 = create(registration);
            Endpoints e = client.endpoints()
                    .inNamespace(registration.getMetadata().get("namespace"))
                    .patch(endpoints1);
            LOG.info("New endpoint: {}", e);
        } else {
            try {
                boolean isExist = false;
                Endpoints endpoints1 = resource.get();
                List<EndpointSubset> subsets = endpoints1.getSubsets();
                for (EndpointSubset es : subsets) {
                    List<EndpointAddress> addresses = es.getAddresses();
                    for (EndpointAddress ea : addresses) {
                        if (ea.getIp().equals(registration.getHost())) {
                            isExist = true;
                            break;
                        }
                    }
                }

                if (!isExist) {
                    //如果存在则不去patch
                    EndpointAddress endpointAddress = new EndpointAddressBuilder().withIp(registration.getHost()).build();
                    EndpointPort endpointPort = new EndpointPortBuilder().withPort(registration.getPort()).build();
                    EndpointSubset endpointSubset = new EndpointSubsetBuilder().addToAddresses(endpointAddress).addToPorts(endpointPort).build();
                    endpoints.getSubsets().add(endpointSubset);
                    Endpoints patch = resource.patch(endpoints);
                    LOG.info("Endpoint updated: {}", patch);
                }
            } catch (RuntimeException e) {
                LOG.warn("Endpoint updated failed:", e);
            }
        }
    }

    @Override
    public void deregister(KubernetesRegistration registration) {
        LOG.info("De-registering service with kubernetes: " + registration.getInstanceId());
        Resource<Endpoints> resource = client.endpoints()
                .inNamespace(registration.getMetadata().get("namespace"))
                .withName(registration.getMetadata().get("name"));

//        EndpointAddress address = new EndpointAddressBuilder().withIp(registration.getHost()).build();
//
//        Endpoints updatedEndpoints = resource.edit()
//                .editMatchingSubset(builder -> builder.hasMatchingPort(v -> v.getPort().equals(registration.getPort())))
//                .removeFromAddresses(address)
//                .endSubset()
//                .done();
//        LOG.info("Endpoint updated: {}", updatedEndpoints);
//
//        resource.get().getSubsets().stream()
//                .filter(subset -> subset.getAddresses().size() == 0)
//                .forEach(subset -> resource.edit()
//                        .removeFromSubsets(subset)
//                        .done());
    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(KubernetesRegistration registration, String status) {

    }

    @Override
    public <T> T getStatus(KubernetesRegistration registration) {
        return null;
    }

    private Endpoints create(KubernetesRegistration registration) {
        EndpointAddress endpointAddress = new EndpointAddressBuilder().withIp(registration.getHost()).build();
        EndpointPort endpointPort = new EndpointPortBuilder().withPort(registration.getPort()).build();
        EndpointSubset endpointSubset = new EndpointSubsetBuilder().addToAddresses(endpointAddress).addToPorts(endpointPort).build();
        ObjectMeta metadata = new ObjectMetaBuilder()
                .withName(registration.getMetadata().get("name"))
                .withNamespace(registration.getMetadata().get("namespace"))
                .build();
        return new EndpointsBuilder().withSubsets(endpointSubset).withMetadata(metadata).build();
    }

}