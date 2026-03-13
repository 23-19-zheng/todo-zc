package com.todo.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 待办事项实体
 */
@Data
@TableName("t_todo")
public class TodoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待办ID(格式:{timestamp}_{random})
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 待办标题
     */
    private String title;

    /**
     * 完成状态(0:未完成,1:已完成)
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer deleted;
}
