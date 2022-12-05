package spring.cloud.kubernetes.loadbalancer.webclient;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerClientRequestTransformer;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import spring.cloud.kubernetes.loadbalancer.Cons;

/**
 * @author apple
 */
public class WebclientLoadBalancerClientRequestTransformer implements LoadBalancerClientRequestTransformer {

    @Override
    public ClientRequest transformRequest(ClientRequest request, ServiceInstance instance) {
        if (instance == null) {
            return request;
        }
        String proxyService = instance.getMetadata().get(Cons.K8S_PROXY_SERVICE);
        if (StringUtils.hasText(proxyService)) {
            request.headers().add(Cons.LB_IP_PORT, instance.getMetadata().get(Cons.K8S_PROXY_SERVICE));
        }
        return request;
    }
}
