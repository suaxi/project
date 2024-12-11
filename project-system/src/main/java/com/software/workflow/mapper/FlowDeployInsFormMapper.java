package com.software.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.workflow.dto.FlowProcDefDto;
import com.software.workflow.entity.FlowDeployInsForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author suaxi
 * @date 2024/12/10 20:45
 */
@Repository
public interface FlowDeployInsFormMapper extends BaseMapper<FlowDeployInsForm> {

    /**
     * 流程定义分页查询
     *
     * @param page 分页参数
     * @param name 名称
     * @return
     */
    Page<FlowProcDefDto> queryProcDefPage(@Param("page") Page<FlowProcDefDto> page, @Param("name") String name);

}
