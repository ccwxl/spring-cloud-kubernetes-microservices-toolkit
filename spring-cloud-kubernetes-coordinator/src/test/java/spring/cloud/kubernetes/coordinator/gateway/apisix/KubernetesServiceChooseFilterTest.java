package spring.cloud.kubernetes.coordinator.gateway.apisix;

import io.github.api7.A6.HTTPReqCall.Req;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ReflectionUtils;
import spring.cloud.kubernetes.coordinator.CoordinatorService;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CoordinatorService.class)
public class KubernetesServiceChooseFilterTest {

    @Autowired
    private KubernetesServiceChooseFilter kubernetesServiceChooseFilter;

    @Test
    public void filter() throws NoSuchFieldException {
        HttpResponse httpResponse = new HttpResponse(1);
        HttpRequest httpRequest = new HttpRequest(new Req());
        httpRequest.initCtx(httpResponse, Map.of(kubernetesServiceChooseFilter.name(), """
                {"namespace":"wxl-k8s-service","service":"account"}
                """));
        Field sourceIP = HttpRequest.class.getDeclaredField("sourceIP");
        ReflectionUtils.makeAccessible(sourceIP);
        ReflectionUtils.setField(sourceIP, httpRequest, "192.168.29.233");
        PluginFilterChain filterChain = new PluginFilterChain(Collections.singletonList(kubernetesServiceChooseFilter));
        filterChain.filter(httpRequest, httpResponse);
    }
}