package com.todo.facade.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 同步待办请求
 */
@Data
@ApiModel(description = "同步待办请求")
public class SyncTodoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "同步操作列表", required = true)
    @NotEmpty(message = "操作列表不能为空")
    @Valid
    private List<SyncAction> actions;

    /**
     * 同步操作
     */
    @Data
    @ApiModel(description = "同步操作")
    public static class SyncAction implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "操作类型: create/update/toggle/delete/clearCompleted", required = true)
        @NotBlank(message = "操作类型不能为空")
        @Pattern(regexp = "^(create|update|toggle|delete|clearCompleted)$",
                message = "操作类型必须是 create/update/toggle/delete/clearCompleted 之一")
        private String type;

        @ApiModelProperty(value = "待办ID (create不需要)")
        private String id;

        @ApiModelProperty(value = "操作数据")
        @Valid
        private SyncActionData data;
    }

    /**
     * 同步操作数据
     */
    @Data
    @ApiModel(description = "同步操作数据")
    public static class SyncActionData implements Serializable {

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "待办标题")
        @Size(max = 200, message = "待办标题长度不能超过200个字符")
        private String title;

        @ApiModelProperty(value = "是否完成")
        private Boolean completed;

        @ApiModelProperty(value = "完成时间戳")
        private Long completedAt;

        @ApiModelProperty(value = "创建时间戳")
        private Long createdAt;
    }
}
