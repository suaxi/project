package com.software.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.entity.Dept;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
public interface DeptService {

    /**
     * 新增
     *
     * @param dept 部门信息
     * @return
     */
    boolean add(Dept dept);

    /**
     * 修改
     *
     * @param dept 部门信息
     * @return
     */
    boolean update(Dept dept);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(Long[] ids);

    /**
     * 根据id查询部门信息
     *
     * @param id id
     * @return
     */
    Dept queryById(Long id);

    /**
     * 根据名称查询部门信息
     *
     * @param name 部门名称
     * @return
     */
    Dept queryByName(String name);

    /**
     * 查询部门列表
     *
     * @return
     */
    List<Dept> queryList();

    /**
     * 分页查询
     *
     * @param dept         部门信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<Dept> queryPage(Dept dept, QueryRequest queryRequest);
}
