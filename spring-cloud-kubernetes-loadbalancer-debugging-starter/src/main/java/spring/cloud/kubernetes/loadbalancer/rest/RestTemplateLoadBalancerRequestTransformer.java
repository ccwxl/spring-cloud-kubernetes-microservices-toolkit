package spring.cloud.kubernetes.loadbalancer.rest;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestTransformer;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;

/**
 * @author apple
 */
public class RestTemplateLoadBalancerRequestTransformer implements LoadBalancerRequestTransformer {

    @Override
    public HttpRequest transformRequest(HttpRequest request, ServiceInstance instance) {
        if (instance == null) {
            return request;
        }
        String proxyService = instance.getMetadata().get(Cons.K8S_PROXY_SERVICE);
        if (StringUtils.hasText(proxyService)) {
            request.getHeaders().add(Cons.LB_IP_PORT, instance.getMetadata().get(Cons.K8S_PROXY_SERVICE));
        }

        return request;
    }
}
