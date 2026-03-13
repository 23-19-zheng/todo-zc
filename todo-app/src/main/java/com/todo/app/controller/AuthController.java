package com.todo.app.controller;

import com.todo.app.service.AuthService;
import com.todo.common.constant.JwtConstants;
import com.todo.facade.request.LoginRequest;
import com.todo.facade.request.RegisterRequest;
import com.todo.facade.response.ApiResponse;
import com.todo.facade.response.LoginResponse;
import com.todo.facade.response.TokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 */
@Slf4j
@Api(tags = "认证接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册请求: username={}", request.getUsername());
        LoginResponse response = authService.register(request);
        log.info("用户注册成功: username={}, userId={}", request.getUsername(), response.getUser().getId());
        return ApiResponse.success(response);
    }

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("用户登录请求: username={}", request.getUsername());
        LoginResponse response = authService.login(request);
        log.info("用户登录成功: username={}, userId={}", request.getUsername(), response.getUser().getId());
        return ApiResponse.success(response);
    }

    /**
     * 刷新Token
     */
    @ApiOperation("刷新Token")
    @PostMapping("/refresh")
    public ApiResponse<TokenResponse> refreshToken(@RequestHeader(JwtConstants.HEADER) String authHeader) {
        log.debug("刷新Token请求");
        // 从Authorization头中提取token
        String refreshToken = authHeader;
        if (refreshToken != null && refreshToken.startsWith(JwtConstants.PREFIX)) {
            refreshToken = refreshToken.substring(JwtConstants.PREFIX.length());
        }

        TokenResponse response = authService.refreshToken(refreshToken);
        log.debug("Token刷新成功");
        return ApiResponse.success(response);
    }
}
