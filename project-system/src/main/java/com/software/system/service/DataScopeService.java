package com.software.system.service;

import com.software.system.entity.User;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/11/21 21:46
 */
public interface DataScopeService {

    /**
     * 查询当前用户数据权限范围
     *
     * @param user 当前用户信息
     * @return 当前用户数据权限范围（deptIds）集合
     */
    List<Integer> getDeptIds(User user);
}
