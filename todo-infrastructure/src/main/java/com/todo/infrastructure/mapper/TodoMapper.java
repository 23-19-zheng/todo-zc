package com.todo.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todo.infrastructure.entity.TodoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 待办事项Mapper
 */
@Mapper
public interface TodoMapper extends BaseMapper<TodoEntity> {

    /**
     * 删除用户所有已完成的待办
     */
    @Update("UPDATE t_todo SET deleted = 1, update_time = NOW() WHERE user_id = #{userId} AND completed = 1 AND deleted = 0")
    int deleteCompletedByUserId(@Param("userId") Long userId);
}
