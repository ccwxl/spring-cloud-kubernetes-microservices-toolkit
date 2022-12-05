package spring.cloud.kubernetes.example.account.facade;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.net.URI;

/**
 * @author wxl
 */
@HttpExchange(value = "uaa")
public interface UaaClient {

    /**
     * 请求认证
     *
     * @param uri        请求url
     * @param identifier 身份标识
     * @return ok
     */
    @GetExchange("/passport/{identifier}")
    String passport(URI uri, @PathVariable("identifier") String identifier);

}
