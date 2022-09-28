package spring.cloud.kubernetes.coordinator.gateway.apisix;

import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.PostRequest;
import org.apache.apisix.plugin.runner.PostResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilter;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.springframework.stereotype.Component;

/**
 * @author wxl
 */
@Component
public class KubernetesServiceChooseFilter implements PluginFilter {

    @Override
    public String name() {
        return "KubernetesServiceChooseFilter";
    }


    @Override
    public void filter(HttpRequest request, HttpResponse response, PluginFilterChain chain) {
        System.out.println("进入了插件!");
        request.setHeader("KubernetesServiceChooseFilter", "true");
        chain.filter(request, response);
    }
}
