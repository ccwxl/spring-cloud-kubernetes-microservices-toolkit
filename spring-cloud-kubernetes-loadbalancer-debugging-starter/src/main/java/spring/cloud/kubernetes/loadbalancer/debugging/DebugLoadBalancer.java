package spring.cloud.kubernetes.loadbalancer.debugging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

/**
 * @author wxl
 */
public class DebugLoadBalancer extends RoundRobinLoadBalancer {

    private static final Logger log = LoggerFactory.getLogger(DebugLoadBalancer.class);

    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final String serviceId;

    private final InetUtils.HostInfo hostInfo;

    public DebugLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, InetUtils inetUtils) {
        super(serviceInstanceListSupplierProvider, serviceId);
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        if (inetUtils == null) {
            inetUtils = new InetUtils(new InetUtilsProperties());
        }
        this.hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
    }

    //实现


}
