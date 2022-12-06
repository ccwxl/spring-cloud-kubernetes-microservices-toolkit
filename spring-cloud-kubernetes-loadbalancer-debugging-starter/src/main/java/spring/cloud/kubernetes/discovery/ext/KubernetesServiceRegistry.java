package spring.cloud.kubernetes.discovery.ext;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.ServiceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/**
 * @author wxl
 * k8s external service register.
 */
public class KubernetesServiceRegistry implements ServiceRegistry<KubernetesRegistration> {

    private static final Logger LOG = LoggerFactory.getLogger(KubernetesServiceRegistry.class);

    private final KubernetesClient client;

    private final InetUtils inetUtils;

    public KubernetesServiceRegistry(KubernetesClient client, InetUtils inetUtils) {
        this.client = client;
        this.inetUtils = inetUtils;
    }

    @Override
    public void register(KubernetesRegistration registration) {
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
                            if (ea.getIp().equals(getHost(registration))) {
                                isExist = true;
                                break;
                            }
                        }
                    }
                    //如果不存在则不去patch
                    if (!isExist) {
                        EndpointAddress endpointAddress = new EndpointAddressBuilder().withIp(getHost(registration)).build();
                        EndpointPort endpointPort = new EndpointPortBuilder().withPort(registration.getPort()).withName("local-server").build();
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

        Resource<Endpoints> endpointsResource = client.endpoints()
                .inNamespace(registration.getNamespace())
                .withName(registration.getServiceId());

        Endpoints originEndpoint = endpointsResource.get();

        Endpoints endpoints = new EndpointsBuilder(originEndpoint)
                .removeMatchingFromSubsets(builder ->
                        builder.hasMatchingAddress(a -> a.getIp().equals(getHost(registration)))
                                && builder.hasMatchingPort(v -> v.getPort().equals(registration.getPort())))
                .build();

        endpointsResource.patch(endpoints);

        LOG.info("De-registering Endpoint patch: {}", endpoints.getSubsets());
    }

    @Override
    public void close() {

    }

    @Override
    public void setStatus(KubernetesRegistration registration, String status) {
        //noting to do
    }

    @Override
    public List<EndpointSubset> getStatus(KubernetesRegistration registration) {
        Resource<Endpoints> endpointsResource = client.endpoints()
                .inNamespace(registration.getNamespace())
                .withName(registration.getServiceId());
        Endpoints originEndpoint = endpointsResource.get();
        return originEndpoint.getSubsets();
    }

    private Endpoints create(KubernetesRegistration registration) {
        EndpointAddress endpointAddress = new EndpointAddressBuilder().withIp(getHost(registration)).build();
        EndpointPort endpointPort = new EndpointPortBuilder().withPort(registration.getPort()).build();
        EndpointSubset endpointSubset = new EndpointSubsetBuilder().addToAddresses(endpointAddress).addToPorts(endpointPort).build();
        ObjectMeta metadata = new ObjectMetaBuilder()
                .withName(registration.getServiceId())
                .withNamespace(registration.getNamespace())
                .build();
        return new EndpointsBuilder().withSubsets(endpointSubset).withMetadata(metadata).build();
    }

    private String getHost(KubernetesRegistration registration) {
        if (StringUtils.hasText(registration.getHost())) {
            return registration.getHost();
        }
        InetAddress inetAddress = inetUtils.findFirstNonLoopbackAddress();
        return inetAddress.getHostAddress();
    }
}