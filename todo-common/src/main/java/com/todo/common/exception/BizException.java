package com.todo.common.exception;

import com.todo.common.enums.ResponseCodeEnum;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BizException extends RuntimeException {

    private Integer code;
    private String message;

    public BizException(String message) {
        super(message);
        this.code = ResponseCodeEnum.FAIL.getCode();
        this.message = message;
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(ResponseCodeEnum responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public BizException(ResponseCodeEnum responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
        this.message = message;
    }
}
