package com.todo.app.service;

import com.todo.domain.service.PreferenceDomainService;
import com.todo.facade.request.SaveFilterPreferenceRequest;
import com.todo.facade.response.FilterPreferenceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户偏好应用服务
 */
@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceDomainService preferenceDomainService;

    /**
     * 获取筛选偏好
     */
    public FilterPreferenceVO getFilterPreference(Long userId) {
        String filter = preferenceDomainService.getFilterPreference(userId);
        FilterPreferenceVO vo = new FilterPreferenceVO();
        vo.setFilter(filter);
        return vo;
    }

    /**
     * 保存筛选偏好
     */
    public void saveFilterPreference(Long userId, SaveFilterPreferenceRequest request) {
        preferenceDomainService.saveFilterPreference(userId, request.getFilter());
    }
}
