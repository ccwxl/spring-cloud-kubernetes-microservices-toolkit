package spring.cloud.kubernetes.loadbalancer.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;
import spring.cloud.kubernetes.loadbalancer.ProxyContextHolder;

import java.io.IOException;

/**
 * @author wxl
 */
public class LoadbalancerRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);
        String loadbalancerIp = LoadbalancerContextHolder.getLoadbalancerIp();
        if (StringUtils.hasLength(loadbalancerIp)) {
            requestWrapper.getHeaders().add(Cons.LB_IP, loadbalancerIp);
        }

        //对代理的支持.写请求头
        String podService = ProxyContextHolder.getRealPodService();
        if (StringUtils.hasLength(podService)) {
            requestWrapper.getHeaders().add(Cons.LB_IP_PORT, podService);
        }

        return clientHttpRequestExecution.execute(requestWrapper, bytes);
    }

}