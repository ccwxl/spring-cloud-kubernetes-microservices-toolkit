package spring.cloud.kubernetes.loadbalancer.debugging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wxl
 */
@Slf4j
public class NetSegmentServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

    private final InetUtils inetUtils;

    public NetSegmentServiceInstanceListSupplier(ServiceInstanceListSupplier delegate, ObjectProvider<InetUtils> inetUtils) {
        super(delegate);
        this.inetUtils = inetUtils.getIfAvailable(() -> new InetUtils(new InetUtilsProperties()));
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return getDelegate().get().map(this::filterByNetSegment);
    }

    private List<ServiceInstance> filterByNetSegment(List<ServiceInstance> instances) {
        InetAddress host = null;
        String loadbalancerIp = LoadbalancerContextHolder.getLoadbalancerIp();
        log.info("NetSegmentServiceInstanceListSupplier Ip :[{}]", loadbalancerIp);
        try {
            if (StringUtils.hasText(loadbalancerIp)) {
                host = InetAddress.getByName(loadbalancerIp);
            } else {
                host = inetUtils.findFirstNonLoopbackAddress();
            }
        } catch (Exception e) {
            log.warn("InetAddress.getByName form [{}] failed.", loadbalancerIp);
        }
        if (host == null) {
            return instances;
        }
        String resourceIp = host.getHostAddress();
        List<ServiceInstance> targetList = new ArrayList<>();
        List<ServiceInstance> publicPodService = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            if (IPV4Util.isSameAddress(resourceIp, instance.getHost())) {
                //排除不健康的instance
                targetList.add(instance);
            }
            //过滤出公共服务
            if (isPublicPodService(instance)) {
                publicPodService.add(instance);
            }
        }

        if (CollectionUtils.isEmpty(targetList)) {
            //如果本地没有任何服务. 那么只去请求k8s的pod. 不要去请求其他开发者的本地服务. 以免出现混乱. 将 instance public-service=true 的过滤出来. 仅适用于k8s内pod的路由方式
            log.info("Selected Pub services are: [{}]", publicPodService);
            return publicPodService;
        } else {
            log.info("Selected services are: [{}]", targetList);
            return targetList;
        }
    }

    private boolean isPublicPodService(ServiceInstance instance) {
        String pubSvc = instance.getMetadata().getOrDefault("public-service", "true");
        return Boolean.parseBoolean(pubSvc);
    }
}
