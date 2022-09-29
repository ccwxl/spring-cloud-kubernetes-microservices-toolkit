package spring.cloud.kubernetes.loadbalancer.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;
import spring.cloud.kubernetes.loadbalancer.Cons;
import spring.cloud.kubernetes.loadbalancer.LoadbalancerContextHolder;

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
        return clientHttpRequestExecution.execute(requestWrapper, bytes);
    }

}