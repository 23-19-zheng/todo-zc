package com.todo.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todo.common.enums.ResponseCodeEnum;
import com.todo.common.exception.BizException;
import com.todo.domain.model.Todo;
import com.todo.domain.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 待办领域服务
 */
@Service
@RequiredArgsConstructor
public class TodoDomainService {

    private final TodoRepository todoRepository;

    /**
     * 创建待办
     */
    public Todo createTodo(Long userId, String title) {
        Todo todo = new Todo();
        todo.setId(Todo.generateId());
        todo.setTitle(title);
        todo.setCompleted(false);
        todo.setUserId(userId);
        todo.setCreatedAt(LocalDateTime.now());
        return todoRepository.save(todo);
    }

    /**
     * 更新待办标题
     */
    public Todo updateTodoTitle(String todoId, Long userId, String title) {
        Todo todo = getTodoByIdAndUserId(todoId, userId);
        todo.setTitle(title);
        todoRepository.update(todo);
        return todo;
    }

    /**
     * 切换待办完成状态
     */
    public Todo toggleTodo(String todoId, Long userId) {
        Todo todo = getTodoByIdAndUserId(todoId, userId);
        if (Boolean.TRUE.equals(todo.getCompleted())) {
            todo.markAsActive();
        } else {
            todo.markAsCompleted();
        }
        todoRepository.update(todo);
        return todo;
    }

    /**
     * 删除待办
     */
    public void deleteTodo(String todoId, Long userId) {
        // 验证待办存在且属于当前用户
        getTodoByIdAndUserId(todoId, userId);
        todoRepository.deleteById(todoId);
    }

    /**
     * 清除已完成的待办
     */
    public int clearCompleted(Long userId) {
        return todoRepository.deleteCompletedByUserId(userId);
    }

    /**
     * 分页查询待办列表
     */
    public IPage<Todo> getTodoList(Long userId, String filter, int page, int pageSize) {
        return todoRepository.findByUserId(userId, filter, page, pageSize);
    }

    /**
     * 获取待办详情
     */
    public Todo getTodoById(String todoId, Long userId) {
        return getTodoByIdAndUserId(todoId, userId);
    }

    /**
     * 统计总数
     */
    public long countTotal(Long userId) {
        return todoRepository.countByUserId(userId);
    }

    /**
     * 统计已完成数
     */
    public long countCompleted(Long userId) {
        return todoRepository.countCompletedByUserId(userId);
    }

    /**
     * 统计未完成数
     */
    public long countActive(Long userId) {
        return todoRepository.countActiveByUserId(userId);
    }

    /**
     * 根据ID和用户ID获取待办
     */
    private Todo getTodoByIdAndUserId(String todoId, Long userId) {
        return todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new BizException(ResponseCodeEnum.RESOURCE_NOT_FOUND, "待办事项不存在"));
    }
}
