package com.todo.facade.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 待办列表响应
 */
@Data
@ApiModel(description = "待办列表响应")
public class TodoListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "待办列表")
    private List<TodoVO> list;

    @ApiModelProperty(value = "总数")
    private Long total;

    @ApiModelProperty(value = "未完成数")
    private Long active;

    @ApiModelProperty(value = "已完成数")
    private Long completed;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;
}
