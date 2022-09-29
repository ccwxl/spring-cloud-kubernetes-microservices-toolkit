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

    @Bean
    public LoadbalancerRestTemplateInterceptor loadbalancerRestTemplateInterceptor() {
        return new LoadbalancerRestTemplateInterceptor();
    }

    @Bean
    public LoadbalancerRestTemplateInterceptorAfterPropertiesSet loadbalancerRestTemplateInterceptorAfterPropertiesSet() {
        return new LoadbalancerRestTemplateInterceptorAfterPropertiesSet();
    }
}
