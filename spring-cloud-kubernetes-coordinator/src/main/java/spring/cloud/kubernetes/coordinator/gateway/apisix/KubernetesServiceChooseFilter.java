package spring.cloud.kubernetes.coordinator.gateway.apisix;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilter;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;

/**
 * @author wxl
 * 负载均衡.
 */
@Slf4j
@Component
public class KubernetesServiceChooseFilter implements PluginFilter {


    @Autowired
    private ObjectMapper objectMapper;

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
        PluginConfig pluginConfig = null;
        try {
            pluginConfig = objectMapper.readValue(config, PluginConfig.class);
        } catch (Exception e) {
            log.error("Serialization configuration failed.", e);
        }
        if (pluginConfig==null){
            chain.filter(request, response);
        }
        log.info("Config is: [{}]", pluginConfig);
        response.setHeader("Kubernetes-Service-Choose-Filter-Cost-Ms", System.currentTimeMillis() - startTime + "ms");
        chain.filter(request, response);
    }
}
