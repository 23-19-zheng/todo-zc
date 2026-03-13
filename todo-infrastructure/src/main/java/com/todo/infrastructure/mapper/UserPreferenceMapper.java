package com.todo.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todo.infrastructure.entity.UserPreferenceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户偏好Mapper
 */
@Mapper
public interface UserPreferenceMapper extends BaseMapper<UserPreferenceEntity> {

}
