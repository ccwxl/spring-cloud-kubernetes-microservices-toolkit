package com.example.demo.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.mp.User;

public interface UserService extends IService<User> {


    User loadByUsername(String username);
}
