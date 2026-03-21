package com.todo.app.controller;

import com.todo.app.service.TodoService;
import com.todo.common.context.UserContext;
import com.todo.common.constant.CommonConstants;
import com.todo.facade.request.*;
import com.todo.facade.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 待办事项控制器
 */
@Slf4j
@Api(tags = "待办事项接口")
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * 获取待办列表
     */
    @ApiOperation("获取待办列表")
    @PostMapping("/getList")
    public ApiResponse<TodoListVO> getTodoList(@RequestBody GetTodoListRequest request) {
        int page = request.getPage() != null ? request.getPage() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 50;

        // 限制pageSize最大值
        if (pageSize > CommonConstants.MAX_PAGE_SIZE) {
            pageSize = CommonConstants.MAX_PAGE_SIZE;
        }

        Long userId = UserContext.getCurrentUserId();
        log.debug("获取待办列表: userId={}, filter={}, page={}, pageSize={}", userId, request.getFilter(), page, pageSize);
        TodoListVO response = todoService.getTodoList(userId, request.getFilter(), page, pageSize);
        return ApiResponse.success(response);
    }

    /**
     * 获取单个待办
     */
    @ApiOperation("获取单个待办")
    @PostMapping("/getOne")
    public ApiResponse<TodoVO> getTodo(@Valid @RequestBody GetTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("获取待办详情: userId={}, todoId={}", userId, request.getId());
        TodoVO response = todoService.getTodo(userId, request.getId());
        return ApiResponse.success(response);
    }

    /**
     * 创建待办
     */
    @ApiOperation("创建待办")
    @PostMapping("/create")
    public ApiResponse<TodoVO> createTodo(@Valid @RequestBody CreateTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("创建待办: userId={}, title={}", userId, request.getTitle());
        TodoVO response = todoService.createTodo(userId, request);
        log.debug("待办创建成功: userId={}, todoId={}", userId, response.getId());
        return ApiResponse.success(response);
    }

    /**
     * 更新待办标题
     */
    @ApiOperation("更新待办标题")
    @PostMapping("/update")
    public ApiResponse<TodoVO> updateTodo(@Valid @RequestBody UpdateTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("更新待办: userId={}, todoId={}, title={}", userId, request.getId(), request.getTitle());
        TodoVO response = todoService.updateTodo(userId, request.getId(), request);
        return ApiResponse.success(response);
    }

    /**
     * 切换完成状态
     */
    @ApiOperation("切换完成状态")
    @PostMapping("/toggle")
    public ApiResponse<TodoVO> toggleTodo(@Valid @RequestBody ToggleTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("切换待办状态: userId={}, todoId={}", userId, request.getId());
        TodoVO response = todoService.toggleTodo(userId, request.getId());
        return ApiResponse.success(response);
    }

    /**
     * 删除待办
     */
    @ApiOperation("删除待办")
    @PostMapping("/delete")
    public ApiResponse<Void> deleteTodo(@Valid @RequestBody DeleteTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("删除待办: userId={}, todoId={}", userId, request.getId());
        todoService.deleteTodo(userId, request.getId());
        return ApiResponse.success();
    }

    /**
     * 清除已完成待办
     */
    @ApiOperation("清除已完成待办")
    @PostMapping("/clearCompleted")
    public ApiResponse<ClearCompletedVO> clearCompleted() {
        Long userId = UserContext.getCurrentUserId();
        log.debug("清除已完成待办: userId={}", userId);
        ClearCompletedVO response = todoService.clearCompleted(userId);
        log.info("清除已完成待办: userId={}, clearedCount={}", userId, response.getClearedCount());
        return ApiResponse.success(response);
    }

    /**
     * 批量同步
     */
    @ApiOperation("批量同步")
    @PostMapping("/sync")
    public ApiResponse<SyncResultVO> syncTodos(@Valid @RequestBody SyncTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("批量同步待办: userId={}, actionCount={}", userId, request.getActions().size());
        SyncResultVO response = todoService.syncTodos(userId, request);
        log.info("批量同步完成: userId={}, syncedCount={}", userId, response.getSyncedCount());
        return ApiResponse.success(response);
    }
}