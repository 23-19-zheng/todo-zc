package com.todo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待办筛选条件枚举
 */
@Getter
@AllArgsConstructor
public enum FilterEnum {

    ALL("all", "全部"),
    ACTIVE("active", "未完成"),
    COMPLETED("completed", "已完成");

    private final String value;
    private final String description;

    public static FilterEnum fromValue(String value) {
        if (value == null) {
            return ALL;
        }
        for (FilterEnum filter : values()) {
            if (filter.getValue().equalsIgnoreCase(value)) {
                return filter;
            }
        }
        return ALL;
    }
}
