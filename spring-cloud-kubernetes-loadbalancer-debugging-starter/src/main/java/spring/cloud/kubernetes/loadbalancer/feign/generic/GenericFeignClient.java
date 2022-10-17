package spring.cloud.kubernetes.loadbalancer.feign.generic;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wxl
 */
public class GenericFeignClient {

    @Autowired
    private GenericFeignClientFactory<GenericFeignService> dynamicFeignClientFactory;

    public Object executePostApi(String feignName, String url, Object params) {
        GenericFeignService dynamicService = dynamicFeignClientFactory.getFeignClient(GenericFeignService.class, feignName);
        return dynamicService.executePostApi(url, params);
    }

    public Object executeGetApi(String feignName, String url, Object params) {
        GenericFeignService dynamicService = dynamicFeignClientFactory.getFeignClient(GenericFeignService.class, feignName);
        return dynamicService.executeGetApi(url, params);
    }

}
