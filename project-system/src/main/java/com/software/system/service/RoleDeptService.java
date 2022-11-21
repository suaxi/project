package com.software.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.software.system.entity.Dept;
import com.software.system.entity.RoleDept;

import java.util.List;
import java.util.Set;

/**
 * @author Wang Hao
 * @date 2022/11/5 22:51
 */
public interface RoleDeptService extends IService<RoleDept> {

    /**
     * 根据角色id删除角色部门关联数据
     *
     * @param roleIds 角色ids
     */
    void deleteRoleDeptByRoleId(List<Long> roleIds);

    /**
     * 根据角色id查询角色对应的部门信息
     *
     * @param roleId 角色id
     * @return 角色对应的部门信息集合
     */
    Set<Dept> queryByRoleId(Long roleId);
}
