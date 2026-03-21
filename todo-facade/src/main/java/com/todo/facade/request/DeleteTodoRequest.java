package com.todo.facade.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 删除待办请求
 */
@Data
@ApiModel(description = "删除待办请求")
public class DeleteTodoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "待办ID", required = true)
    @NotBlank(message = "待办ID不能为空")
    private String id;
}