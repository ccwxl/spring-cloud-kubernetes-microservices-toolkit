package spring.cloud.kubernetes.loadbalancer.debugging;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
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
public class LoadBalanceAutoConfiguration {

}

