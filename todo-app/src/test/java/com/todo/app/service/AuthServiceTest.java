package com.todo.app.service;

import com.todo.common.exception.BizException;
import com.todo.domain.model.User;
import com.todo.domain.service.AuthDomainService;
import com.todo.facade.request.LoginRequest;
import com.todo.facade.request.RegisterRequest;
import com.todo.facade.response.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 认证服务测试
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthDomainService authDomainService;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("用户注册成功")
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        when(authDomainService.register("testuser", "123456")).thenReturn(testUser);
        when(authDomainService.generateAccessToken(any(), any())).thenReturn("accessToken");
        when(authDomainService.generateRefreshToken(any())).thenReturn("refreshToken");

        LoginResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getToken());
        assertEquals("testuser", response.getUser().getUsername());

        verify(authDomainService).register("testuser", "123456");
    }

    @Test
    @DisplayName("用户登录成功")
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        when(authDomainService.login("testuser", "123456")).thenReturn(testUser);
        when(authDomainService.generateAccessToken(any(), any())).thenReturn("accessToken");
        when(authDomainService.generateRefreshToken(any())).thenReturn("refreshToken");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getToken());
        assertEquals("testuser", response.getUser().getUsername());

        verify(authDomainService).login("testuser", "123456");
    }

    @Test
    @DisplayName("用户登录失败-用户不存在")
    void login_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(authDomainService.login("nonexistent", "password"))
                .thenThrow(new BizException("用户不存在"));

        assertThrows(BizException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("用户登录失败-密码错误")
    void login_WrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        when(authDomainService.login("testuser", "wrongpassword"))
                .thenThrow(new BizException("密码错误"));

        assertThrows(BizException.class, () -> authService.login(request));
    }

    @Test
    @DisplayName("用户注册失败-用户名已存在")
    void register_UsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setPassword("123456");

        when(authDomainService.register("existinguser", "123456"))
                .thenThrow(new BizException("用户名已存在"));

        assertThrows(BizException.class, () -> authService.register(request));
    }
}