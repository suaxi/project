package com.software.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.software.system.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询数据权限
     *
     * @param userId 用户id
     * @return
     */
    List<String> queryDataScopeByUserId(Long userId);

    /**
     * 根据用户id查询角色列表
     * @param userId 用户id
     * @return 角色列表
     */
    List<Role> queryRoleListByUserId(Long userId);

}
