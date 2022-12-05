package spring.cloud.kubernetes.example.uaa.facode;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wxl
 */
@HttpExchange(value = "organization")
public interface OrganizationClient {

    @GetExchange("/organization/info/identifier/{identifier}")
    String organization(@PathVariable("identifier") String identifier);
}
