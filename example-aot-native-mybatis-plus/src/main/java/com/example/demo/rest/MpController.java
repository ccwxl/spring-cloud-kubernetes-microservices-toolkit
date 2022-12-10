package com.example.demo.rest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.mp.User;
import com.example.demo.mp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author apple
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MpController {

    private final UserService userService;

    @PostMapping("/mp/user")
    public User addUser() {
        User user = new User();
        user.setSsn(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString().substring(0, 5) + "@gmail.com");
        user.setAddress("山东济南市");
        user.setMobile("15636987541");
        boolean save = userService.save(user);
        if (save) {
            return user;
        }
        throw new RuntimeException("save user error.");
    }

    @GetMapping("/mp/user")
    public User getUser(@RequestParam String ssn) {
        log.info("this class :[{}]", this.getClass().getName());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        return userService.getOne(
                userLambdaQueryWrapper.eq(User::getSsn, ssn));
    }

    @GetMapping("/mp/user/wrapper")
    public User testQueryWrapper(@RequestParam String ssn) {

        return new User();
    }
}
