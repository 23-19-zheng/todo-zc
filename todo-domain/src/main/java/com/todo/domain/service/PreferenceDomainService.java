package com.todo.domain.service;

import com.todo.common.enums.FilterEnum;
import com.todo.domain.model.UserPreference;
import com.todo.domain.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户偏好领域服务
 */
@Service
@RequiredArgsConstructor
public class PreferenceDomainService {

    private final UserPreferenceRepository userPreferenceRepository;

    /**
     * 获取用户筛选偏好
     */
    public String getFilterPreference(Long userId) {
        return userPreferenceRepository.findByUserId(userId)
                .map(UserPreference::getFilter)
                .orElse(FilterEnum.ALL.getValue());
    }

    /**
     * 保存用户筛选偏好
     */
    public void saveFilterPreference(Long userId, String filter) {
        // 验证筛选值
        FilterEnum.fromValue(filter);

        UserPreference preference = new UserPreference(userId, filter);
        userPreferenceRepository.save(preference);
    }
}
