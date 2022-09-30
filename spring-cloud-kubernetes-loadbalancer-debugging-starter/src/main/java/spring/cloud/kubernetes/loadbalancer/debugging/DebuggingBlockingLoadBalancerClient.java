package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.ProxyContextHolder;

import java.net.URI;

/**
 * @author wxl
 * 如果请求的代理. 需要重新url. 增加通用前缀
 */
public class DebuggingBlockingLoadBalancerClient extends BlockingLoadBalancerClient {

    public DebuggingBlockingLoadBalancerClient(ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerClientFactory) {
        super(loadBalancerClientFactory);
    }

    @Override
    public URI reconstructURI(ServiceInstance serviceInstance, URI original) {
        String podService = ProxyContextHolder.getRealPodService();
        if (StringUtils.hasLength(podService)) {
            //如果当前需要请求代理. 增加前缀 TODO




        }

        return super.reconstructURI(serviceInstance, original);
    }
}
