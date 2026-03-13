package com.todo.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.todo.domain.model.Todo;
import com.todo.domain.service.PreferenceDomainService;
import com.todo.domain.service.TodoDomainService;
import com.todo.facade.request.CreateTodoRequest;
import com.todo.facade.request.SyncTodoRequest;
import com.todo.facade.request.UpdateTodoRequest;
import com.todo.facade.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 待办应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoDomainService todoDomainService;
    private final PreferenceDomainService preferenceDomainService;

    /**
     * 获取待办列表
     */
    public TodoListVO getTodoList(Long userId, String filter, int page, int pageSize) {
        // 如果filter为空，从用户偏好获取
        if (filter == null || filter.isEmpty()) {
            filter = preferenceDomainService.getFilterPreference(userId);
        }

        IPage<Todo> todoPage = todoDomainService.getTodoList(userId, filter, page, pageSize);

        TodoListVO vo = new TodoListVO();
        vo.setList(todoPage.getRecords().stream().map(this::toTodoVO).collect(Collectors.toList()));
        vo.setTotal(todoDomainService.countTotal(userId));
        vo.setActive(todoDomainService.countActive(userId));
        vo.setCompleted(todoDomainService.countCompleted(userId));
        vo.setPage((int) todoPage.getCurrent());
        vo.setPageSize((int) todoPage.getSize());

        return vo;
    }

    /**
     * 获取单个待办
     */
    public TodoVO getTodo(Long userId, String todoId) {
        Todo todo = todoDomainService.getTodoById(todoId, userId);
        return toTodoVO(todo);
    }

    /**
     * 创建待办
     */
    public TodoVO createTodo(Long userId, CreateTodoRequest request) {
        Todo todo = todoDomainService.createTodo(userId, request.getTitle());
        return toTodoVO(todo);
    }

    /**
     * 更新待办
     */
    public TodoVO updateTodo(Long userId, String todoId, UpdateTodoRequest request) {
        Todo todo = todoDomainService.updateTodoTitle(todoId, userId, request.getTitle());
        return toTodoVO(todo);
    }

    /**
     * 切换待办状态
     */
    public TodoVO toggleTodo(Long userId, String todoId) {
        Todo todo = todoDomainService.toggleTodo(todoId, userId);
        return toTodoVO(todo);
    }

    /**
     * 删除待办
     */
    public void deleteTodo(Long userId, String todoId) {
        todoDomainService.deleteTodo(todoId, userId);
    }

    /**
     * 清除已完成待办
     */
    public ClearCompletedVO clearCompleted(Long userId) {
        int count = todoDomainService.clearCompleted(userId);
        ClearCompletedVO vo = new ClearCompletedVO();
        vo.setClearedCount(count);
        return vo;
    }

    /**
     * 批量同步
     */
    @Transactional
    public SyncResultVO syncTodos(Long userId, SyncTodoRequest request) {
        int syncedCount = 0;
        int failedCount = 0;
        List<Todo> syncedTodos = new ArrayList<>();

        for (SyncTodoRequest.SyncAction action : request.getActions()) {
            try {
                Todo todo = processSyncAction(userId, action);
                if (todo != null) {
                    syncedTodos.add(todo);
                }
                syncedCount++;
            } catch (Exception e) {
                failedCount++;
                log.warn("同步操作失败: userId={}, actionType={}, actionId={}, error={}",
                        userId, action.getType(), action.getId(), e.getMessage());
            }
        }

        log.info("批量同步完成: userId={}, total={}, synced={}, failed={}",
                userId, request.getActions().size(), syncedCount, failedCount);

        SyncResultVO vo = new SyncResultVO();
        vo.setSyncedCount(syncedCount);
        vo.setTodos(syncedTodos.stream().map(this::toTodoVO).collect(Collectors.toList()));
        return vo;
    }

    private Todo processSyncAction(Long userId, SyncTodoRequest.SyncAction action) {
        String type = action.getType();
        SyncTodoRequest.SyncActionData data = action.getData();

        switch (type) {
            case "create":
                return todoDomainService.createTodo(userId, data.getTitle());
            case "update":
                return todoDomainService.updateTodoTitle(action.getId(), userId, data.getTitle());
            case "toggle":
                return todoDomainService.toggleTodo(action.getId(), userId);
            case "delete":
                todoDomainService.deleteTodo(action.getId(), userId);
                return null;
            case "clearCompleted":
                todoDomainService.clearCompleted(userId);
                return null;
            default:
                return null;
        }
    }

    private TodoVO toTodoVO(Todo todo) {
        TodoVO vo = new TodoVO();
        vo.setId(todo.getId());
        vo.setTitle(todo.getTitle());
        vo.setCompleted(todo.getCompleted());
        vo.setCreatedAt(todo.getCreatedAt() != null
                ? todo.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                : null);
        vo.setCompletedAt(todo.getCompletedAt() != null
                ? todo.getCompletedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                : null);
        return vo;
    }
}
