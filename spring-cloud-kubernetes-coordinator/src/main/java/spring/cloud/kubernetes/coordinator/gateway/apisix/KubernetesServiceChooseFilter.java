package spring.cloud.kubernetes.coordinator.gateway.apisix;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilter;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
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

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    public String name() {

        return "KubernetesServiceChooseFilter";
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, PluginFilterChain chain) {
        if (loadBalancerClient instanceof BlockingLoadBalancerClient blockingLoadBalancerClient) {
            doServiceChoose(blockingLoadBalancerClient, request, response);
            chain.filter(request, response);
        } else {
            chain.filter(request, response);
        }
    }

    private void doServiceChoose(BlockingLoadBalancerClient blockingLoadBalancerClient, HttpRequest request, HttpResponse response) {
        long startTime = System.currentTimeMillis();
        PluginConfig pluginConfig = getPluginConfig(request);
        if (pluginConfig == null) {
            return;
        }
        String sourceIp = request.getSourceIP();
        log.info("Req client Ip: [{}]", sourceIp);
        LoadbalancerContextHolder.setLoadbalancerIp(sourceIp);
        ServiceInstance serviceInstance = blockingLoadBalancerClient.choose(pluginConfig.getService());
        log.info("Config is: [{}]", serviceInstance);
        response.setHeader("Kubernetes-Service-Choose-Filter-Cost-Ms", System.currentTimeMillis() - startTime + "ms");
        response.setHeader("Kubernetes-Service-Choose-Service", serviceInstance.toString());
    }

    @Nullable
    private PluginConfig getPluginConfig(HttpRequest request) {
        String config = request.getConfig(this);
        PluginConfig pluginConfig = null;
        try {
            pluginConfig = objectMapper.readValue(config, PluginConfig.class);
        } catch (Exception e) {
            log.error("Serialization configuration failed.", e);
        }
        return pluginConfig;
    }
}
