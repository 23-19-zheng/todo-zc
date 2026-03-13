package com.todo.facade.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 筛选偏好响应
 */
@Data
@ApiModel(description = "筛选偏好响应")
public class FilterPreferenceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "筛选条件")
    private String filter;
}
