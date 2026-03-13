package com.todo.facade.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户响应对象
 */
@Data
@ApiModel(description = "用户信息")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "创建时间戳")
    private Long createdAt;

    public static UserVO of(Long id, String username, Long createdAt) {
        UserVO vo = new UserVO();
        vo.setId(String.valueOf(id));
        vo.setUsername(username);
        vo.setCreatedAt(createdAt);
        return vo;
    }
}
