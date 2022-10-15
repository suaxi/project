package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.system.entity.User;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
public interface UserService {

    /**
     * 新增
     *
     * @param user 用户信息
     * @return
     */
    boolean add(User user);

    /**
     * 修改
     *
     * @param user 用户信息
     * @return
     */
    boolean update(User user);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(Long[] ids);

    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return
     */
    User queryById(Long id);

    /**
     * 根据用户名查询用户信息
     *
     * @param name 用户名
     * @return
     */
    User queryByName(String name);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<User> queryList();

    /**
     * 分页查询
     *
     * @param user         用户信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<User> queryPage(User user, QueryRequest queryRequest);
}
