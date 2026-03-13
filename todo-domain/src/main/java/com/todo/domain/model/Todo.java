package com.todo.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 待办事项领域模型
 */
@Data
public class Todo {

    /**
     * 待办ID(格式:{timestamp}_{random})
     */
    private String id;

    /**
     * 待办标题
     */
    private String title;

    /**
     * 完成状态
     */
    private Boolean completed;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 完成时间
     */
    private LocalDateTime completedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 生成待办ID
     */
    public static String generateId() {
        long timestamp = System.currentTimeMillis();
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + "_" + random;
    }

    /**
     * 标记为完成
     */
    public void markAsCompleted() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * 标记为未完成
     */
    public void markAsActive() {
        this.completed = false;
        this.completedAt = null;
    }
}
