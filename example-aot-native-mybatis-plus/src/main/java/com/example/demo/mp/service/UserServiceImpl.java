package com.example.demo.mp.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.mp.User;
import com.example.demo.mp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author apple
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public User loadByUsername(String username) {

        return null;
    }
}
