package spring.cloud.kubernetes.loadbalancer.rest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author wxl
 */
public class LoadbalancerRestTemplateInterceptorAfterPropertiesSet implements InitializingBean {

    @Autowired(required = false)
    private Collection<RestTemplate> restTemplates;

    @Autowired
    private LoadbalancerRestTemplateInterceptor seataRestTemplateInterceptor;

    @Override
    public void afterPropertiesSet() {
        if (this.restTemplates != null) {
            for (RestTemplate restTemplate : restTemplates) {
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(
                        restTemplate.getInterceptors());
                interceptors.add(this.seataRestTemplateInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        }
    }
}
