package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.dto.Tree;
import com.software.system.dto.DeptDto;
import com.software.system.entity.Dept;

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
    boolean delete(List<Integer> ids);

    /**
     * 根据id查询部门信息
     *
     * @param id id
     * @return
     */
    Dept queryById(Integer id);

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
    Page<DeptDto> queryPage(Dept dept, QueryRequest queryRequest);

    /**
     * 根据父id查询子级部门
     *
     * @param pid 父级id
     * @return 子级部门列表
     */
    List<DeptDto> queryChildListByPid(Integer pid);

    /**
     * 查询部门树
     *
     * @return 部门树
     */
    List<? extends Tree<?>> queryDeptTree();

    /**
     * 根据id查找同级与上级部门数据
     *
     * @param dept     部门信息
     * @param deptList 部门信息list
     * @return 同级与上级部门列表
     */
    List<Dept> querySuperiorList(Dept dept, List<Dept> deptList);

    /**
     * 查询指定部门的子级部门
     *
     * @param deptList 指定部门集合
     * @return 指定部门子级部门集合（包含父级）
     */
    List<Dept> queryDeptChildren(List<Dept> deptList);

}
