package spring.cloud.kubernetes.loadbalancer.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import spring.cloud.kubernetes.loadbalancer.Cons;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wxl
 */
public class LoadbalancerHandlerInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory
            .getLogger(LoadbalancerHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String lbIp = request.getHeader(Cons.LB_IP);
        if (log.isDebugEnabled()) {
            log.debug("lbIp is :[{}]", lbIp);
        }
        if (StringUtils.isNotBlank(lbIp)) {
            LoadbalancerContextHolder.setLoadbalancerIp(lbIp);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception e) {
        if (StringUtils.isNotBlank(LoadbalancerContextHolder.getLoadbalancerIp())) {
            LoadbalancerContextHolder.resetLocaleContext();
        }
    }

}
