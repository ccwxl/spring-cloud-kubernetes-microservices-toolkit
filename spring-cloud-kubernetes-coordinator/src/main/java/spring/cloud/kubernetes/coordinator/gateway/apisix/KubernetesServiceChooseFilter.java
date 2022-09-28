package spring.cloud.kubernetes.coordinator.gateway.apisix;

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
    public void postFilter(PostRequest request, PostResponse response, PluginFilterChain chain) {
        //获取到请求者的ip

        //选一个合适的upstream

        //放入到header中 x-apisix-original-dst-host

        //识别当前是代理请求吗?
        request.getUpstreamHeaders().put("spring-cloud-kubernetes-coordinator", "true");
        chain.postFilter(request, response);
    }
}
