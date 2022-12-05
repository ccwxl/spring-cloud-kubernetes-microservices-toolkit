package spring.cloud.kubernetes.loadbalancer.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wxl
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestTemplate.class)
public class LoadbalancerRestTemplateAutoConfiguration {

    /**
     * 要放在 org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor 的后面执行
     *
     * @return LoadbalancerRestTemplateInterceptor
     */
    @Bean
    public RestTemplateLoadBalancerRequestTransformer restTemplateLoadBalancerRequestTransformer() {

        return new RestTemplateLoadBalancerRequestTransformer();
    }
}
