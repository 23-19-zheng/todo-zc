package com.todo.common.constant;

/**
 * Redis Key常量
 */
public class RedisConstants {

    private RedisConstants() {
    }

    /**
     * Key前缀
     */
    private static final String PREFIX = "todo:";

    /**
     * RefreshToken Key前缀
     * 完整Key: todo:refresh:{userId}
     */
    public static final String REFRESH_TOKEN_PREFIX = PREFIX + "refresh:";

    /**
     * 用户筛选偏好Key前缀
     * 完整Key: todo:preference:filter:{userId}
     */
    public static final String PREFERENCE_FILTER_PREFIX = PREFIX + "preference:filter:";

    /**
     * 构建RefreshToken Key
     */
    public static String buildRefreshTokenKey(Long userId) {
        return REFRESH_TOKEN_PREFIX + userId;
    }

    /**
     * 构建用户筛选偏好Key
     */
    public static String buildPreferenceFilterKey(Long userId) {
        return PREFERENCE_FILTER_PREFIX + userId;
    }
}
