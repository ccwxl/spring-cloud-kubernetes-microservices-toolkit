package spring.cloud.kubernetes.example.uaa.facode;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wxl
 */
@FeignClient(contextId = "uaa-organization", name = "organization")
public interface OrganizationFeignClient {

    @GetMapping("/organization/info/identifier/{identifier}")
    String organization(@PathVariable("identifier") String identifier);
}
