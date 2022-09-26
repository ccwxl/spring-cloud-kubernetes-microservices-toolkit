package spring.cloud.kubernetes.example.account.facade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wxl
 */
@FeignClient(contextId = "account-uaa", name = "uaa")
public interface UaaFeignClient {

    @GetMapping("/uaa/passport/{identifier}")
    String passport(@PathVariable("identifier") String identifier);

}
