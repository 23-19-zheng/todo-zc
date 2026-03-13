package com.todo.app.controller;

import com.todo.app.service.TodoService;
import com.todo.common.context.UserContext;
import com.todo.common.constant.CommonConstants;
import com.todo.facade.request.CreateTodoRequest;
import com.todo.facade.request.SyncTodoRequest;
import com.todo.facade.request.UpdateTodoRequest;
import com.todo.facade.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @GetMapping
    public ApiResponse<TodoListVO> getTodoList(
            @ApiParam("筛选条件: all/active/completed") @RequestParam(required = false) String filter,
            @ApiParam("页码") @RequestParam(defaultValue = "1") int page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "50") int pageSize) {

        // 限制pageSize最大值
        if (pageSize > CommonConstants.MAX_PAGE_SIZE) {
            pageSize = CommonConstants.MAX_PAGE_SIZE;
        }

        Long userId = UserContext.getCurrentUserId();
        log.debug("获取待办列表: userId={}, filter={}, page={}, pageSize={}", userId, filter, page, pageSize);
        TodoListVO response = todoService.getTodoList(userId, filter, page, pageSize);
        return ApiResponse.success(response);
    }

    /**
     * 获取单个待办
     */
    @ApiOperation("获取单个待办")
    @GetMapping("/{id}")
    public ApiResponse<TodoVO> getTodo(@PathVariable String id) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("获取待办详情: userId={}, todoId={}", userId, id);
        TodoVO response = todoService.getTodo(userId, id);
        return ApiResponse.success(response);
    }

    /**
     * 创建待办
     */
    @ApiOperation("创建待办")
    @PostMapping
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
    @PutMapping("/{id}")
    public ApiResponse<TodoVO> updateTodo(@PathVariable String id,
                                          @Valid @RequestBody UpdateTodoRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("更新待办: userId={}, todoId={}, title={}", userId, id, request.getTitle());
        TodoVO response = todoService.updateTodo(userId, id, request);
        return ApiResponse.success(response);
    }

    /**
     * 切换完成状态
     */
    @ApiOperation("切换完成状态")
    @PatchMapping("/{id}/toggle")
    public ApiResponse<TodoVO> toggleTodo(@PathVariable String id) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("切换待办状态: userId={}, todoId={}", userId, id);
        TodoVO response = todoService.toggleTodo(userId, id);
        return ApiResponse.success(response);
    }

    /**
     * 删除待办
     */
    @ApiOperation("删除待办")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTodo(@PathVariable String id) {
        Long userId = UserContext.getCurrentUserId();
        log.debug("删除待办: userId={}, todoId={}", userId, id);
        todoService.deleteTodo(userId, id);
        return ApiResponse.success();
    }

    /**
     * 清除已完成待办
     */
    @ApiOperation("清除已完成待办")
    @DeleteMapping("/completed")
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
