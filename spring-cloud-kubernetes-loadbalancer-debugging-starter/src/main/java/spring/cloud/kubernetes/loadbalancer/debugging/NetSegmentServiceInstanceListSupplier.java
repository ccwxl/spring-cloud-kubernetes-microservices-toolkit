package spring.cloud.kubernetes.loadbalancer.debugging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

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
        InetAddress host = inetUtils.findFirstNonLoopbackAddress();
        if (host == null) {
            return instances;
        }
        String resourceIp = host.getHostAddress();
        List<ServiceInstance> targetList = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            if (IPV4Util.isSameAddress(resourceIp, instance.getHost())) {
                targetList.add(instance);
            }
        }
        if (CollectionUtils.isEmpty(targetList)) {
            return instances;
        }
        return targetList;
    }
}
