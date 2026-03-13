package com.todo.app.service;

import com.todo.domain.model.User;
import com.todo.domain.service.AuthDomainService;
import com.todo.facade.request.LoginRequest;
import com.todo.facade.request.RegisterRequest;
import com.todo.facade.response.LoginResponse;
import com.todo.facade.response.TokenResponse;
import com.todo.facade.response.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

/**
 * 认证应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthDomainService authDomainService;

    /**
     * 用户注册
     */
    public LoginResponse register(RegisterRequest request) {
        User user = authDomainService.register(request.getUsername(), request.getPassword());

        String accessToken = authDomainService.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = authDomainService.generateRefreshToken(user.getId());

        UserVO userVO = toUserVO(user);
        return LoginResponse.of(accessToken, userVO);
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        User user = authDomainService.login(request.getUsername(), request.getPassword());

        String accessToken = authDomainService.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = authDomainService.generateRefreshToken(user.getId());

        UserVO userVO = toUserVO(user);
        return LoginResponse.of(accessToken, userVO);
    }

    /**
     * 刷新Token
     */
    public TokenResponse refreshToken(String refreshToken) {
        Long userId = authDomainService.refreshAccessToken(refreshToken);
        User user = authDomainService.getUserById(userId);

        String newAccessToken = authDomainService.generateAccessToken(user.getId(), user.getUsername());
        String newRefreshToken = authDomainService.generateRefreshToken(user.getId());

        TokenResponse response = new TokenResponse();
        response.setToken(newAccessToken);
        return response;
    }

    private UserVO toUserVO(User user) {
        Long createdAt = user.getCreatedAt() != null
                ? user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                : null;
        return UserVO.of(user.getId(), user.getUsername(), createdAt);
    }
}
