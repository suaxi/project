SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` int(11) NULL DEFAULT NULL COMMENT '上级部门ID',
  `sub_count` int(5) NULL DEFAULT 0 COMMENT '子部门数目',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '状态',
  `sort` int(5) NULL DEFAULT 999 COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 4, 1, '研发部', 1, 1, '2022-10-08 22:07:30', 'admin', '2025-05-09 23:31:48', 'admin', 0);
INSERT INTO `sys_dept` VALUES (2, 4, 0, '运维部', 1, 1, '2022-10-08 22:07:30', 'admin', '2025-10-10 16:00:22', 'admin', 0);
INSERT INTO `sys_dept` VALUES (3, 7, 0, '测试部', 1, 1, '2022-10-08 22:07:30', 'admin', '2025-10-10 11:59:21', 'admin', 0);
INSERT INTO `sys_dept` VALUES (4, 0, 1, '华南分部', 1, 1, '2022-10-08 22:07:30', 'admin', '2023-04-13 22:15:14', 'admin', 0);
INSERT INTO `sys_dept` VALUES (5, 0, 1, '华北分部', 1, 1, '2022-10-08 22:07:30', 'admin', '2025-10-13 17:34:13', 'admin', 0);
INSERT INTO `sys_dept` VALUES (6, 5, 0, 'UI部门', 1, 10, '2022-10-08 22:07:30', 'admin', '2025-10-13 17:34:13', 'admin', 0);
INSERT INTO `sys_dept` VALUES (7, 1, 1, '研发一组', 1, 1, '2022-10-08 22:07:30', 'admin', '2025-05-10 19:54:09', 'admin', 0);
INSERT INTO `sys_dept` VALUES (8, 1, 2, '研发二组', 1, 11, '2022-10-08 22:07:30', 'admin', '2025-10-10 16:24:21', 'admin', 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'user_status', '用户状态', '2022-10-08 22:07:30', NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (2, 'dept_status', '部门状态', '2022-10-08 22:07:30', NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (3, 'job_status', '岗位状态', '2022-10-08 22:07:30', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_detail`;
CREATE TABLE `sys_dict_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dict_id` int(11) NULL DEFAULT NULL COMMENT '字典id',
  `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典标签',
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典值',
  `sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典详情' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO `sys_dict_detail` VALUES (1, 1, '激活', 'true', 1, '2022-10-08 22:07:30', NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (2, 1, '禁用', 'false', 2, '2022-10-08 22:07:30', NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (3, 2, '启用', 'true', 1, '2022-10-08 22:07:30', NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (4, 2, '停用', 'false', 2, '2022-10-08 22:07:30', NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (5, 3, '启用', 'true', 1, '2022-10-08 22:07:30', NULL, '2025-10-11 22:27:07', 'admin');
INSERT INTO `sys_dict_detail` VALUES (6, 3, '停用', 'false', 2, '2022-10-08 22:07:30', NULL, '2022-12-13 22:42:41', 'admin');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '人事专员', 1, 1, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_job` VALUES (2, '产品经理', 2, 1, '2022-10-08 22:07:30', 'admin', '2022-12-05 21:36:14', 'admin', 0);
INSERT INTO `sys_job` VALUES (3, '全栈开发', 3, 1, '2022-10-08 22:07:30', 'admin', '2023-05-05 11:33:43', 'admin', 0);
INSERT INTO `sys_job` VALUES (4, '软件测试', 4, 1, '2022-10-08 22:07:30', 'admin', '2025-10-13 17:34:31', 'admin', 0);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `log_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数',
  `request_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求ip',
  `time` bigint(20) NULL DEFAULT NULL COMMENT '耗时',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `browser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `exception_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` int(11) NULL DEFAULT NULL COMMENT '上级菜单ID',
  `sub_count` int(5) NULL DEFAULT 0 COMMENT '子菜单数目',
  `type` int(11) NULL DEFAULT NULL COMMENT '菜单类型',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单标题',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `i_frame` int(1) NULL DEFAULT 0 COMMENT '是否外链',
  `cache` int(1) NOT NULL DEFAULT 0 COMMENT '缓存',
  `hidden` int(1) NOT NULL DEFAULT 0 COMMENT '隐藏',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `sort` int(5) NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_title`(`title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 6, 0, '系统管理', NULL, NULL, 'system', 'system', 0, 0, 0, NULL, 1, '2022-10-08 22:07:30', 'admin', '2025-10-15 21:35:24', 'admin', 0);
INSERT INTO `sys_menu` VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 'user', 'user', 0, 0, 0, 'user:list', 2, '2022-10-08 22:07:30', 'admin', '2025-10-15 20:32:21', 'admin', 0);
INSERT INTO `sys_menu` VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 'peoples', 'role', 0, 0, 0, 'roles:list', 3, '2022-10-08 22:07:30', 'admin', '2025-10-11 17:04:44', 'admin', 0);
INSERT INTO `sys_menu` VALUES (4, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 'list', 'menu', 0, 0, 0, 'menu:list', 5, '2022-10-08 22:07:30', 'admin', '2025-10-11 17:05:52', 'admin', 0);
INSERT INTO `sys_menu` VALUES (5, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 'tree', 'dept', 0, 0, 0, 'dept:list', 6, '2022-10-08 22:07:30', 'admin', '2025-10-11 17:06:20', 'admin', 0);
INSERT INTO `sys_menu` VALUES (6, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 'tree-table', 'job', 0, 0, 0, 'job:list', 7, '2022-10-08 22:07:30', 'admin', '2025-10-11 17:06:57', 'admin', 0);
INSERT INTO `sys_menu` VALUES (7, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 'dict', 'dict', 0, 0, 0, 'dict:list', 8, '2022-10-08 22:07:30', 'admin', '2025-10-11 17:07:13', 'admin', 0);
INSERT INTO `sys_menu` VALUES (8, 0, 2, 0, '系统监控', NULL, NULL, 'monitor', 'monitor', 0, 0, 0, NULL, 10, '2022-10-08 22:07:30', 'admin', '2025-10-15 21:13:30', 'admin', 0);
INSERT INTO `sys_menu` VALUES (9, 8, 0, 1, '操作日志', 'OperationLog', 'monitor/operationLog/index', 'log', 'operation-log', 0, 1, 0, NULL, 11, '2022-10-08 22:07:30', 'admin', '2025-10-12 23:26:37', 'admin', 0);
INSERT INTO `sys_menu` VALUES (10, 0, 2, 0, '多级菜单', NULL, '', 'tree-table', 'nested', 0, 0, 0, NULL, 900, '2022-10-08 22:07:30', 'admin', '2025-10-14 20:59:15', 'admin', 0);
INSERT INTO `sys_menu` VALUES (11, 10, 1, 0, '二级菜单 - 1', 'Menu1', 'nested/menu1', 'list', 'menu2-1', 0, 0, 0, NULL, 10, '2022-10-08 22:07:30', 'admin', '2025-10-14 21:00:53', 'admin', 0);
INSERT INTO `sys_menu` VALUES (12, 10, 0, 1, '二级菜单 - 2', 'Menu2', 'nested/index', 'list', 'menu2-2', 0, 0, 0, NULL, 20, '2022-10-08 22:07:30', 'admin', '2025-10-14 21:01:00', 'admin', 0);
INSERT INTO `sys_menu` VALUES (13, 2, 0, 2, '用户新增', NULL, '', '', '', 0, 0, 0, 'user:add', 2, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 2, 0, 2, '用户编辑', NULL, '', '', '', 0, 0, 0, 'user:edit', 3, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 2, 0, 2, '用户删除', NULL, '', '', '', 0, 0, 0, 'user:del', 4, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 3, 0, 2, '角色创建', NULL, '', '', '', 0, 0, 0, 'roles:add', 2, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 3, 0, 2, '角色修改', NULL, '', '', '', 0, 0, 0, 'roles:edit', 3, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 3, 0, 2, '角色删除', NULL, '', '', '', 0, 0, 0, 'roles:del', 4, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 4, 0, 2, '菜单新增', NULL, '', '', '', 0, 0, 0, 'menu:add', 2, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 4, 0, 2, '菜单编辑', NULL, '', '', '', 0, 0, 0, 'menu:edit', 3, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 4, 0, 2, '菜单删除', NULL, '', '', '', 0, 0, 0, 'menu:del', 4, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 5, 0, 2, '部门新增', NULL, '', '', '', 0, 0, 0, 'dept:add', 2, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 5, 0, 2, '部门编辑', NULL, '', '', '', 0, 0, 0, 'dept:edit', 3, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 5, 0, 2, '部门删除', NULL, '', '', '', 0, 0, 0, 'dept:del', 4, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 6, 0, 2, '岗位新增', NULL, '', '', '', 0, 0, 0, 'job:add', 2, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 6, 0, 2, '岗位编辑', NULL, '', '', '', 0, 0, 0, 'job:edit', 3, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (27, 6, 0, 2, '岗位删除', NULL, '', '', '', 0, 0, 0, 'job:del', 4, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (28, 7, 0, 2, '字典新增', NULL, '', '', '', 0, 0, 0, 'dict:add', 2, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (29, 7, 0, 2, '字典编辑', NULL, '', '', '', 0, 0, 0, 'dict:edit', 3, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (30, 7, 0, 2, '字典删除', NULL, '', '', '', 0, 0, 0, 'dict:del', 4, '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (31, 11, 0, 1, '三级菜单 - 1', 'Menu3', 'nested/menu/index', 'list', 'menu3', 0, 0, 0, NULL, 10, '2022-10-08 22:07:30', 'admin', '2025-10-14 20:57:52', 'admin', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `level` int(255) NULL DEFAULT NULL COMMENT '角色级别',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `data_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据权限',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, '-', '全部', '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (2, '普通用户', 2, '-', '本级', '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (3, '测试角色', 3, '测试', '全部', '2022-10-08 22:07:30', 'admin', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色部门关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (1, 1, 5);
INSERT INTO `sys_role_dept` VALUES (2, 1, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 8, 1);
INSERT INTO `sys_role_menu` VALUES (2, 9, 1);
INSERT INTO `sys_role_menu` VALUES (3, 12, 1);
INSERT INTO `sys_role_menu` VALUES (4, 31, 1);
INSERT INTO `sys_role_menu` VALUES (5, 11, 1);
INSERT INTO `sys_role_menu` VALUES (6, 10, 1);
INSERT INTO `sys_role_menu` VALUES (7, 5, 1);
INSERT INTO `sys_role_menu` VALUES (8, 3, 1);
INSERT INTO `sys_role_menu` VALUES (9, 21, 1);
INSERT INTO `sys_role_menu` VALUES (10, 6, 1);
INSERT INTO `sys_role_menu` VALUES (11, 1, 1);
INSERT INTO `sys_role_menu` VALUES (12, 2, 1);
INSERT INTO `sys_role_menu` VALUES (13, 7, 1);
INSERT INTO `sys_role_menu` VALUES (14, 19, 1);
INSERT INTO `sys_role_menu` VALUES (15, 28, 1);
INSERT INTO `sys_role_menu` VALUES (16, 13, 1);
INSERT INTO `sys_role_menu` VALUES (17, 20, 1);
INSERT INTO `sys_role_menu` VALUES (18, 29, 1);
INSERT INTO `sys_role_menu` VALUES (19, 27, 1);
INSERT INTO `sys_role_menu` VALUES (20, 23, 1);
INSERT INTO `sys_role_menu` VALUES (21, 24, 1);
INSERT INTO `sys_role_menu` VALUES (22, 4, 1);
INSERT INTO `sys_role_menu` VALUES (23, 16, 1);
INSERT INTO `sys_role_menu` VALUES (24, 26, 1);
INSERT INTO `sys_role_menu` VALUES (25, 17, 1);
INSERT INTO `sys_role_menu` VALUES (26, 14, 1);
INSERT INTO `sys_role_menu` VALUES (27, 22, 1);
INSERT INTO `sys_role_menu` VALUES (28, 18, 1);
INSERT INTO `sys_role_menu` VALUES (29, 25, 1);
INSERT INTO `sys_role_menu` VALUES (30, 30, 1);
INSERT INTO `sys_role_menu` VALUES (31, 15, 1);
INSERT INTO `sys_role_menu` VALUES (39, 2, 2);
INSERT INTO `sys_role_menu` VALUES (40, 3, 2);
INSERT INTO `sys_role_menu` VALUES (41, 4, 2);
INSERT INTO `sys_role_menu` VALUES (42, 5, 2);
INSERT INTO `sys_role_menu` VALUES (43, 6, 2);
INSERT INTO `sys_role_menu` VALUES (44, 7, 2);
INSERT INTO `sys_role_menu` VALUES (45, 1, 2);
INSERT INTO `sys_role_menu` VALUES (46, 12, 2);
INSERT INTO `sys_role_menu` VALUES (47, 31, 2);
INSERT INTO `sys_role_menu` VALUES (48, 11, 2);
INSERT INTO `sys_role_menu` VALUES (49, 10, 2);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `avatar_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像真实路径',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `is_admin` int(1) NOT NULL DEFAULT 0 COMMENT '是否为admin账号',
  `enabled` int(1) NULL DEFAULT 0 COMMENT '状态（1：启用，0：禁用）',
  `pwd_reset_time` datetime NULL DEFAULT NULL COMMENT '密码修改时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username`) USING BTREE,
  UNIQUE INDEX `uniq_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 2, 'admin', '管理员', '男', '18888888888', 'admin@qq.com', 'avatar-20200806032259161.png', '/Users/jie/Documents/work/me/admin/eladmin/~/avatar/avatar-20200806032259161.png', '$2a$10$W00k5XX2yj/l7UthtWMUgeDAKQV7ULX3odvDx2TYKpcogqPm0Tn5u', 1, 1, NULL, '2022-10-08 22:07:30', 'admin', '2025-10-15 22:54:10', 'admin', 0);
INSERT INTO `sys_user` VALUES (2, 2, 'test', '测试用户', '男', '19999999999', '231@qq.com', NULL, NULL, '$2a$10$W00k5XX2yj/l7UthtWMUgeDAKQV7ULX3odvDx2TYKpcogqPm0Tn5u', 0, 0, NULL, '2022-10-08 22:07:30', 'admin', '2025-10-15 23:02:54', 'admin', 0);
INSERT INTO `sys_user` VALUES (3, 1, '孙笑川', '带带大师兄', '男', '13988888888', '654321@qq.com', NULL, NULL, '$2a$10$r/BsX68Q6ZgiM1Qx0/UlAusU0xHwiTPqP1zCaXLBZcUJGZl/7BZ9C', 0, 1, NULL, '2022-10-08 22:07:30', 'admin', '2025-10-15 22:53:42', 'admin', 0);
INSERT INTO `sys_user` VALUES (4, 7, '刘波', '药水哥', '男', '13988888888', '123456@qq.com', NULL, NULL, '$2a$10$HMC37QtDVWZ6UatfQgT5wu1JDp53//LVBqnanCdTXgBmygT9DOCqq', 0, 1, NULL, '2022-10-26 21:43:25', 'admin', '2025-10-15 22:52:01', 'admin', 0);

-- ----------------------------
-- Table structure for sys_user_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_job`;
CREATE TABLE `sys_user_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `job_id` int(11) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户岗位关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user_job
-- ----------------------------
INSERT INTO `sys_user_job` VALUES (3, 4, 2);
INSERT INTO `sys_user_job` VALUES (4, 3, 3);
INSERT INTO `sys_user_job` VALUES (5, 1, 1);
INSERT INTO `sys_user_job` VALUES (6, 2, 2);
INSERT INTO `sys_user_job` VALUES (7, 2, 3);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 4, 2);
INSERT INTO `sys_user_role` VALUES (3, 3, 2);
INSERT INTO `sys_user_role` VALUES (4, 1, 1);
INSERT INTO `sys_user_role` VALUES (5, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;
