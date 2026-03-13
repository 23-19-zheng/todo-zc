package com.todo.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todo.domain.model.Todo;

import java.util.List;
import java.util.Optional;

/**
 * 待办仓储接口
 */
public interface TodoRepository {

    /**
     * 根据ID查找待办
     */
    Optional<Todo> findById(String id);

    /**
     * 根据ID和用户ID查找待办
     */
    Optional<Todo> findByIdAndUserId(String id, Long userId);

    /**
     * 分页查询用户的待办列表
     */
    IPage<Todo> findByUserId(Long userId, String filter, int page, int pageSize);

    /**
     * 统计用户的待办数量
     */
    long countByUserId(Long userId);

    /**
     * 统计用户已完成的待办数量
     */
    long countCompletedByUserId(Long userId);

    /**
     * 统计用户未完成的待办数量
     */
    long countActiveByUserId(Long userId);

    /**
     * 保存待办
     */
    Todo save(Todo todo);

    /**
     * 更新待办
     */
    void update(Todo todo);

    /**
     * 删除待办
     */
    void deleteById(String id);

    /**
     * 删除用户所有已完成的待办
     */
    int deleteCompletedByUserId(Long userId);

    /**
     * 批量保存待办
     */
    void saveAll(List<Todo> todos);
}
