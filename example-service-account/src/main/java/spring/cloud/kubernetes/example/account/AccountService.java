package spring.cloud.kubernetes.example.account;

import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import spring.cloud.kubernetes.example.account.facade.UaaClient;
import spring.cloud.kubernetes.example.account.facade.UaaFeignClient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wxl
 */
@EnableFeignClients(clients = UaaFeignClient.class)
@Configuration(proxyBeanMethods = false)
@SpringBootApplication
@ImportRuntimeHints(AccountService.AccountRuntimeHints.class)
@RegisterReflectionForBinding(value = {Service.class, IntOrString.Deserializer.class, Endpoints.class, EndpointsList.class})
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

    static class AccountRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            ReflectionHints ref = hints.reflection();
            try {
                for (Class<?> c : getK8sClasses()) {
                    ref.registerType(c, MemberCategory.values());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Collection<? extends Class<?>> getK8sClasses() {
            Set<Class<?>> classes = new HashSet<>();
            return classes;
        }
    }
}
