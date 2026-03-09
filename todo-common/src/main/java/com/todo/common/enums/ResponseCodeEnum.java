package com.todo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "系统异常");

    private final Integer code;
    private final String message;

    public static ResponseCodeEnum getByCode(Integer code) {
        for (ResponseCodeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return FAIL;
    }
}
