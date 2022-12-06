package spring.cloud.kubernetes.example.account;

import org.springframework.boot.SpringApplicationAotProcessor;


public class AccountServiceTest {

    public static void main(String[] args) throws Exception {
        args = new String[]{"spring.cloud.kubernetes.example.account.AccountService",
                "/Users/apple/opensource/spring-cloud-kubernetes-microservices-toolkit/example-service-account/build/generated/aotSources",
                "/Users/apple/opensource/spring-cloud-kubernetes-microservices-toolkit/example-service-account/build/generated/aotResources",
                "/Users/apple/opensource/spring-cloud-kubernetes-microservices-toolkit/example-service-account/build/generated/aotClasses",
                "spring.cloud.kubernetes.example.account",
                "example-service-account"};
        SpringApplicationAotProcessor.main(args);
    }
}