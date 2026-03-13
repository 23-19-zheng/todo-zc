package com.todo.common.context;

import lombok.Data;

/**
 * 用户上下文 - 存储当前登录用户信息
 */
@Data
public class UserContext {

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    public UserContext() {
    }

    public UserContext(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     * 设置当前用户上下文
     */
    public static void set(UserContext context) {
        CONTEXT.set(context);
    }

    /**
     * 获取当前用户上下文
     */
    public static UserContext get() {
        return CONTEXT.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        UserContext context = get();
        return context != null ? context.getUserId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        UserContext context = get();
        return context != null ? context.getUsername() : null;
    }

    /**
     * 清除当前用户上下文
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
