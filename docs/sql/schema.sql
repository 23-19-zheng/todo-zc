-- ========================================
-- TODO项目数据库DDL - 表结构定义
-- 数据库: todo_db
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS todo_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE todo_db;

-- ----------------------------------------
-- 用户表
-- ----------------------------------------
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识(0:未删除,1:已删除)',
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------------------
-- 待办事项表
-- ----------------------------------------
CREATE TABLE IF NOT EXISTS t_todo (
    id VARCHAR(50) PRIMARY KEY COMMENT '待办ID',
    title VARCHAR(500) NOT NULL COMMENT '待办标题',
    completed TINYINT(1) DEFAULT 0 COMMENT '完成状态(0:未完成,1:已完成)',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    completed_at DATETIME DEFAULT NULL COMMENT '完成时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识(0:未删除,1:已删除)',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办事项表';

-- ----------------------------------------
-- 用户偏好表
-- ----------------------------------------
CREATE TABLE IF NOT EXISTS t_user_preference (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    filter VARCHAR(20) DEFAULT 'all' COMMENT '筛选条件(all/active/completed)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识(0:未删除,1:已删除)',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户偏好表';