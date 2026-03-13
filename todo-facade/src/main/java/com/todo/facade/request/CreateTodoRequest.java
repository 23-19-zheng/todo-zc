package com.todo.facade.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 创建待办请求
 */
@Data
@ApiModel(description = "创建待办请求")
public class CreateTodoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "待办标题", required = true)
    @NotBlank(message = "待办标题不能为空")
    @Size(min = 1, max = 200, message = "待办标题长度必须在1-200个字符之间")
    private String title;
}
