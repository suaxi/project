package com.software.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.software.system.entity.Dept;
import com.software.system.entity.RoleDept;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Wang Hao
 * @date 2022/11/5 22:50
 */
@Repository
public interface RoleDeptMapper extends BaseMapper<RoleDept> {

    /**
     * 根据角色id查询角色对应的部门信息
     *
     * @param roleId 角色id
     * @return 角色对应的部门信息集合
     */
    Set<Dept> queryByRoleId(Long roleId);
}
