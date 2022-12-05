package spring.cloud.kubernetes.loadbalancer.webclient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author apple
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WebClient.class)
public class WebclientLoadBalancerClientRequestTransformerAutoConfiguration {

    @Bean
    public WebclientLoadBalancerClientRequestTransformer webclientLoadBalancerClientRequestTransformer() {
        return new WebclientLoadBalancerClientRequestTransformer();
    }
}
