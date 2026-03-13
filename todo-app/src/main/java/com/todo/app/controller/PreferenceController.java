package com.todo.app.controller;

import com.todo.app.service.PreferenceService;
import com.todo.common.context.UserContext;
import com.todo.facade.request.SaveFilterPreferenceRequest;
import com.todo.facade.response.ApiResponse;
import com.todo.facade.response.FilterPreferenceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户偏好控制器
 */
@Api(tags = "用户偏好接口")
@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    /**
     * 获取筛选偏好
     */
    @ApiOperation("获取筛选偏好")
    @GetMapping("/filter")
    public ApiResponse<FilterPreferenceVO> getFilterPreference() {
        Long userId = UserContext.getCurrentUserId();
        FilterPreferenceVO response = preferenceService.getFilterPreference(userId);
        return ApiResponse.success(response);
    }

    /**
     * 保存筛选偏好
     */
    @ApiOperation("保存筛选偏好")
    @PutMapping("/filter")
    public ApiResponse<Void> saveFilterPreference(@Valid @RequestBody SaveFilterPreferenceRequest request) {
        Long userId = UserContext.getCurrentUserId();
        preferenceService.saveFilterPreference(userId, request);
        return ApiResponse.success();
    }
}
