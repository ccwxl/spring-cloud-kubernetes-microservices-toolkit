package spring.cloud.kubernetes.loadbalancer.debugging;

import io.fabric8.kubernetes.api.model.Pod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.kubernetes.commons.PodUtils;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesServiceInstance;
import org.springframework.cloud.loadbalancer.core.*;
import reactor.core.publisher.Mono;
import spring.cloud.kubernetes.discovery.ext.KubernetesRegistration;
import spring.cloud.kubernetes.loadbalancer.ProxyContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wxl
 */
public class NetSegmentLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Log log = LogFactory.getLog(RoundRobinLoadBalancer.class);

    final AtomicInteger position;

    final String serviceId;

    boolean register;


    PodUtils<Pod> podUtils;

    ProxyProperties proxyProperties;

    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public NetSegmentLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                  String serviceId, String register, PodUtils<Pod> podUtils, ProxyProperties proxyProperties) {
        this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000));
        this.register = Boolean.parseBoolean(register);
        this.podUtils = podUtils;
        this.proxyProperties = proxyProperties;

    }

    public NetSegmentLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                  String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }

        // Ignore the sign bit, this allows pos to loop sequentially from 0 to
        // Integer.MAX_VALUE
        int pos = this.position.incrementAndGet() & Integer.MAX_VALUE;

        ServiceInstance instance = instances.get(pos % instances.size());

        //TODO 如何判断. 根据当前注册的元数据判断. ok
        //如果是本地服务. 而且只能请求公共服务. 那么就要去请求代理服务. 因为无法直接请求pod.
        //要重写URL. 怎么办. 需要在每个框架中独立实现... 或者使用header?

        //判断标准
        // 1. 在 pod 外
        // 2. spring.cloud.kubernetes.discovery.register =true 说明是本地服务.
        // 3. instance 的元数据中包含 k8s-public-service=true. 这个时候需要请求代理服务
        if (!podUtils.isInsideKubernetes() && register && instance.getMetadata().containsKey("k8s-public-service")) {
            ProxyContextHolder.setRealPodService(instance.getHost() + ":" + instance.getPort());
            instance = getProxyInstance(instance);
        }
        return new DefaultResponse(instance);
    }

    private ServiceInstance getProxyInstance(ServiceInstance si) {

        return new KubernetesServiceInstance(si.getInstanceId(), si.getServiceId(), proxyProperties.getHost(), proxyProperties.getPort(), si.getMetadata(), si.isSecure());
    }
}
