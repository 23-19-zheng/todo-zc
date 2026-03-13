package com.todo.facade.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 清除已完成待办响应
 */
@Data
@ApiModel(description = "清除已完成待办响应")
public class ClearCompletedVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "清除数量")
    private Integer clearedCount;
}
