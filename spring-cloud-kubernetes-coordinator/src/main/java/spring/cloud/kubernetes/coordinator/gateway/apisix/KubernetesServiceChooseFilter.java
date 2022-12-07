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
import org.springframework.cloud.client.loadbalancer.DefaultRequestContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycleValidator;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;

import java.util.Map;
import java.util.Set;

/**
 * @author wxl
 * 负载均衡.
 * 在filter中不能对Response 对象修改. 一旦修改就会完全使用插件的response.
 */
@Slf4j
@Component
public class KubernetesServiceChooseFilter implements PluginFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private LoadBalancerClientFactory loadBalancerClientFactory;

    @Override
    public String name() {

        return "KubernetesDiscoveryServiceChooseFilter";
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, PluginFilterChain chain) {
        PluginConfig pluginConfig = getPluginConfig(request);
        if (pluginConfig == null) {
            chain.filter(request, response);
            return;
        }
        //服务发现
        if (loadBalancerClient instanceof BlockingLoadBalancerClient blockingLoadBalancerClient) {
            doServiceChoose(pluginConfig, blockingLoadBalancerClient, request);
        }
        chain.filter(request, response);
    }

    private void doServiceChoose(PluginConfig pluginConfig, BlockingLoadBalancerClient blockingLoadBalancerClient, HttpRequest request) {
        String sourceIp = null;
        if (StringUtils.hasText(pluginConfig.getRealIp())) {
            sourceIp = request.getHeader(pluginConfig.getRealIp().toLowerCase());
        }

        if (!StringUtils.hasLength(sourceIp)) {
            sourceIp = request.getSourceIP();
        }

        LoadbalancerContextHolder.setLoadbalancerIp(sourceIp);
        Set<LoadBalancerLifecycle> supportedLifecycleProcessors = getSupportedLifecycleProcessors(pluginConfig.getService());
        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(ReactiveLoadBalancer.REQUEST));
        ServiceInstance serviceInstance = blockingLoadBalancerClient.choose(pluginConfig.getService(), ReactiveLoadBalancer.REQUEST);
        log.info("gateway choose service is : [{}] sourceIp: [{}]", serviceInstance.getHost() + ":" + serviceInstance.getPort(), sourceIp);
        request.setHeader(Cons.LB_IP, sourceIp);
        request.setHeader(Cons.LB_IP_PORT, serviceInstance.getHost() + ":" + serviceInstance.getPort());
        LoadbalancerContextHolder.resetLocaleContext();
    }

    @Nullable
    private PluginConfig getPluginConfig(HttpRequest request) {
        String config = request.getConfig(this);
        if (!StringUtils.hasLength(config)) {
            return null;
        }
        PluginConfig pluginConfig = null;
        try {
            pluginConfig = objectMapper.readValue(config, PluginConfig.class);
        } catch (Exception e) {
            log.error("Serialization configuration failed.", e);
        }
        return pluginConfig;
    }

    private Set<LoadBalancerLifecycle> getSupportedLifecycleProcessors(String serviceId) {
        return LoadBalancerLifecycleValidator.getSupportedLifecycleProcessors(
                loadBalancerClientFactory.getInstances(serviceId, LoadBalancerLifecycle.class),
                DefaultRequestContext.class, Object.class, ServiceInstance.class);
    }
}
