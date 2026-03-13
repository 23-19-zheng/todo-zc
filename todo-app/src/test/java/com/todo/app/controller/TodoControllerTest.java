package com.todo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.service.TodoService;
import com.todo.facade.request.CreateTodoRequest;
import com.todo.facade.response.TodoListVO;
import com.todo.facade.response.TodoVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 待办控制器测试
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    private TodoVO createTestTodoVO() {
        TodoVO vo = new TodoVO();
        vo.setId("1709800000000_abc123");
        vo.setTitle("测试待办");
        vo.setCompleted(false);
        vo.setCreatedAt(System.currentTimeMillis());
        vo.setCompletedAt(null);
        return vo;
    }

    @Test
    @DisplayName("获取待办列表成功")
    void getTodoList_Success() throws Exception {
        TodoListVO listVO = new TodoListVO();
        listVO.setList(new ArrayList<>());
        listVO.setTotal(0);
        listVO.setActive(0);
        listVO.setCompleted(0);
        listVO.setPage(1);
        listVO.setPageSize(50);

        when(todoService.getTodoList(any(), any(), any(), any())).thenReturn(listVO);

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    @DisplayName("获取待办列表-带筛选参数")
    void getTodoList_WithFilter() throws Exception {
        TodoListVO listVO = new TodoListVO();
        listVO.setList(new ArrayList<>());
        listVO.setTotal(0);
        listVO.setActive(0);
        listVO.setCompleted(0);
        listVO.setPage(1);
        listVO.setPageSize(50);

        when(todoService.getTodoList(any(), any(), any(), any())).thenReturn(listVO);

        mockMvc.perform(get("/todos?filter=active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    @DisplayName("创建待办成功")
    void createTodo_Success() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("新的待办事项");

        TodoVO responseVO = createTestTodoVO();
        when(todoService.createTodo(any(), any())).thenReturn(responseVO);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.title").value("测试待办"));
    }

    @Test
    @DisplayName("创建待办-标题为空")
    void createTodo_EmptyTitle() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("");

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("创建待办-标题过长")
    void createTodo_TitleTooLong() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("a".repeat(201));

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("删除待办成功")
    void deleteTodo_Success() throws Exception {
        mockMvc.perform(delete("/todos/1709800000000_abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }

    @Test
    @DisplayName("清除已完成待办成功")
    void clearCompleted_Success() throws Exception {
        mockMvc.perform(delete("/todos/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }
}