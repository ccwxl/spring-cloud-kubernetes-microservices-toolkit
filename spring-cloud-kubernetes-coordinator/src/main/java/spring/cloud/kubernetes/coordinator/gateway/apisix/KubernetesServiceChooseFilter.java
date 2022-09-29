package spring.cloud.kubernetes.coordinator.gateway.apisix;

import lombok.extern.slf4j.Slf4j;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilter;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.springframework.stereotype.Component;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;

/**
 * @author wxl
 * 负载均衡.
 */
@Slf4j
@Component
public class KubernetesServiceChooseFilter implements PluginFilter {

    @Override
    public String name() {

        return "KubernetesServiceChooseFilter";
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, PluginFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String sourceIp = request.getSourceIP();
        log.info("Req client Ip: [{}]", sourceIp);
        LoadbalancerContextHolder.setLoadbalancerIp(sourceIp);
        String config = request.getConfig(this);
        log.info("Config is: [{}]", config);
        response.setHeader("KubernetesServiceChooseFilterConstMs", String.valueOf(System.currentTimeMillis() - startTime));
        chain.filter(request, response);
    }
}
