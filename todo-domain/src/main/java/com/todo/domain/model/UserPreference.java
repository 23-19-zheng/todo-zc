package com.todo.domain.model;

import lombok.Data;

/**
 * 用户偏好领域模型
 */
@Data
public class UserPreference {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 筛选条件
     */
    private String filter;

    public UserPreference() {
    }

    public UserPreference(Long userId, String filter) {
        this.userId = userId;
        this.filter = filter;
    }
}
