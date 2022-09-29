package spring.cloud.kubernetes.loadbalancer;

import org.springframework.core.NamedInheritableThreadLocal;

/**
 * @author wxl
 * 负载均衡的上下文
 */
public class LoadbalancerContextHolder {
    private static final ThreadLocal<String> LOADBALANCER_CONTEXT_HOLDER =
            new NamedInheritableThreadLocal<>("LoadbalancerContex");

    public static String getLoadbalancerIp() {

        return LOADBALANCER_CONTEXT_HOLDER.get();
    }

    public static void setLoadbalancerIp(String loadbalancerIp) {

        LOADBALANCER_CONTEXT_HOLDER.set(loadbalancerIp);
    }

    public static void resetLocaleContext() {

        LOADBALANCER_CONTEXT_HOLDER.remove();
    }
}
