package spring.cloud.kubernetes.coordinator.service.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.kubernetes.fabric8.discovery.KubernetesDiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wxl
 * 每10秒扫描下所有service的endpoint. 把已经不健康的endpoint(pod)清理掉.
 */
@Component
public class ServiceHealthCheck {

    @Autowired
    KubernetesDiscoveryClient discoveryClient;

    @Scheduled(fixedRate = 3, initialDelay = 15, timeUnit = TimeUnit.SECONDS)
    public void serviceEndpointCheck() {

    }
}
