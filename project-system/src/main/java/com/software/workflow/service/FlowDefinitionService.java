package com.software.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.workflow.dto.FlowProcDefDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author suaxi
 * @date 2024/12/10 19:44
 */
public interface FlowDefinitionService {

    /**
     * 根据流程定义key判断流程是否存在
     *
     * @param processDefinitionKey 流程定义key
     * @return
     */
    boolean exist(String processDefinitionKey);

    /**
     * 流程定义列表
     *
     * @param pageNum  页数
     * @param pageSize 每页条数
     * @return 流程定义分页列表数据
     */
    Page<FlowProcDefDto> list(String name, Integer pageNum, Integer pageSize);

    /**
     * 导入流程文件
     * 当每个key的流程第一次部署时，指定版本为1。对其后所有使用相同key的流程定义，
     * 部署时版本会在该key当前已部署的最高版本号基础上加1，key参数用于区分流程定义
     *
     * @param name     流程部署名称
     * @param category 分类
     * @param in       inputStream
     */
    void importFile(String name, String category, InputStream in);

    /**
     * 读取xml
     *
     * @param deployId 流程部署ID
     * @return
     */
    String readXml(String deployId) throws IOException;

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程定义ID
     * @param variables 流程变量
     * @return
     */
    boolean startProcessInstanceById(String procDefId, Map<String, Object> variables);

    /**
     * 激活或挂起流程定义
     *
     * @param state    状态
     * @param deployId 流程部署ID
     */
    void updateState(Integer state, String deployId);

    /**
     * 删除流程定义
     *
     * @param deployId 流程部署ID act_ge_bytearray 表中 deployment_id值
     */
    void delete(String deployId);

    /**
     * 读取图片文件
     *
     * @param deployId
     * @return
     */
    InputStream readImage(String deployId);

}
