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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 4, 1, '研发部', 1, 1, '2019-03-25 09:15:32', '1', '2020-08-02 14:48:47', NULL, 0);
INSERT INTO `sys_dept` VALUES (2, 4, 0, '运维部', 1, 1, '2019-03-25 09:20:44', '1', '2020-05-17 14:27:27', NULL, 0);
INSERT INTO `sys_dept` VALUES (3, 7, 0, '测试部', 1, 1, '2019-03-25 09:52:18', '1', '2020-06-08 11:59:21', NULL, 0);
INSERT INTO `sys_dept` VALUES (4, NULL, 1, '华南分部', 1, 1, '2019-03-25 11:04:50', '1', '2023-04-13 22:15:14', NULL, 0);
INSERT INTO `sys_dept` VALUES (5, NULL, 2, '华北分部', 1, 1, '2019-03-25 11:04:53', '1', '2020-05-14 12:54:00', '1', 0);
INSERT INTO `sys_dept` VALUES (6, 5, 0, 'UI部门', 1, 1, '2020-05-13 22:56:53', '1', '2020-05-14 12:54:13', '1', 0);
INSERT INTO `sys_dept` VALUES (7, 1, 0, '研发一组', 1, 1, '2020-08-02 14:49:07', '1', '2020-08-02 14:49:07', '1', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'user_status', '用户状态', '2019-10-27 20:31:36', NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (2, 'dept_status', '部门状态', '2019-10-27 20:31:36', NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (3, 'job_status', '岗位状态', '2019-10-27 20:31:36', NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (6, 'test', '测试123', '2022-12-12 20:45:41', '1', '2022-12-12 20:45:45', '1');

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典详情' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict_detail
-- ----------------------------
INSERT INTO `sys_dict_detail` VALUES (1, 1, '激活', 'true', 1, '2019-10-27 20:31:36', NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (2, 1, '禁用', 'false', 2, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (3, 2, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (4, 2, '停用', 'false', 2, '2019-10-27 20:31:36', NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (5, 3, '启用', 'true', 1, NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_detail` VALUES (6, 3, '停用', 'false', 2, '2019-10-27 20:31:36', NULL, '2022-12-13 22:42:41', '1');
INSERT INTO `sys_dict_detail` VALUES (9, 6, '测试2', 'test2', 1, '2022-12-13 22:42:14', '1', '2022-12-13 22:57:57', '1');

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
INSERT INTO `sys_job` VALUES (1, '人事专员', 1, 1, '2019-03-29 14:52:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_job` VALUES (2, '产品经理', 2, 1, '2019-03-29 14:55:51', NULL, '2022-12-05 21:36:14', '1', 0);
INSERT INTO `sys_job` VALUES (3, '全栈开发', 3, 1, '2019-03-31 13:39:30', NULL, '2020-05-05 11:33:43', '1', 0);
INSERT INTO `sys_job` VALUES (4, '软件测试', 4, 1, '2019-03-31 13:39:43', NULL, '2022-12-05 21:31:58', '1', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = COMPACT;

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
  `update_user` varchar(50) NULL DEFAULT NULL COMMENT '更新人',
  `is_delete` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_title`(`title`) USING BTREE,
  UNIQUE INDEX `uniq_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, NULL, 7, 0, '系统管理', NULL, NULL, 'system', 'system', 0, 0, 0, NULL, 1, '2018-12-18 15:11:29', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (2, 1, 3, 1, '用户管理', 'User', 'system/user/index', 'peoples', 'user', 0, 0, 0, 'user:list', 2, '2018-12-18 15:14:44', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (3, 1, 3, 1, '角色管理', 'Role', 'system/role/index', 'role', 'role', 0, 0, 0, 'roles:list', 3, '2018-12-18 15:16:07', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (5, 1, 3, 1, '菜单管理', 'Menu', 'system/menu/index', 'menu', 'menu', 0, 0, 0, 'menu:list', 5, '2018-12-18 15:17:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (6, NULL, 4, 0, '系统监控', NULL, NULL, 'monitor', 'monitor', 0, 0, 0, NULL, 10, '2018-12-18 15:17:48', NULL, '2022-12-04 12:58:42', NULL, 0);
INSERT INTO `sys_menu` VALUES (7, 6, 0, 1, '操作日志', 'Log', 'monitor/log/index', 'log', 'logs', 0, 1, 0, NULL, 11, '2018-12-18 15:18:26', NULL, '2020-06-06 13:11:57', NULL, 0);
INSERT INTO `sys_menu` VALUES (9, 6, 0, 1, 'SQL监控', 'Sql', 'monitor/sql/index', 'sqlMonitor', 'druid', 0, 0, 0, NULL, 18, '2018-12-18 15:19:34', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, NULL, 5, 0, '组件管理', NULL, NULL, 'zujian', 'components', 0, 0, 0, NULL, 50, '2018-12-19 13:38:16', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, 10, 0, 1, '图标库', 'Icons', 'components/icons/index', 'icon', 'icon', 0, 0, 0, NULL, 51, '2018-12-19 13:38:49', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, 36, 0, 1, '邮件工具', 'Email', 'tools/email/index', 'email', 'email', 0, 0, 0, NULL, 35, '2018-12-27 10:13:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, 10, 0, 1, '富文本', 'Editor', 'components/Editor', 'fwb', 'tinymce', 0, 0, 0, NULL, 52, '2018-12-27 11:58:25', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 36, 3, 1, '存储管理', 'Storage', 'tools/storage/index', 'qiniu', 'storage', 0, 0, 0, 'storage:list', 34, '2018-12-31 11:12:15', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 36, 0, 1, '支付宝工具', 'AliPay', 'tools/aliPay/index', 'alipay', 'aliPay', 0, 0, 0, NULL, 37, '2018-12-31 14:52:38', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, NULL, 2, 0, '多级菜单', NULL, '', 'menu', 'nested', 0, 0, 0, NULL, 900, '2019-01-04 16:22:03', NULL, '2020-06-21 17:27:35', NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 21, 2, 0, '二级菜单1', NULL, '', 'menu', 'menu1', 0, 0, 0, NULL, 999, '2019-01-04 16:23:29', NULL, '2020-06-21 17:27:20', NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 21, 0, 1, '二级菜单2', NULL, 'nested/menu2/index', 'menu', 'menu2', 0, 0, 0, NULL, 999, '2019-01-04 16:23:57', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 22, 0, 1, '三级菜单1', 'Test', 'nested/menu1/menu1-1', 'menu', 'menu1-1', 0, 0, 0, NULL, 999, '2019-01-04 16:24:48', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (27, 22, 0, 1, '三级菜单2', NULL, 'nested/menu1/menu1-2', 'menu', 'menu1-2', 0, 0, 0, NULL, 999, '2019-01-07 17:27:32', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (28, 1, 3, 1, '任务调度', 'Timing', 'system/timing/index', 'timing', 'timing', 0, 0, 0, 'timing:list', 999, '2019-01-07 20:34:40', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (30, 36, 0, 1, '代码生成', 'GeneratorIndex', 'generator/index', 'dev', 'generator', 0, 1, 0, NULL, 32, '2019-01-11 15:45:55', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 6, 0, 1, '异常日志', 'ErrorLog', 'monitor/log/errorLog', 'error', 'errorLog', 0, 0, 0, NULL, 12, '2019-01-13 13:49:03', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (33, 10, 0, 1, 'Markdown', 'Markdown', 'components/MarkDown', 'markdown', 'markdown', 0, 0, 0, NULL, 53, '2019-03-08 13:46:44', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (34, 10, 0, 1, 'Yaml编辑器', 'YamlEdit', 'components/YamlEdit', 'dev', 'yaml', 0, 0, 0, NULL, 54, '2019-03-08 15:49:40', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (35, 1, 3, 1, '部门管理', 'Dept', 'system/dept/index', 'dept', 'dept', 0, 0, 0, 'dept:list', 6, '2019-03-25 09:46:00', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (36, NULL, 7, 0, '系统工具', NULL, '', 'sys-tools', 'sys-tools', 0, 0, 0, NULL, 30, '2019-03-29 10:57:35', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (37, 1, 3, 1, '岗位管理', 'Job', 'system/job/index', 'Steve-Jobs', 'job', 0, 0, 0, 'job:list', 7, '2019-03-29 13:51:18', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (38, 36, 0, 1, '接口文档', 'Swagger', 'tools/swagger/index', 'swagger', 'swagger2', 0, 0, 0, NULL, 36, '2019-03-29 19:57:53', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (39, 1, 3, 1, '字典管理', 'Dict', 'system/dict/index', 'dictionary', 'dict', 0, 0, 0, 'dict:list', 8, '2019-04-10 11:49:04', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (41, 6, 0, 1, '在线用户', 'OnlineUser', 'monitor/online/index', 'Steve-Jobs', 'online', 0, 0, 0, NULL, 10, '2019-10-26 22:08:43', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (44, 2, 0, 2, '用户新增', NULL, '', '', '', 0, 0, 0, 'user:add', 2, '2019-10-29 10:59:46', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (45, 2, 0, 2, '用户编辑', NULL, '', '', '', 0, 0, 0, 'user:edit', 3, '2019-10-29 11:00:08', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (46, 2, 0, 2, '用户删除', NULL, '', '', '', 0, 0, 0, 'user:del', 4, '2019-10-29 11:00:23', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (48, 3, 0, 2, '角色创建', NULL, '', '', '', 0, 0, 0, 'roles:add', 2, '2019-10-29 12:45:34', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (49, 3, 0, 2, '角色修改', NULL, '', '', '', 0, 0, 0, 'roles:edit', 3, '2019-10-29 12:46:16', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (50, 3, 0, 2, '角色删除', NULL, '', '', '', 0, 0, 0, 'roles:del', 4, '2019-10-29 12:46:51', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (52, 5, 0, 2, '菜单新增', NULL, '', '', '', 0, 0, 0, 'menu:add', 2, '2019-10-29 12:55:07', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (53, 5, 0, 2, '菜单编辑', NULL, '', '', '', 0, 0, 0, 'menu:edit', 3, '2019-10-29 12:55:40', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (54, 5, 0, 2, '菜单删除', NULL, '', '', '', 0, 0, 0, 'menu:del', 4, '2019-10-29 12:56:00', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (56, 35, 0, 2, '部门新增', NULL, '', '', '', 0, 0, 0, 'dept:add', 2, '2019-10-29 12:57:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (57, 35, 0, 2, '部门编辑', NULL, '', '', '', 0, 0, 0, 'dept:edit', 3, '2019-10-29 12:57:27', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (58, 35, 0, 2, '部门删除', NULL, '', '', '', 0, 0, 0, 'dept:del', 4, '2019-10-29 12:57:41', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (60, 37, 0, 2, '岗位新增', NULL, '', '', '', 0, 0, 0, 'job:add', 2, '2019-10-29 12:58:27', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (61, 37, 0, 2, '岗位编辑', NULL, '', '', '', 0, 0, 0, 'job:edit', 3, '2019-10-29 12:58:45', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (62, 37, 0, 2, '岗位删除', NULL, '', '', '', 0, 0, 0, 'job:del', 4, '2019-10-29 12:59:04', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (64, 39, 0, 2, '字典新增', NULL, '', '', '', 0, 0, 0, 'dict:add', 2, '2019-10-29 13:00:17', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (65, 39, 0, 2, '字典编辑', NULL, '', '', '', 0, 0, 0, 'dict:edit', 3, '2019-10-29 13:00:42', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (66, 39, 0, 2, '字典删除', NULL, '', '', '', 0, 0, 0, 'dict:del', 4, '2019-10-29 13:00:59', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (73, 28, 0, 2, '任务新增', NULL, '', '', '', 0, 0, 0, 'timing:add', 2, '2019-10-29 13:07:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (74, 28, 0, 2, '任务编辑', NULL, '', '', '', 0, 0, 0, 'timing:edit', 3, '2019-10-29 13:07:41', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (75, 28, 0, 2, '任务删除', NULL, '', '', '', 0, 0, 0, 'timing:del', 4, '2019-10-29 13:07:54', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (77, 18, 0, 2, '上传文件', NULL, '', '', '', 0, 0, 0, 'storage:add', 2, '2019-10-29 13:09:09', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (78, 18, 0, 2, '文件编辑', NULL, '', '', '', 0, 0, 0, 'storage:edit', 3, '2019-10-29 13:09:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (79, 18, 0, 2, '文件删除', NULL, '', '', '', 0, 0, 0, 'storage:del', 4, '2019-10-29 13:09:34', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (80, 6, 0, 1, '服务监控', 'ServerMonitor', 'monitor/server/index', 'codeConsole', 'server', 0, 0, 0, 'monitor:list', 14, '2019-11-07 13:06:39', NULL, '2020-05-04 18:20:50', NULL, 0);
INSERT INTO `sys_menu` VALUES (82, 36, 0, 1, '生成配置', 'GeneratorConfig', 'generator/config', 'dev', 'generator/config/:tableName', 0, 1, 1, '', 33, '2019-11-17 20:08:56', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (83, 10, 0, 1, '图表库', 'Echarts', 'components/Echarts', 'chart', 'echarts', 0, 1, 0, '', 50, '2019-11-21 09:04:32', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (90, NULL, 5, 1, '运维管理', 'Mnt', '', 'mnt', 'mnt', 0, 0, 0, NULL, 20, '2019-11-09 10:31:08', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (92, 90, 3, 1, '服务器', 'ServerDeploy', 'mnt/server/index', 'server', 'mnt/serverDeploy', 0, 0, 0, 'serverDeploy:list', 22, '2019-11-10 10:29:25', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (93, 90, 3, 1, '应用管理', 'App', 'mnt/app/index', 'app', 'mnt/app', 0, 0, 0, 'app:list', 23, '2019-11-10 11:05:16', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (94, 90, 3, 1, '部署管理', 'Deploy', 'mnt/deploy/index', 'deploy', 'mnt/deploy', 0, 0, 0, 'deploy:list', 24, '2019-11-10 15:56:55', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (97, 90, 1, 1, '部署备份', 'DeployHistory', 'mnt/deployHistory/index', 'backup', 'mnt/deployHistory', 0, 0, 0, 'deployHistory:list', 25, '2019-11-10 16:49:44', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (98, 90, 3, 1, '数据库管理', 'Database', 'mnt/database/index', 'database', 'mnt/database', 0, 0, 0, 'database:list', 26, '2019-11-10 20:40:04', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (102, 97, 0, 2, '删除', NULL, '', '', '', 0, 0, 0, 'deployHistory:del', 999, '2019-11-17 09:32:48', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (103, 92, 0, 2, '服务器新增', NULL, '', '', '', 0, 0, 0, 'serverDeploy:add', 999, '2019-11-17 11:08:33', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (104, 92, 0, 2, '服务器编辑', NULL, '', '', '', 0, 0, 0, 'serverDeploy:edit', 999, '2019-11-17 11:08:57', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (105, 92, 0, 2, '服务器删除', NULL, '', '', '', 0, 0, 0, 'serverDeploy:del', 999, '2019-11-17 11:09:15', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (106, 93, 0, 2, '应用新增', NULL, '', '', '', 0, 0, 0, 'app:add', 999, '2019-11-17 11:10:03', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (107, 93, 0, 2, '应用编辑', NULL, '', '', '', 0, 0, 0, 'app:edit', 999, '2019-11-17 11:10:28', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (108, 93, 0, 2, '应用删除', NULL, '', '', '', 0, 0, 0, 'app:del', 999, '2019-11-17 11:10:55', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (109, 94, 0, 2, '部署新增', NULL, '', '', '', 0, 0, 0, 'deploy:add', 999, '2019-11-17 11:11:22', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (110, 94, 0, 2, '部署编辑', NULL, '', '', '', 0, 0, 0, 'deploy:edit', 999, '2019-11-17 11:11:41', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (111, 94, 0, 2, '部署删除', NULL, '', '', '', 0, 0, 0, 'deploy:del', 999, '2019-11-17 11:12:01', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (112, 98, 0, 2, '数据库新增', NULL, '', '', '', 0, 0, 0, 'database:add', 999, '2019-11-17 11:12:43', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (113, 98, 0, 2, '数据库编辑', NULL, '', '', '', 0, 0, 0, 'database:edit', 999, '2019-11-17 11:12:58', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (114, 98, 0, 2, '数据库删除', NULL, '', '', '', 0, 0, 0, 'database:del', 999, '2019-11-17 11:13:14', NULL, NULL, NULL, 0);
INSERT INTO `sys_menu` VALUES (116, 36, 0, 1, '生成预览', 'Preview', 'generator/preview', 'java', 'generator/preview/:tableName', 0, 1, 1, NULL, 999, '2019-11-26 14:54:36', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_oss_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss_attachment`;
CREATE TABLE `sys_oss_attachment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bucket_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储桶名称',
  `path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '待存储路径',
  `business_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务ID',
  `business_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `sub_business_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子业务ID',
  `sub_business_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子业务类型',
  `file_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型',
  `file_size` double(15, 2) NOT NULL COMMENT '文件大小',
  `file_md5` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'MD5',
  `store_server` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储服务器信息',
  `store_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储路径信息',
  `group_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属组ID',
  `user_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人ID',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'OSS附件列表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_oss_attachment
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 1, '-', '全部', '2018-11-23 11:04:37', NULL, '2020-08-06 16:10:24', NULL, 0);
INSERT INTO `sys_role` VALUES (2, '普通用户', 2, '-', '本级', '2018-11-23 13:09:06', NULL, '2020-09-05 10:45:12', NULL, 0);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色部门关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (4, 8, 5);
INSERT INTO `sys_role_dept` VALUES (5, 17, 1);
INSERT INTO `sys_role_dept` VALUES (6, 22, 5);
INSERT INTO `sys_role_dept` VALUES (7, 24, 5);
INSERT INTO `sys_role_dept` VALUES (8, 9, 4);
INSERT INTO `sys_role_dept` VALUES (9, 1, 5);
INSERT INTO `sys_role_dept` VALUES (10, 1, 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 727 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (141, 1, 8);
INSERT INTO `sys_role_menu` VALUES (142, 49, 8);
INSERT INTO `sys_role_menu` VALUES (143, 53, 8);
INSERT INTO `sys_role_menu` VALUES (144, 2, 8);
INSERT INTO `sys_role_menu` VALUES (145, 37, 8);
INSERT INTO `sys_role_menu` VALUES (146, 61, 8);
INSERT INTO `sys_role_menu` VALUES (147, 56, 8);
INSERT INTO `sys_role_menu` VALUES (148, 5, 8);
INSERT INTO `sys_role_menu` VALUES (149, 62, 8);
INSERT INTO `sys_role_menu` VALUES (150, 75, 8);
INSERT INTO `sys_role_menu` VALUES (151, 48, 8);
INSERT INTO `sys_role_menu` VALUES (152, 66, 8);
INSERT INTO `sys_role_menu` VALUES (153, 46, 8);
INSERT INTO `sys_role_menu` VALUES (154, 45, 8);
INSERT INTO `sys_role_menu` VALUES (155, 52, 8);
INSERT INTO `sys_role_menu` VALUES (156, 54, 8);
INSERT INTO `sys_role_menu` VALUES (157, 65, 8);
INSERT INTO `sys_role_menu` VALUES (158, 28, 8);
INSERT INTO `sys_role_menu` VALUES (159, 74, 8);
INSERT INTO `sys_role_menu` VALUES (160, 57, 8);
INSERT INTO `sys_role_menu` VALUES (161, 44, 8);
INSERT INTO `sys_role_menu` VALUES (162, 64, 8);
INSERT INTO `sys_role_menu` VALUES (163, 50, 8);
INSERT INTO `sys_role_menu` VALUES (164, 60, 8);
INSERT INTO `sys_role_menu` VALUES (165, 39, 8);
INSERT INTO `sys_role_menu` VALUES (166, 3, 8);
INSERT INTO `sys_role_menu` VALUES (167, 73, 8);
INSERT INTO `sys_role_menu` VALUES (168, 58, 8);
INSERT INTO `sys_role_menu` VALUES (169, 35, 8);
INSERT INTO `sys_role_menu` VALUES (170, 58, 9);
INSERT INTO `sys_role_menu` VALUES (171, 2, 9);
INSERT INTO `sys_role_menu` VALUES (172, 46, 9);
INSERT INTO `sys_role_menu` VALUES (173, 44, 9);
INSERT INTO `sys_role_menu` VALUES (174, 45, 9);
INSERT INTO `sys_role_menu` VALUES (175, 7, 24);
INSERT INTO `sys_role_menu` VALUES (176, 1, 2);
INSERT INTO `sys_role_menu` VALUES (177, 2, 2);
INSERT INTO `sys_role_menu` VALUES (178, 6, 2);
INSERT INTO `sys_role_menu` VALUES (179, 7, 2);
INSERT INTO `sys_role_menu` VALUES (180, 9, 2);
INSERT INTO `sys_role_menu` VALUES (181, 10, 2);
INSERT INTO `sys_role_menu` VALUES (182, 11, 2);
INSERT INTO `sys_role_menu` VALUES (183, 14, 2);
INSERT INTO `sys_role_menu` VALUES (184, 15, 2);
INSERT INTO `sys_role_menu` VALUES (185, 19, 2);
INSERT INTO `sys_role_menu` VALUES (186, 21, 2);
INSERT INTO `sys_role_menu` VALUES (187, 22, 2);
INSERT INTO `sys_role_menu` VALUES (188, 23, 2);
INSERT INTO `sys_role_menu` VALUES (189, 24, 2);
INSERT INTO `sys_role_menu` VALUES (190, 27, 2);
INSERT INTO `sys_role_menu` VALUES (191, 30, 2);
INSERT INTO `sys_role_menu` VALUES (192, 32, 2);
INSERT INTO `sys_role_menu` VALUES (193, 33, 2);
INSERT INTO `sys_role_menu` VALUES (194, 34, 2);
INSERT INTO `sys_role_menu` VALUES (195, 36, 2);
INSERT INTO `sys_role_menu` VALUES (196, 80, 2);
INSERT INTO `sys_role_menu` VALUES (197, 82, 2);
INSERT INTO `sys_role_menu` VALUES (198, 83, 2);
INSERT INTO `sys_role_menu` VALUES (199, 116, 2);
INSERT INTO `sys_role_menu` VALUES (651, 1, 1);
INSERT INTO `sys_role_menu` VALUES (652, 2, 1);
INSERT INTO `sys_role_menu` VALUES (653, 3, 1);
INSERT INTO `sys_role_menu` VALUES (654, 5, 1);
INSERT INTO `sys_role_menu` VALUES (655, 6, 1);
INSERT INTO `sys_role_menu` VALUES (656, 7, 1);
INSERT INTO `sys_role_menu` VALUES (657, 9, 1);
INSERT INTO `sys_role_menu` VALUES (658, 10, 1);
INSERT INTO `sys_role_menu` VALUES (659, 11, 1);
INSERT INTO `sys_role_menu` VALUES (660, 14, 1);
INSERT INTO `sys_role_menu` VALUES (661, 15, 1);
INSERT INTO `sys_role_menu` VALUES (662, 18, 1);
INSERT INTO `sys_role_menu` VALUES (663, 19, 1);
INSERT INTO `sys_role_menu` VALUES (664, 21, 1);
INSERT INTO `sys_role_menu` VALUES (665, 22, 1);
INSERT INTO `sys_role_menu` VALUES (666, 23, 1);
INSERT INTO `sys_role_menu` VALUES (667, 24, 1);
INSERT INTO `sys_role_menu` VALUES (668, 27, 1);
INSERT INTO `sys_role_menu` VALUES (669, 28, 1);
INSERT INTO `sys_role_menu` VALUES (670, 30, 1);
INSERT INTO `sys_role_menu` VALUES (671, 32, 1);
INSERT INTO `sys_role_menu` VALUES (672, 33, 1);
INSERT INTO `sys_role_menu` VALUES (673, 34, 1);
INSERT INTO `sys_role_menu` VALUES (674, 36, 1);
INSERT INTO `sys_role_menu` VALUES (675, 37, 1);
INSERT INTO `sys_role_menu` VALUES (676, 38, 1);
INSERT INTO `sys_role_menu` VALUES (677, 39, 1);
INSERT INTO `sys_role_menu` VALUES (678, 41, 1);
INSERT INTO `sys_role_menu` VALUES (679, 44, 1);
INSERT INTO `sys_role_menu` VALUES (680, 45, 1);
INSERT INTO `sys_role_menu` VALUES (681, 46, 1);
INSERT INTO `sys_role_menu` VALUES (682, 48, 1);
INSERT INTO `sys_role_menu` VALUES (683, 49, 1);
INSERT INTO `sys_role_menu` VALUES (684, 50, 1);
INSERT INTO `sys_role_menu` VALUES (685, 52, 1);
INSERT INTO `sys_role_menu` VALUES (686, 53, 1);
INSERT INTO `sys_role_menu` VALUES (687, 54, 1);
INSERT INTO `sys_role_menu` VALUES (688, 60, 1);
INSERT INTO `sys_role_menu` VALUES (689, 61, 1);
INSERT INTO `sys_role_menu` VALUES (690, 62, 1);
INSERT INTO `sys_role_menu` VALUES (691, 64, 1);
INSERT INTO `sys_role_menu` VALUES (692, 65, 1);
INSERT INTO `sys_role_menu` VALUES (693, 66, 1);
INSERT INTO `sys_role_menu` VALUES (694, 73, 1);
INSERT INTO `sys_role_menu` VALUES (695, 74, 1);
INSERT INTO `sys_role_menu` VALUES (696, 75, 1);
INSERT INTO `sys_role_menu` VALUES (697, 77, 1);
INSERT INTO `sys_role_menu` VALUES (698, 78, 1);
INSERT INTO `sys_role_menu` VALUES (699, 79, 1);
INSERT INTO `sys_role_menu` VALUES (700, 80, 1);
INSERT INTO `sys_role_menu` VALUES (701, 82, 1);
INSERT INTO `sys_role_menu` VALUES (702, 83, 1);
INSERT INTO `sys_role_menu` VALUES (703, 90, 1);
INSERT INTO `sys_role_menu` VALUES (704, 92, 1);
INSERT INTO `sys_role_menu` VALUES (705, 93, 1);
INSERT INTO `sys_role_menu` VALUES (706, 94, 1);
INSERT INTO `sys_role_menu` VALUES (707, 97, 1);
INSERT INTO `sys_role_menu` VALUES (708, 98, 1);
INSERT INTO `sys_role_menu` VALUES (709, 102, 1);
INSERT INTO `sys_role_menu` VALUES (710, 103, 1);
INSERT INTO `sys_role_menu` VALUES (711, 104, 1);
INSERT INTO `sys_role_menu` VALUES (712, 105, 1);
INSERT INTO `sys_role_menu` VALUES (713, 106, 1);
INSERT INTO `sys_role_menu` VALUES (714, 107, 1);
INSERT INTO `sys_role_menu` VALUES (715, 108, 1);
INSERT INTO `sys_role_menu` VALUES (716, 109, 1);
INSERT INTO `sys_role_menu` VALUES (717, 110, 1);
INSERT INTO `sys_role_menu` VALUES (718, 111, 1);
INSERT INTO `sys_role_menu` VALUES (719, 112, 1);
INSERT INTO `sys_role_menu` VALUES (720, 113, 1);
INSERT INTO `sys_role_menu` VALUES (721, 114, 1);
INSERT INTO `sys_role_menu` VALUES (722, 116, 1);
INSERT INTO `sys_role_menu` VALUES (723, 56, 1);
INSERT INTO `sys_role_menu` VALUES (724, 35, 1);
INSERT INTO `sys_role_menu` VALUES (725, 57, 1);
INSERT INTO `sys_role_menu` VALUES (726, 58, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 2, 'admin', '管理员', '男', '18888888888', '201507802@qq.com', 'avatar-20200806032259161.png', '/Users/jie/Documents/work/me/admin/eladmin/~/avatar/avatar-20200806032259161.png', '$2a$10$W00k5XX2yj/l7UthtWMUgeDAKQV7ULX3odvDx2TYKpcogqPm0Tn5u', 1, 1, '2020-05-03 16:38:31', '2018-08-23 09:11:56', '1', '2020-09-05 10:43:31', NULL, 0);
INSERT INTO `sys_user` VALUES (2, 2, 'test', '测试', '男', '19999999999', '231@qq.com', NULL, NULL, '$2a$10$W00k5XX2yj/l7UthtWMUgeDAKQV7ULX3odvDx2TYKpcogqPm0Tn5u', 0, 1, NULL, '2020-05-05 11:15:49', '1', '2020-09-05 10:43:38', NULL, 0);
INSERT INTO `sys_user` VALUES (7, 1, '孙笑川', '123', '男', '13242842112', '1@qq.com', NULL, NULL, '$2a$10$r/BsX68Q6ZgiM1Qx0/UlAusU0xHwiTPqP1zCaXLBZcUJGZl/7BZ9C', 0, 1, NULL, '2022-10-26 00:22:40', NULL, '2022-10-26 00:22:40', '1', 0);
INSERT INTO `sys_user` VALUES (8, 7, '刘波', '药水哥', '男', '123', '123@qq.com', NULL, NULL, '$2a$10$HMC37QtDVWZ6UatfQgT5wu1JDp53//LVBqnanCdTXgBmygT9DOCqq', 0, 1, NULL, '2022-10-26 21:43:25', '1', '2022-10-26 21:43:25', NULL, 0);

-- ----------------------------
-- Table structure for sys_user_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_job`;
CREATE TABLE `sys_user_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `job_id` int(11) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户岗位关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user_job
-- ----------------------------
INSERT INTO `sys_user_job` VALUES (1, 8, 4);
INSERT INTO `sys_user_job` VALUES (2, 9, 1);
INSERT INTO `sys_user_job` VALUES (4, 12, 2);
INSERT INTO `sys_user_job` VALUES (5, 12, 3);
INSERT INTO `sys_user_job` VALUES (6, 13, 2);
INSERT INTO `sys_user_job` VALUES (7, 13, 3);
INSERT INTO `sys_user_job` VALUES (15, 15, 3);
INSERT INTO `sys_user_job` VALUES (16, 23, 3);
INSERT INTO `sys_user_job` VALUES (17, 24, 2);
INSERT INTO `sys_user_job` VALUES (36, 38, 2);
INSERT INTO `sys_user_job` VALUES (47, 39, 3);
INSERT INTO `sys_user_job` VALUES (52, 25, 1);
INSERT INTO `sys_user_job` VALUES (55, 11, 1);
INSERT INTO `sys_user_job` VALUES (56, 11, 3);
INSERT INTO `sys_user_job` VALUES (59, 37, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);
INSERT INTO `sys_user_role` VALUES (4, 7, 1);
INSERT INTO `sys_user_role` VALUES (5, 8, 2);
INSERT INTO `sys_user_role` VALUES (6, 9, 2);
INSERT INTO `sys_user_role` VALUES (8, 12, 2);
INSERT INTO `sys_user_role` VALUES (9, 12, 1);
INSERT INTO `sys_user_role` VALUES (10, 13, 2);
INSERT INTO `sys_user_role` VALUES (11, 13, 1);
INSERT INTO `sys_user_role` VALUES (19, 15, 2);
INSERT INTO `sys_user_role` VALUES (20, 23, 4);
INSERT INTO `sys_user_role` VALUES (21, 24, 2);
INSERT INTO `sys_user_role` VALUES (40, 38, 2);
INSERT INTO `sys_user_role` VALUES (51, 39, 3);
INSERT INTO `sys_user_role` VALUES (56, 25, 2);
INSERT INTO `sys_user_role` VALUES (59, 11, 1);
INSERT INTO `sys_user_role` VALUES (60, 11, 2);
INSERT INTO `sys_user_role` VALUES (63, 37, 2);

SET FOREIGN_KEY_CHECKS = 1;
