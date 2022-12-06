package spring.cloud.kubernetes.example.organization;

import io.fabric8.kubernetes.api.model.Endpoints;
import io.fabric8.kubernetes.api.model.EndpointsList;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wxl
 */
@SpringBootApplication
@RegisterReflectionForBinding(value = {Service.class, IntOrString.Deserializer.class, Endpoints.class, EndpointsList.class})
public class OrganizationService {


    public static void main(String[] args) {

        SpringApplication.run(OrganizationService.class, args);
    }
}
