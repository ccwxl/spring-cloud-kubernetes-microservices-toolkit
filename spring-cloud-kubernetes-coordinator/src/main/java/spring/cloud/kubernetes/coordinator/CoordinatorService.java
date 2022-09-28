package spring.cloud.kubernetes.coordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

/**
 * @author wxl
 * 适配
 */
@SpringBootApplication(scanBasePackages = {"spring.cloud.kubernetes.coordinator", "org.apache.apisix.plugin.runner"})
public class CoordinatorService {

    @Bean
    public ScheduledAnnotationBeanPostProcessor processor() {

        return new ScheduledAnnotationBeanPostProcessor();
    }

    @Bean(destroyMethod = "stop", initMethod = "start")
    public Health health() {
        return new Health();
    }

    public static void main(String[] args) {

        SpringApplication.run(CoordinatorService.class, args);
    }
}
