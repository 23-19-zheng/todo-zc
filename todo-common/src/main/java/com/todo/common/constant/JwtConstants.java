package com.todo.common.constant;

/**
 * JWT相关常量
 */
public class JwtConstants {

    private JwtConstants() {
    }

    /**
     * Authorization请求头
     */
    public static final String HEADER = "Authorization";

    /**
     * Token前缀
     */
    public static final String PREFIX = "Bearer ";

    /**
     * JWT subject - 用户ID
     */
    public static final String CLAIM_USER_ID = "userId";

    /**
     * JWT claim - 用户名
     */
    public static final String CLAIM_USERNAME = "username";

    /**
     * JWT claim - Token类型
     */
    public static final String CLAIM_TYPE = "type";

    /**
     * Access Token类型
     */
    public static final String TYPE_ACCESS = "access";

    /**
     * Refresh Token类型
     */
    public static final String TYPE_REFRESH = "refresh";
}
