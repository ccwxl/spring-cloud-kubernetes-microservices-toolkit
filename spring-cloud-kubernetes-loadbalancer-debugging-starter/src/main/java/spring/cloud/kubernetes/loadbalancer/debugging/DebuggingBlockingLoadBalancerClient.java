package spring.cloud.kubernetes.loadbalancer.debugging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;

import java.net.URI;

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
        if (StringUtils.hasLength(serviceInstance.getMetadata().get(Cons.K8S_PROXY_SERVICE))) {
            //如果当前需要请求代理. 增加前缀
            URI uri = CustomLoadBalancerUriTools.reconstructURI(serviceInstance, original,
                    proxyProperties.getPrefix(), serviceInstance.getMetadata().get(Cons.K8S_PROXY_SERVICE));
            log.info("request proxy origin url: [{}] proxy url: [{}]", original.toASCIIString(), uri.toASCIIString());
            return uri;
        }
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }
}
