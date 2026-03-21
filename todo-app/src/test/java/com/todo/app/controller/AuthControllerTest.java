package com.todo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.service.AuthService;
import com.todo.facade.request.LoginRequest;
import com.todo.facade.request.RegisterRequest;
import com.todo.facade.response.LoginResponse;
import com.todo.facade.response.UserVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证控制器测试
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("用户注册成功")
    void register_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        UserVO userVO = UserVO.of(1L, "testuser", System.currentTimeMillis());
        LoginResponse response = LoginResponse.of("token", userVO);

        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.token").value("token"));
    }

    @Test
    @DisplayName("注册参数校验失败-用户名过短")
    void register_ValidationFail_ShortUsername() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("ab");
        request.setPassword("123456");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1002));
    }

    @Test
    @DisplayName("注册参数校验失败-密码过短")
    void register_ValidationFail_ShortPassword() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1002));
    }

    @Test
    @DisplayName("用户登录成功")
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("123456");

        UserVO userVO = UserVO.of(1L, "testuser", System.currentTimeMillis());
        LoginResponse response = LoginResponse.of("token", userVO);

        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.data.token").value("token"));
    }

    @Test
    @DisplayName("登录参数校验失败-用户名为空")
    void login_ValidationFail_EmptyUsername() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("123456");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("登录参数校验失败-密码过短")
    void login_ValidationFail_ShortPassword() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}