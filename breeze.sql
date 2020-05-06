/*
 Navicat Premium Data Transfer

 Source Server         : 本地DockerMariaDB
 Source Server Type    : MariaDB
 Source Server Version : 100412
 Source Host           : localhost:3306
 Source Schema         : breeze

 Target Server Type    : MariaDB
 Target Server Version : 100412
 File Encoding         : 65001

 Date: 06/05/2020 17:38:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '全名称',
  `dept_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门类型',
  `parent_id` int(10) NULL DEFAULT NULL COMMENT '上级部门id',
  `parent_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所有上级部门id集合',
  `sort_no` int(10) NULL DEFAULT NULL COMMENT '排序号',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '系统', '系统部门', '1', 0, NULL, 10, NULL, 0, 1, 1587209177669);
INSERT INTO `sys_dept` VALUES (3, '测试1', '测试部门1', '1', 0, NULL, 30, NULL, 0, 1, 1587188628807);
INSERT INTO `sys_dept` VALUES (4, '测试', '测试部门', '2', 1, ',1,', 10, 1, 1587372428808, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `dict_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '字典编码',
  `dict_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `dict_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典项描述',
  `dict_type` tinyint(1) NULL DEFAULT NULL COMMENT '字典类型，0=字典项，1=字典值',
  `dict_parent` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序字段',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'SYS_GENDER', NULL, '性别描述', 0, '0', 10, NULL, 1, 1588746336996, 1, 1588755538445);
INSERT INTO `sys_dict` VALUES (5, '0', '1', '男', 1, 'SYS_GENDER', 10, '男性', 1, 1588754380534, 1, 1588755550310);
INSERT INTO `sys_dict` VALUES (6, '0', '2', '女', 1, 'SYS_GENDER', 20, '女性', 1, 1588755143935, NULL, NULL);
INSERT INTO `sys_dict` VALUES (9, '0', '0', '保密', 1, 'SYS_GENDER', 30, '保密', 1, 1588755309106, NULL, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_type` tinyint(1) NULL DEFAULT NULL COMMENT '菜单类型，1目录，2菜单，3按钮',
  `menu_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `menu_permission` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单权限',
  `menu_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单地址',
  `menu_open_way` tinyint(1) NULL DEFAULT NULL COMMENT '打开方式',
  `keep_alive` tinyint(1) NULL DEFAULT 0 COMMENT '缓存路由，0否，1是',
  `parent_menu_id` int(11) NULL DEFAULT NULL COMMENT '父级菜单id',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `create_id` int(11) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(11) NULL DEFAULT NULL COMMENT '修改者',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 1, 'iconfont icon-xitongguanli', '', '/admin', 1, 0, 0, 10, 1, 1586873897597, 1, 1588140001166);
INSERT INTO `sys_menu` VALUES (2, '菜单管理', 2, 'iconfont icon-liebiao', 'sys_menu_list', '/menu/index', 1, 1, 1, 30, 1, 1586873897597, 1, 1588140030687);
INSERT INTO `sys_menu` VALUES (3, '新增', 3, '', 'sys_menu_add', '', 1, 0, 2, 10, 1, 1586873897597, 1, 1586873897597);
INSERT INTO `sys_menu` VALUES (4, '用户管理', 2, 'iconfont icon-icon_zhanghao', 'sys_user_list', '/user/index', 0, 0, 1, 10, 1, 15868738975974, 1, 1588140017761);
INSERT INTO `sys_menu` VALUES (5, '修改', 3, '', 'sys_menu_edit', '', NULL, 0, 2, 20, 1, 1586873897597, NULL, NULL);
INSERT INTO `sys_menu` VALUES (6, '删除', 3, '', 'sys_menu_del', '', NULL, 0, 2, 30, 1, 1586873897597, NULL, NULL);
INSERT INTO `sys_menu` VALUES (8, '角色管理', 2, 'iconfont icon-hezuohuobanmiyueguanli', 'sys_role_list', '/role/index', 1, 0, 1, 20, 1, 1586873897597, 1, 1588140024232);
INSERT INTO `sys_menu` VALUES (9, '新增', 3, '', 'sys_role_add', '', 0, 0, 8, 10, 1, 1586873897597, 1, 1586873897597);
INSERT INTO `sys_menu` VALUES (10, '修改', 3, '', 'sys_role_edit', '', NULL, 0, 8, 20, 1, 1587003606792, NULL, NULL);
INSERT INTO `sys_menu` VALUES (11, '删除', 3, '', 'sys_role_del', '', NULL, 0, 8, 30, 1, 1587003632848, NULL, NULL);
INSERT INTO `sys_menu` VALUES (12, '分配权限', 3, '', 'sys_role_permission', '', NULL, 0, 8, 40, 1, 1587003658491, NULL, NULL);
INSERT INTO `sys_menu` VALUES (13, '部门管理', 2, 'iconfont icon-web-icon-', 'sys_dept_tree', '/dept/index', 0, 1, 1, 40, 1, 1587183444985, 1, 1588140037452);
INSERT INTO `sys_menu` VALUES (14, '新增', 3, '', 'sys_dept_add', '', NULL, 0, 13, 10, 1, 1587183707960, NULL, NULL);
INSERT INTO `sys_menu` VALUES (15, '修改', 3, '', 'sys_dept_edit', '', NULL, 0, 13, 20, 1, 1587183734115, NULL, NULL);
INSERT INTO `sys_menu` VALUES (16, '删除', 3, '', 'sys_dept_del', '', NULL, 0, 13, 10, 1, 1587183755875, NULL, NULL);
INSERT INTO `sys_menu` VALUES (17, '新增', 3, '', 'sys_user_add', '', NULL, 0, 4, 10, 1, 1587301660034, NULL, NULL);
INSERT INTO `sys_menu` VALUES (18, '修改', 3, '', 'sys_user_edit', '', NULL, 0, 4, 20, 1, 1587301686503, NULL, NULL);
INSERT INTO `sys_menu` VALUES (19, '删除', 3, '', 'sys_user_del', '', NULL, 0, 4, 30, 1, 1587301707912, NULL, NULL);
INSERT INTO `sys_menu` VALUES (20, '系统工具', 1, 'iconfont icon-yuanquyunwei', '', '/tools', 0, 0, 0, 20, 1, 1588056029771, 1, 1588139980682);
INSERT INTO `sys_menu` VALUES (21, '代码生成', 2, 'iconfont icon-weibiaoti46', 'tools_code_generator', '/generator/index', 0, 1, 20, 10, 1, 1588056062239, 1, 1588164731326);
INSERT INTO `sys_menu` VALUES (22, '数据字典', 2, 'iconfont icon-wenjianguanli', 'sys_dict_list', '/dict/index', NULL, 0, 1, 50, 1, 1588326220595, NULL, NULL);
INSERT INTO `sys_menu` VALUES (23, '新增', 3, '', 'sys_dict_add', '', NULL, 0, 22, 10, 1, 1588327796534, NULL, NULL);
INSERT INTO `sys_menu` VALUES (24, '修改', 3, '', 'sys_dict_edit', '', NULL, 0, 22, 20, 1, 1588327830837, NULL, NULL);
INSERT INTO `sys_menu` VALUES (25, '删除', 3, '', 'sys_dict_del', '', NULL, 0, 22, 30, 1, 1588327850394, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `role_type` int(1) NULL DEFAULT NULL COMMENT '角色类型',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `dept_id` int(10) NULL DEFAULT NULL COMMENT '部门id',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '系统管理员', 'SYSTEM_ADMIN', 1, '系统管理员，拥有最高权限', NULL, NULL, NULL, 1, 1586873897597);
INSERT INTO `sys_role` VALUES (6, '测试角色', 'TEST', 1, '测试角色', NULL, 1, 1586873897597, NULL, NULL);
INSERT INTO `sys_role` VALUES (7, '测试1', 'TEST1', 1, '测试1', NULL, 1, 1586873897597, 1, 1586875117536);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色code',
  `permission` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限id',
  PRIMARY KEY (`role_code`, `permission`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dept_add');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dept_del');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dept_edit');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dept_tree');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dict_add');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dict_del');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dict_edit');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_dict_list');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_menu_add');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_menu_del');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_menu_edit');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_menu_list');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_role_add');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_role_del');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_role_edit');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_role_list');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_role_permission');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_user_add');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_user_del');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_user_edit');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'sys_user_list');
INSERT INTO `sys_role_permission` VALUES ('SYSTEM_ADMIN', 'tools_code_generator');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '校验盐',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `birthday` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生日',
  `group` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户组',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '用户状态，0禁用，1启用',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除标记',
  `head_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  `password_update_time` bigint(13) NULL DEFAULT NULL COMMENT '密码修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '系统管理员', 'f01d5a5774ef6f5ec58ad08bdfe52aca', '1234567890', 1, '18512345678', '342652190@qq.com', NULL, NULL, 1, 0, NULL, NULL, 1588055460068, 1, 1588749365242, 1588055460068);

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `dept_id` int(10) NOT NULL COMMENT '部门id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`dept_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(10) NOT NULL COMMENT '用户id',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色code',
  PRIMARY KEY (`user_id`, `role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 'SYSTEM_ADMIN');

SET FOREIGN_KEY_CHECKS = 1;
