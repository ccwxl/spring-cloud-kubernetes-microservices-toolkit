package spring.cloud.kubernetes.loadbalancer.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;
import spring.cloud.kubernetes.loadbalancer.ProxyContextHolder;

/**
 * @author wxl
 */
public class LoadbalancerFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String loadbalancerIp = LoadbalancerContextHolder.getLoadbalancerIp();
        if (StringUtils.hasLength(loadbalancerIp)) {
            template.header(Cons.LB_IP, loadbalancerIp);
        }
    }
}