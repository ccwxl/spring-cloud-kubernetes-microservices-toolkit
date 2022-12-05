package spring.cloud.kubernetes.loadbalancer.feign;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxl
 */
@Configuration
@ConditionalOnClass(Feign.class)
public class LoadbalancerFeignAutoConfiguration {
    @Bean
    public DebugFeignHeadersTransformer debugHeadersTransformer() {

        return new DebugFeignHeadersTransformer();
    }
}
