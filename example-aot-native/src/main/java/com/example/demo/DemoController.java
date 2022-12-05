package com.example.demo;

import com.example.demo.DemoController.DemoControllerRuntimeHints;
import com.example.demo.hello.HelloService;
import com.example.demo.hello.ResourceHelloService;
import com.example.demo.hello.SimpleHelloService;
import com.example.demo.mp.Test;
import com.example.demo.mp.TestMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.aot.hint.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Optional;

@RestController
@ImportRuntimeHints(DemoControllerRuntimeHints.class)
public class DemoController {

    private final ObjectProvider<HelloService> helloServices;

    private final TestMapper testMapper;

    DemoController(ObjectProvider<HelloService> helloServices,
                   TestMapper testMappers) {
        this.helloServices = helloServices;
        this.testMapper = testMappers;
    }

    @GetMapping("/hello")
    HelloResponse hello(@RequestParam(required = false) String mode) throws Exception {
        String message = getHelloMessage(mode, "Native");
        return new HelloResponse(message);
    }

    @GetMapping("/hello/mp")
    Test helloMp(@RequestParam(required = false) String name) throws Exception {
        Test test = new Test(name);
        testMapper.insert(test);
        return test;
    }

    private String getHelloMessage(String mode, String name) throws Exception {
        if (mode == null) {
            return "No option provided";
        } else if (mode.equals("bean")) {
            HelloService service = this.helloServices.getIfUnique();
            return (service != null) ? service.sayHello(name) : "No bean found";
        } else if (mode.equals("reflection")) {
            String implementationName = Optional.ofNullable(getDefaultHelloServiceImplementation())
                    .orElse(SimpleHelloService.class.getName());
            Class<?> implementationClass = ClassUtils.forName(implementationName, getClass().getClassLoader());
            Method method = implementationClass.getMethod("sayHello", String.class);
            Object instance = BeanUtils.instantiateClass(implementationClass);
            return (String) ReflectionUtils.invokeMethod(method, instance, name);
        } else if (mode.equals("resource")) {
            ResourceHelloService helloService = new ResourceHelloService(new ClassPathResource("hello.txt"));
            return helloService.sayHello(name);
        }
        return "Unknown mode: " + mode;
    }


    public record HelloResponse(String message) {

    }

    // Tricking Graal to not deduce a constant
    protected String getDefaultHelloServiceImplementation() {
        return null;
    }

    static class DemoControllerRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerConstructor(org.apache.ibatis.builder.CacheRefResolver.class.getConstructors()[0], ExecutableMode.INTROSPECT);

            hints.reflection().registerType(org.apache.ibatis.builder.CacheRefResolver.class,
                    MemberCategory.PUBLIC_FIELDS,
                    MemberCategory.DECLARED_FIELDS,
                    MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_PUBLIC_METHODS,
                    MemberCategory.INVOKE_PUBLIC_METHODS,
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.PUBLIC_CLASSES,
                    MemberCategory.DECLARED_CLASSES,
                    MemberCategory.INTROSPECT_DECLARED_METHODS);
            hints.reflection().registerType(org.apache.ibatis.parsing.XNode.class, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS);
            hints.reflection().registerType(org.apache.ibatis.builder.annotation.MethodResolver.class, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS);
            hints.reflection().registerType(org.apache.ibatis.mapping.ResultFlag.class, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS);
            hints.reflection().registerType(org.apache.ibatis.builder.ResultMapResolver.class, MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS);


            hints.reflection().registerType(org.apache.ibatis.builder.annotation.MapperAnnotationBuilder.class, MemberCategory.PUBLIC_FIELDS,
                    MemberCategory.DECLARED_FIELDS,
                    MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_PUBLIC_METHODS,
                    MemberCategory.INVOKE_PUBLIC_METHODS,
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.PUBLIC_CLASSES,
                    MemberCategory.DECLARED_CLASSES,
                    MemberCategory.INTROSPECT_DECLARED_METHODS);

            hints.reflection().registerType(MapperBuilderAssistant.class, MemberCategory.PUBLIC_FIELDS,
                    MemberCategory.DECLARED_FIELDS,
                    MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INTROSPECT_PUBLIC_METHODS,
                    MemberCategory.INVOKE_PUBLIC_METHODS,
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.PUBLIC_CLASSES,
                    MemberCategory.DECLARED_CLASSES,
                    MemberCategory.INTROSPECT_DECLARED_METHODS);


            hints.reflection()
                    .registerConstructor(SimpleHelloService.class.getConstructors()[0], ExecutableMode.INVOKE)
                    .registerMethod(ReflectionUtils.findMethod(SimpleHelloService.class, "sayHello", String.class), ExecutableMode.INVOKE);


            hints.resources().registerPattern("hello.txt");

        }

    }

}