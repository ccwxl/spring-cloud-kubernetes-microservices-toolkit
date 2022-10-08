package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author wxl
 */
@Profile("test")
@ConditionalOnDiscoveryEnabled
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ProxyProperties.class)
@LoadBalancerClients(defaultConfiguration = LoadBalanceConfig.class)
@AutoConfigureBefore(BlockingLoadBalancerClientAutoConfiguration.class)
public class LoadBalanceAutoConfiguration {

    @Bean
    @ConditionalOnBean(LoadBalancerClientFactory.class)
    public LoadBalancerClient blockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory, ProxyProperties proxyProperties) {
        return new DebuggingBlockingLoadBalancerClient(loadBalancerClientFactory, proxyProperties);
    }
}

