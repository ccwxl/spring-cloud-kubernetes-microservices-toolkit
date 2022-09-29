package spring.cloud.kubernetes.loadbalancer.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wxl
 */
@ConditionalOnWebApplication
@ConditionalOnClass(DispatcherServlet.class)
public class LoadbalancerHandlerInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoadbalancerHandlerInterceptor()).addPathPatterns("/**");
    }
}
