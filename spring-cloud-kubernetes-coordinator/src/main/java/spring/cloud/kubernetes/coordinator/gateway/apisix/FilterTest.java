package spring.cloud.kubernetes.coordinator.gateway.apisix;

import io.github.api7.A6.HTTPReqCall.Req;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author wxl
 */
@Component
@ConditionalOnProperty(value = "filter.enabled", havingValue = "true", matchIfMissing = false)
public class FilterTest implements CommandLineRunner {

    @Autowired
    private KubernetesServiceChooseFilter kubernetesServiceChooseFilter;

    @Override
    public void run(String... args) throws Exception {
        kubernetesServiceChooseFilter.filter(new HttpRequest(new Req()), new HttpResponse(10), null);
    }
}
