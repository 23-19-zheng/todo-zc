package com.todo.domain.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.todo.common.enums.FilterEnum;
import com.todo.domain.model.Todo;
import com.todo.domain.repository.TodoRepository;
import com.todo.infrastructure.entity.TodoEntity;
import com.todo.infrastructure.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 待办仓储实现
 */
@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepository {

    private final TodoMapper todoMapper;

    @Override
    public Optional<Todo> findById(String id) {
        TodoEntity entity = todoMapper.selectById(id);
        return Optional.ofNullable(entity).map(this::toModel);
    }

    @Override
    public Optional<Todo> findByIdAndUserId(String id, Long userId) {
        LambdaQueryWrapper<TodoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TodoEntity::getId, id)
                .eq(TodoEntity::getUserId, userId);
        TodoEntity entity = todoMapper.selectOne(wrapper);
        return Optional.ofNullable(entity).map(this::toModel);
    }

    @Override
    public IPage<Todo> findByUserId(Long userId, String filter, int page, int pageSize) {
        Page<TodoEntity> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<TodoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TodoEntity::getUserId, userId);

        FilterEnum filterEnum = FilterEnum.fromValue(filter);
        if (filterEnum == FilterEnum.ACTIVE) {
            wrapper.eq(TodoEntity::getCompleted, false);
        } else if (filterEnum == FilterEnum.COMPLETED) {
            wrapper.eq(TodoEntity::getCompleted, true);
        }

        wrapper.orderByDesc(TodoEntity::getCreateTime);

        IPage<TodoEntity> entityPage = todoMapper.selectPage(pageParam, wrapper);

        return entityPage.convert(this::toModel);
    }

    @Override
    public long countByUserId(Long userId) {
        LambdaQueryWrapper<TodoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TodoEntity::getUserId, userId);
        return todoMapper.selectCount(wrapper);
    }

    @Override
    public long countCompletedByUserId(Long userId) {
        LambdaQueryWrapper<TodoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TodoEntity::getUserId, userId)
                .eq(TodoEntity::getCompleted, true);
        return todoMapper.selectCount(wrapper);
    }

    @Override
    public long countActiveByUserId(Long userId) {
        LambdaQueryWrapper<TodoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TodoEntity::getUserId, userId)
                .eq(TodoEntity::getCompleted, false);
        return todoMapper.selectCount(wrapper);
    }

    @Override
    public Todo save(Todo todo) {
        TodoEntity entity = toEntity(todo);
        todoMapper.insert(entity);
        todo.setId(entity.getId());
        return todo;
    }

    @Override
    public void update(Todo todo) {
        TodoEntity entity = toEntity(todo);
        todoMapper.updateById(entity);
    }

    @Override
    public void deleteById(String id) {
        todoMapper.deleteById(id);
    }

    @Override
    public int deleteCompletedByUserId(Long userId) {
        return todoMapper.deleteCompletedByUserId(userId);
    }

    @Override
    public void saveAll(List<Todo> todos) {
        List<TodoEntity> entities = todos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        for (TodoEntity entity : entities) {
            todoMapper.insert(entity);
        }
    }

    private Todo toModel(TodoEntity entity) {
        Todo todo = new Todo();
        todo.setId(entity.getId());
        todo.setTitle(entity.getTitle());
        todo.setCompleted(entity.getCompleted());
        todo.setUserId(entity.getUserId());
        todo.setCompletedAt(entity.getCompletedAt());
        todo.setCreatedAt(entity.getCreateTime());
        return todo;
    }

    private TodoEntity toEntity(Todo todo) {
        TodoEntity entity = new TodoEntity();
        entity.setId(todo.getId());
        entity.setTitle(todo.getTitle());
        entity.setCompleted(todo.getCompleted() != null ? todo.getCompleted() : false);
        entity.setUserId(todo.getUserId());
        entity.setCompletedAt(todo.getCompletedAt());
        return entity;
    }
}
