package spring.cloud.kubernetes.coordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

/**
 * @author wxl
 * 适配
 */
@SpringBootApplication
public class CoordinatorService {

    @Bean
    public ScheduledAnnotationBeanPostProcessor processor() {

        return new ScheduledAnnotationBeanPostProcessor();
    }

    public static void main(String[] args) {

        SpringApplication.run(CoordinatorService.class, args);
    }
}
