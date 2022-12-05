package spring.cloud.kubernetes.example.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import spring.cloud.kubernetes.example.uaa.facode.OrganizationClient;
import spring.cloud.kubernetes.example.uaa.facode.OrganizationFeignClient;

/**
 * @author wxl
 */
@EnableFeignClients(clients = OrganizationFeignClient.class)
@SpringBootApplication
public class UaaService {

    public static void main(String[] args) {

        SpringApplication.run(UaaService.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient webClient(WebClient.Builder builder) {

        return builder.build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) {

        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
    }

    @Bean
    public OrganizationClient uaaClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(OrganizationClient.class);
    }
}
