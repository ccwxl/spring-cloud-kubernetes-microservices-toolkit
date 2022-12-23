package spring.cloud.kubernetes.example.account.rest;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.kubernetes.example.account.facade.UaaClient;
import spring.cloud.kubernetes.example.account.facade.UaaFeignClient;

import java.net.URI;

/**
 * @author wxl
 */
@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@FieldNameConstants
public class LoginController {

    private final UaaFeignClient uaaFeignClient;

    private final UaaClient uaaClient;

    @GetMapping("/login")
    public String login() {
        uaaClient.passport(URI.create("uaa"), "aa");
        String aa = uaaFeignClient.passport("aa");
        return aa;
    }
}
