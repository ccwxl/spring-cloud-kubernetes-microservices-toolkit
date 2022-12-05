package com.example.demo;


import org.springframework.boot.SpringApplicationAotProcessor;

class DemoAotNativeApplicationTest {


    public static void main(String[] args) throws Exception {
        args = new String[]{"com.example.demo.DemoAotNativeApplication",
                "/Users/apple/opensource/spring-cloud-kubernetes-microservices-toolkit/example-aot-native/build/generated/aotSources",
                "/Users/apple/opensource/spring-cloud-kubernetes-microservices-toolkit/example-aot-native/build/generated/aotResources",
                "/Users/apple/opensource/spring-cloud-kubernetes-microservices-toolkit/example-aot-native/build/generated/aotClasses",
                "cc.sofast.springcloud.kubernetes.microservices.toolkit",
                "example-aot-native"};
        SpringApplicationAotProcessor.main(args);
    }

}