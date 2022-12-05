package spring.cloud.kubernetes.example.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import spring.cloud.kubernetes.example.account.facade.UaaClient;
import spring.cloud.kubernetes.example.account.facade.UaaFeignClient;

/**
 * @author wxl
 */
@EnableFeignClients(clients = UaaFeignClient.class)
@SpringBootApplication
public class AccountService {

    public static void main(String[] args) {

        SpringApplication.run(AccountService.class, args);
    }

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {

        return webClientBuilder.build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) {
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
    }

    @Bean
    public UaaClient uaaClient(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(UaaClient.class);
    }
}
