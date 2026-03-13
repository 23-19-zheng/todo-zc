package com.todo.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.domain.model.Todo;
import com.todo.domain.service.PreferenceDomainService;
import com.todo.domain.service.TodoDomainService;
import com.todo.facade.request.CreateTodoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 待办服务测试
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoDomainService todoDomainService;

    @Mock
    private PreferenceDomainService preferenceDomainService;

    @InjectMocks
    private TodoService todoService;

    private Todo createTestTodo() {
        Todo todo = new Todo();
        todo.setId("1709800000000_abc123");
        todo.setTitle("测试待办");
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setCompletedAt(null);
        return todo;
    }

    @Test
    @DisplayName("获取待办列表成功")
    void getTodoList_Success() {
        Long userId = 1L;
        String filter = "all";
        int page = 1;
        int pageSize = 10;

        Page<Todo> todoPage = new Page<>(page, pageSize);
        todoPage.setRecords(new ArrayList<>());
        todoPage.setTotal(0);

        when(preferenceDomainService.getFilterPreference(userId)).thenReturn("all");
        when(todoDomainService.getTodoList(userId, filter, page, pageSize)).thenReturn(todoPage);
        when(todoDomainService.countTotal(userId)).thenReturn(0L);
        when(todoDomainService.countActive(userId)).thenReturn(0L);
        when(todoDomainService.countCompleted(userId)).thenReturn(0L);

        var result = todoService.getTodoList(userId, filter, page, pageSize);

        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getActive());
        assertEquals(0, result.getCompleted());
    }

    @Test
    @DisplayName("创建待办成功")
    void createTodo_Success() {
        Long userId = 1L;
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("新的待办");

        Todo todo = createTestTodo();
        when(todoDomainService.createTodo(userId, "新的待办")).thenReturn(todo);

        var result = todoService.createTodo(userId, request);

        assertNotNull(result);
        assertEquals("1709800000000_abc123", result.getId());
        assertEquals("测试待办", result.getTitle());
        assertFalse(result.getCompleted());

        verify(todoDomainService).createTodo(userId, "新的待办");
    }

    @Test
    @DisplayName("更新待办成功")
    void updateTodo_Success() {
        Long userId = 1L;
        String todoId = "1709800000000_abc123";
        String newTitle = "更新后的标题";

        Todo todo = createTestTodo();
        todo.setTitle(newTitle);

        when(todoDomainService.updateTodoTitle(todoId, userId, newTitle)).thenReturn(todo);

        var result = todoService.updateTodo(userId, todoId,
                new com.todo.facade.request.UpdateTodoRequest() {{ setTitle(newTitle); }});

        assertNotNull(result);
        assertEquals(newTitle, result.getTitle());
    }

    @Test
    @DisplayName("切换待办状态成功")
    void toggleTodo_Success() {
        Long userId = 1L;
        String todoId = "1709800000000_abc123";

        Todo todo = createTestTodo();
        todo.setCompleted(true);
        todo.setCompletedAt(LocalDateTime.now());

        when(todoDomainService.toggleTodo(todoId, userId)).thenReturn(todo);

        var result = todoService.toggleTodo(userId, todoId);

        assertNotNull(result);
        assertTrue(result.getCompleted());
        assertNotNull(result.getCompletedAt());
    }

    @Test
    @DisplayName("删除待办成功")
    void deleteTodo_Success() {
        Long userId = 1L;
        String todoId = "1709800000000_abc123";

        doNothing().when(todoDomainService).deleteTodo(todoId, userId);

        todoService.deleteTodo(userId, todoId);

        verify(todoDomainService).deleteTodo(todoId, userId);
    }

    @Test
    @DisplayName("清除已完成待办成功")
    void clearCompleted_Success() {
        Long userId = 1L;

        when(todoDomainService.clearCompleted(userId)).thenReturn(5);

        var result = todoService.clearCompleted(userId);

        assertNotNull(result);
        assertEquals(5, result.getClearedCount());
    }
}