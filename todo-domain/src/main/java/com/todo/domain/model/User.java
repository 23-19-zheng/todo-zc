package com.todo.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户领域模型
 */
@Data
public class User {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
