package com.software.workflow.common.constant;

/**
 * @author suaxi
 * @date 2024/12/10 18:59
 */
public class ProcessConstant {

    /**
     * bpmn文件后缀
     */
    public static final String BPMN_FILE_SUFFIX = ".bpmn";

    /**
     * 动态数据
     */
    public static final String DYNAMIC = "dynamic";

    /**
     * 固定任务接收
     */
    public static final String FIXED = "fixed";

    /**
     * 单个审批人
     */
    public static final String ASSIGNEE = "assignee";

    /**
     * 候选人
     */
    public static final String CANDIDATE_USERS = "candidateUsers";

    /**
     * 审批组
     */
    public static final String CANDIDATE_GROUPS = "candidateGroups";

    /**
     * 单个审批人
     */
    public static final String PROCESS_APPROVAL = "approval";

    /**
     * 会签人员
     */
    public static final String PROCESS_MULTI_INSTANCE_USER = "userList";

    /**
     * nameapace
     */
    public static final String NAMASPASE = "http://flowable.org/bpmn";

    /**
     * 会签节点
     */
    public static final String PROCESS_MULTI_INSTANCE = "multiInstance";

    /**
     * 自定义属性 dataType
     */
    public static final String PROCESS_CUSTOM_DATA_TYPE = "dataType";

    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE = "userType";

    /**
     * 初始化人员
     */
    public static final String PROCESS_INITIATOR = "INITIATOR";


    /**
     * 流程跳过
     */
    public static final String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";

}
