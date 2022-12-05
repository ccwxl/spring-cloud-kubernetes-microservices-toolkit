package spring.cloud.kubernetes.loadbalancer.feign;

import feign.Request;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author apple
 */
public class DebugFeignHeadersTransformer implements LoadBalancerFeignRequestTransformer {

    @Override
    public Request transformRequest(Request request, ServiceInstance instance) {
        if (instance == null) {
            return request;
        }
        String proxyService = instance.getMetadata().get(Cons.K8S_PROXY_SERVICE);
        if (StringUtils.hasText(proxyService)) {
            Map<String, Collection<String>> headers = new HashMap<>(request.headers());
            headers.put(Cons.LB_IP_PORT, Collections.singleton(instance.getMetadata().get(Cons.K8S_PROXY_SERVICE)));
            request = Request.create(request.httpMethod(), request.url(), headers, request.body(), request.charset(),
                    request.requestTemplate());
        }
        return request;
    }
}
