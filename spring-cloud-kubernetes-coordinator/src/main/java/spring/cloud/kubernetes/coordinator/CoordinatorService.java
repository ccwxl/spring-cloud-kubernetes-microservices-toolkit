package spring.cloud.kubernetes.coordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wxl
 * 适配
 */
@SpringBootApplication(scanBasePackages = {"spring.cloud.kubernetes.coordinator", "org.apache.apisix.plugin.runner"})
public class CoordinatorService {

    public static void main(String[] args) {

        SpringApplication.run(CoordinatorService.class, args);
    }
}
