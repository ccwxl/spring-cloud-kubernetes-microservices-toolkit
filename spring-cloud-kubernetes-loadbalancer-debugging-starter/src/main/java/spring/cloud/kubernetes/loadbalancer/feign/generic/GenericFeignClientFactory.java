package spring.cloud.kubernetes.loadbalancer.feign.generic;

import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author wxl
 */
@Component
public class GenericFeignClientFactory<T> {

    private FeignClientBuilder feignClientBuilder;

    public GenericFeignClientFactory(ApplicationContext appContext) {
        this.feignClientBuilder = new FeignClientBuilder(appContext);
    }

    public T getFeignClient(final Class<T> type, String serviceId) {
        return this.feignClientBuilder.forType(type, serviceId).build();
    }
}