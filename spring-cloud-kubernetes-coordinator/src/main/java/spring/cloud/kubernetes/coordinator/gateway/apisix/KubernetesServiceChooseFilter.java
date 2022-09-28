package spring.cloud.kubernetes.coordinator.gateway.apisix;

import lombok.extern.slf4j.Slf4j;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.PostRequest;
import org.apache.apisix.plugin.runner.PostResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilter;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.springframework.stereotype.Component;

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
        response.setHeader("KubernetesServiceChooseFilter", "true");
        String sourceIp = request.getSourceIP();
        log.info("Req client Ip: [{}]", sourceIp);
        chain.filter(request, response);
    }
}
