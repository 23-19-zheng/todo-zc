package com.todo.facade.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 同步结果响应
 */
@Data
@ApiModel(description = "同步结果响应")
public class SyncResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "同步成功数量")
    private Integer syncedCount;

    @ApiModelProperty(value = "同步后的待办列表")
    private List<TodoVO> todos;
}
