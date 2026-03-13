package com.todo.domain.repository;

import com.todo.domain.model.UserPreference;

import java.util.Optional;

/**
 * 用户偏好仓储接口
 */
public interface UserPreferenceRepository {

    /**
     * 根据用户ID查找偏好
     */
    Optional<UserPreference> findByUserId(Long userId);

    /**
     * 保存偏好
     */
    UserPreference save(UserPreference preference);
}
