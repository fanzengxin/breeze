/*
 Navicat Premium Data Transfer

 Source Server         : 100
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : 192.168.2.100:3306
 Source Schema         : breeze

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 18/12/2019 18:43:31
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
  `dept_type` int(2) NULL DEFAULT NULL COMMENT '部门类型',
  `parent_dept_id` int(10) NULL DEFAULT NULL COMMENT '上级部门id',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_type` int(2) NULL DEFAULT NULL COMMENT '菜单类型',
  `menu_icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `menu_permission` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单权限',
  `menu_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单地址',
  `menu_open_way` int(2) NULL DEFAULT NULL COMMENT '打开方式',
  `parent_menu_id` int(10) NULL DEFAULT NULL COMMENT '父级菜单id',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_type` int(2) NULL DEFAULT NULL COMMENT '角色类型',
  `parent_role_type` int(10) NULL DEFAULT NULL COMMENT '父级角色id',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_permisson_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permisson_rel`;
CREATE TABLE `sys_role_permisson_rel`  (
  `id` int(10) NOT NULL COMMENT '主键',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色id',
  `permisson_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限代码',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '随机盐',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `birthday` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生日',
  `group` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户组',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '用户状态',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '删除标记',
  `create_id` int(11) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(11) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  `password_update_time` bigint(13) NULL DEFAULT NULL COMMENT '密码修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '2', 'admin', '6813161715e0900895798fc0d35ec238', 'd460c63702f2b6b9', 1, '18511113333', '123@qq.com', '1990-04-23', '0', 0, 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES (5, '3', 'test', '6813161715e0900895798fc0d35ec238', '5798fc0d35ec238e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_rel`;
CREATE TABLE `sys_user_role_rel`  (
  `id` int(10) NOT NULL COMMENT '主键',
  `user_id` int(10) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色id',
  `create_id` int(10) NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` bigint(13) NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` int(10) NULL DEFAULT NULL COMMENT '修改者id',
  `update_time` bigint(13) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
