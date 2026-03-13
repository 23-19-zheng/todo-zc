package com.todo.infrastructure.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT签名密钥
     */
    private String secret;

    /**
     * AccessToken过期时间（毫秒）
     */
    private long accessTokenExpiration = 86400000L; // 24小时

    /**
     * RefreshToken过期时间（毫秒）
     */
    private long refreshTokenExpiration = 604800000L; // 7天

    /**
     * Authorization请求头名称
     */
    private String header = "Authorization";

    /**
     * Token前缀
     */
    private String prefix = "Bearer ";
}
