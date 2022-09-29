package spring.cloud.kubernetes.example.account.rest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.kubernetes.example.account.facade.UaaFeignClient;

/**
 * @author wxl
 */
@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class LoginController {

    private final UaaFeignClient uaaFeignClient;

    @GetMapping("/login")
    public String login() {
        uaaFeignClient.passport("aa");
        return "ok";
    }
}
