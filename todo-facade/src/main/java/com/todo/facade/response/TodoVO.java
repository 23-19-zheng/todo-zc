package com.todo.facade.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 待办响应对象
 */
@Data
@ApiModel(description = "待办事项")
public class TodoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "待办ID")
    private String id;

    @ApiModelProperty(value = "待办标题")
    private String title;

    @ApiModelProperty(value = "是否完成")
    private Boolean completed;

    @ApiModelProperty(value = "创建时间戳")
    private Long createdAt;

    @ApiModelProperty(value = "完成时间戳")
    private Long completedAt;
}
