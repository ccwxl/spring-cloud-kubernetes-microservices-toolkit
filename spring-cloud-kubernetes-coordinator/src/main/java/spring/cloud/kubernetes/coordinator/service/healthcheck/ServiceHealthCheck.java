package spring.cloud.kubernetes.coordinator.service.healthcheck;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.kubernetes.fabric8.discovery.KubernetesDiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wxl
 * 每10秒扫描下所有service的endpoint. 把已经不健康的endpoint(pod)清理掉.
 * <a href="https://www.annhe.net/article-4422.html">...</a>
 */
@Component
public class ServiceHealthCheck {

    @Autowired
    KubernetesDiscoveryClient discoveryClient;

    @Autowired
    private KubernetesClient client;

    public void serviceEndpointCheck() throws InterruptedException {
        while (isRunning()) {
            List<String> services =
                    discoveryClient.getServices();
            //get service endpoint

            //check endpoint address

            //check heahcheck

            Thread.sleep(1000);
        }
    }

    private boolean isRunning() {

        return false;
    }
}
