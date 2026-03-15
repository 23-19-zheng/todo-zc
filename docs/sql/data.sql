-- ========================================
-- TODO项目数据库DML - 测试数据
-- 数据库: todo_db
-- ========================================

USE todo_db;

-- ----------------------------------------
-- 测试用户数据
-- 密码: 123456 的BCrypt加密值
-- ----------------------------------------
INSERT INTO t_user (username, password) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi'),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi');

-- ----------------------------------------
-- 测试待办事项数据
-- ----------------------------------------
INSERT INTO t_todo (id, title, completed, user_id, completed_at) VALUES
('1710000000001_abc', '学习Spring Boot', 0, 1, NULL),
('1710000000002_def', '完成TODO项目', 0, 1, NULL),
('1710000000003_ghi', '编写单元测试', 1, 1, '2024-03-10 10:00:00'),
('1710000000004_jkl', '部署到生产环境', 0, 1, NULL),
('1710000000005_mno', '学习Redis缓存', 0, 2, NULL);

-- ----------------------------------------
-- 用户偏好数据
-- ----------------------------------------
INSERT INTO t_user_preference (user_id, filter) VALUES
(1, 'all'),
(2, 'active');