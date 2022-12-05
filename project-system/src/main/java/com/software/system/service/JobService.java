package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.system.entity.Job;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
public interface JobService {

    /**
     * 新增
     *
     * @param job 岗位信息
     * @return
     */
    boolean add(Job job);

    /**
     * 修改
     *
     * @param job 岗位信息
     * @return
     */
    boolean update(Job job);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Long> ids);

    /**
     * 根据id查询岗位信息
     *
     * @param id id
     * @return
     */
    Job queryById(Long id);

    /**
     * 根据名称查询岗位信息
     *
     * @param name 岗位名称
     * @return
     */
    Job queryByName(String name);

    /**
     * 查询岗位列表
     *
     * @return
     */
    List<Job> queryList();

    /**
     * 分页查询
     *
     * @param job          岗位信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<Job> queryPage(Job job, QueryRequest queryRequest);
}
