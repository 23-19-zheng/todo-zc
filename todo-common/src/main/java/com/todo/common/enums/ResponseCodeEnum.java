package com.todo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    // 成功
    SUCCESS(1000, "success"),

    // 通用错误码 1001-1006
    RESOURCE_NOT_FOUND(1001, "资源不存在"),
    PARAM_VALIDATION_FAILED(1002, "参数验证失败"),
    UNAUTHORIZED(1003, "未授权/Token过期"),
    FORBIDDEN(1004, "禁止访问"),
    INTERNAL_ERROR(1005, "服务器内部错误"),
    RATE_LIMIT_EXCEEDED(1006, "请求频率超限"),

    // 认证错误码 2001-2003
    USERNAME_EXISTS(2001, "用户名已存在"),
    PASSWORD_ERROR(2002, "密码错误"),
    USER_NOT_FOUND(2003, "用户不存在"),

    // 兼容旧错误码
    FAIL(1005, "操作失败"),
    PARAM_ERROR(1002, "参数错误"),
    NOT_FOUND(1001, "资源不存在"),
    SYSTEM_ERROR(1005, "系统异常");

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
