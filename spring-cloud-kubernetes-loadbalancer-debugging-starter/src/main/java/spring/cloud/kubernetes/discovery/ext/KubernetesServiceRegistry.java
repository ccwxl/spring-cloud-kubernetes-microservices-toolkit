package spring.cloud.kubernetes.discovery.ext;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.ServiceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;

import java.util.Collections;
import java.util.List;

/**
 * @author wxl
 * k8s external service register.
 * TODO 注册到k8s的endpoint上. 或者redis中.
 */
public class KubernetesServiceRegistry implements ServiceRegistry<KubernetesRegistration> {

    private static final Logger LOG = LoggerFactory.getLogger(KubernetesServiceRegistry.class);

    private final KubernetesClient client;

    public KubernetesServiceRegistry(KubernetesClient client) {
        this.client = client;
    }

    @Override
    public void register(KubernetesRegistration registration) {
        //TODO 注册到不同的地方如redis.
        LOG.info("Registering service with kubernetes: " + registration.getServiceId());
        ServiceResource<Service> serviceResource = client.services().inNamespace(registration.getNamespace())
                .withName(registration.getServiceId());
        Service service = serviceResource.get();
        if (service == null) {
            //注册 service + endpoint
            Service registerService = new ServiceBuilder()
                    .withNewMetadata()
                    .withName(registration.getServiceId())
                    .withLabels(Collections.singletonMap("app", registration.getServiceId()))
                    .endMetadata()
                    .withNewSpec()
                    .withSelector(Collections.singletonMap("app", registration.getServiceId()))
                    .addNewPort()
                    .withName("local-rpc-port")
                    .withProtocol("TCP")
                    .withPort(registration.getPort())
                    .withTargetPort(new IntOrString(registration.getPort()))
                    .endPort()
                    .withType("ClusterIP")
                    .withClusterIP("None")
                    .endSpec()
                    .build();
            service = client.services().create(registerService);
            LOG.info("New service: {}", service);
            Endpoints endpoints = create(registration);
            Endpoints e = client.endpoints()
                    .inNamespace(registration.getNamespace())
                    .create(endpoints);
            LOG.info("New endpoint: {}", e);
        } else {
            Resource<Endpoints> resource = client.endpoints()
                    .inNamespace(registration.getNamespace())
                    .withName(registration.getServiceId());
            Endpoints endpoints = resource.get();
            if (endpoints == null) {
                //注册endpoint
                Endpoints endpoints1 = create(registration);
                Endpoints e = client.endpoints()
                        .inNamespace(registration.getNamespace())
                        .create(endpoints1);
                LOG.info("New endpoint: {}", e);
            } else {
                //更新endpoint
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
    }

    @Override
    public void deregister(KubernetesRegistration registration) {
        LOG.info("De-registering service with kubernetes: " + registration.getServiceId());
        Endpoints originEndpoint = client.endpoints()
                .inNamespace(registration.getNamespace())
                .withName(registration.getServiceId())
                .get();
        Endpoints endpoints = new EndpointsBuilder(originEndpoint)
                .removeMatchingFromSubsets(builder ->
                        builder.hasMatchingAddress(a -> a.getIp().equals(registration.getHost()))
                                && builder.hasMatchingPort(v -> v.getPort().equals(registration.getPort())))
                .build();
        Endpoints patchEndpoint = client.endpoints().inNamespace(registration.getNamespace()).create(endpoints);
        LOG.info("De-registering Endpoint patch: {}", patchEndpoint.getSubsets());
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
                .withName(registration.getServiceId())
                .withNamespace(registration.getNamespace())
                .build();
        return new EndpointsBuilder().withSubsets(endpointSubset).withMetadata(metadata).build();
    }

}