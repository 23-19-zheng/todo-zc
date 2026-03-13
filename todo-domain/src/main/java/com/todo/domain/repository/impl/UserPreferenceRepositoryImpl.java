package com.todo.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todo.domain.model.UserPreference;
import com.todo.domain.repository.UserPreferenceRepository;
import com.todo.infrastructure.entity.UserPreferenceEntity;
import com.todo.infrastructure.mapper.UserPreferenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户偏好仓储实现
 */
@Repository
@RequiredArgsConstructor
public class UserPreferenceRepositoryImpl implements UserPreferenceRepository {

    private final UserPreferenceMapper userPreferenceMapper;

    @Override
    public Optional<UserPreference> findByUserId(Long userId) {
        LambdaQueryWrapper<UserPreferenceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPreferenceEntity::getUserId, userId);
        UserPreferenceEntity entity = userPreferenceMapper.selectOne(wrapper);
        return Optional.ofNullable(entity).map(this::toModel);
    }

    @Override
    public UserPreference save(UserPreference preference) {
        // 先查询是否存在
        Optional<UserPreference> existing = findByUserId(preference.getUserId());
        if (existing.isPresent()) {
            // 更新
            UserPreferenceEntity entity = new UserPreferenceEntity();
            entity.setId(existing.get().getId());
            entity.setUserId(preference.getUserId());
            entity.setFilter(preference.getFilter());
            userPreferenceMapper.updateById(entity);
            preference.setId(existing.get().getId());
        } else {
            // 新增
            UserPreferenceEntity entity = toEntity(preference);
            userPreferenceMapper.insert(entity);
            preference.setId(entity.getId());
        }
        return preference;
    }

    private UserPreference toModel(UserPreferenceEntity entity) {
        UserPreference preference = new UserPreference();
        preference.setId(entity.getId());
        preference.setUserId(entity.getUserId());
        preference.setFilter(entity.getFilter());
        return preference;
    }

    private UserPreferenceEntity toEntity(UserPreference preference) {
        UserPreferenceEntity entity = new UserPreferenceEntity();
        entity.setId(preference.getId());
        entity.setUserId(preference.getUserId());
        entity.setFilter(preference.getFilter());
        return entity;
    }
}
