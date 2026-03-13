package com.todo.domain.service;

import com.todo.common.enums.ResponseCodeEnum;
import com.todo.common.exception.BizException;
import com.todo.domain.model.User;
import com.todo.domain.repository.UserRepository;
import com.todo.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证领域服务
 */
@Service
@RequiredArgsConstructor
public class AuthDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户注册
     */
    public User register(String username, String password) {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(username)) {
            throw new BizException(ResponseCodeEnum.USERNAME_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        // 查找用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BizException(ResponseCodeEnum.USER_NOT_FOUND));

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException(ResponseCodeEnum.PASSWORD_ERROR);
        }

        return user;
    }

    /**
     * 刷新Token
     */
    public Long refreshAccessToken(String refreshToken) {
        // 验证RefreshToken
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }

        return jwtTokenProvider.getUserIdFromToken(refreshToken);
    }

    /**
     * 根据ID获取用户
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResponseCodeEnum.USER_NOT_FOUND));
    }

    /**
     * 生成AccessToken
     */
    public String generateAccessToken(Long userId, String username) {
        return jwtTokenProvider.generateAccessToken(userId, username);
    }

    /**
     * 生成RefreshToken
     */
    public String generateRefreshToken(Long userId) {
        return jwtTokenProvider.generateRefreshToken(userId);
    }
}
