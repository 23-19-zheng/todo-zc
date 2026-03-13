package com.todo.facade.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 保存筛选偏好请求
 */
@Data
@ApiModel(description = "保存筛选偏好请求")
public class SaveFilterPreferenceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "筛选条件: all/active/completed", required = true)
    @NotBlank(message = "筛选条件不能为空")
    @Pattern(regexp = "^(all|active|completed)$", message = "筛选条件必须是 all/active/completed 之一")
    private String filter;
}
