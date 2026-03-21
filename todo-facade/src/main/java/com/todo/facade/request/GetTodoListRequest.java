package com.todo.facade.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 获取待办列表请求
 */
@Data
@ApiModel(description = "获取待办列表请求")
public class GetTodoListRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "筛选条件: all/active/completed")
    private String filter;

    @ApiModelProperty(value = "页码")
    private Integer page = 1;

    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 50;
}