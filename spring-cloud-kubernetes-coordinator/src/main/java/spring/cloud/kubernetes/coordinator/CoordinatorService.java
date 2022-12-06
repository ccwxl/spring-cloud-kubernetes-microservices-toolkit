package spring.cloud.kubernetes.coordinator;

import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wxl
 * 适配
 */
@RegisterReflectionForBinding(value = {Service.class, IntOrString.Deserializer.class, Endpoints.class, EndpointsList.class})
@SpringBootApplication(scanBasePackages = {"spring.cloud.kubernetes.coordinator", "org.apache.apisix.plugin.runner"})
public class CoordinatorService {

    public static void main(String[] args) {

        SpringApplication.run(CoordinatorService.class, args);
    }
}
