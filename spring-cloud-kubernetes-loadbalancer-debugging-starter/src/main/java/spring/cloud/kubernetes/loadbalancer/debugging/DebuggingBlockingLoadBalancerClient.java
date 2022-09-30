package spring.cloud.kubernetes.loadbalancer.debugging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import spring.cloud.kubernetes.loadbalancer.ProxyContextHolder;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wxl
 * 如果请求的代理. 需要重新url. 增加通用前缀
 */
@Slf4j
public class DebuggingBlockingLoadBalancerClient extends BlockingLoadBalancerClient {

    private final ProxyProperties proxyProperties;

    public DebuggingBlockingLoadBalancerClient(ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerClientFactory, ProxyProperties proxyProperties) {
        super(loadBalancerClientFactory);
        this.proxyProperties = proxyProperties;
    }

    @Override
    public URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        String podService = ProxyContextHolder.getRealPodService();
        if (StringUtils.hasLength(podService)) {
            //如果当前需要请求代理. 增加前缀 TODO
            URI uri = MyLoadBalancerUriTools.reconstructURI(serviceInstance, original, proxyProperties.getPrefix(), podService);
            return uri;
        }
        log.info("DebuggingBlockingLoadBalancerClient reconstructURI.");
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }
}
