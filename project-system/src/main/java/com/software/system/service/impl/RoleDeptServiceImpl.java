package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.system.entity.Dept;
import com.software.system.entity.RoleDept;
import com.software.system.mapper.RoleDeptMapper;
import com.software.system.service.RoleDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Wang Hao
 * @date 2022/11/5 22:52
 */
@Service
public class RoleDeptServiceImpl extends ServiceImpl<RoleDeptMapper, RoleDept> implements RoleDeptService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleDeptByRoleId(List<Long> roleIds) {
        if (roleIds.size() > 0) {
            this.baseMapper.delete(new LambdaQueryWrapper<RoleDept>().in(RoleDept::getRoleId, roleIds));
        }
    }

    @Override
    public Set<Dept> queryByRoleId(Long roleId) {
        if (roleId != null) {
            return this.baseMapper.queryByRoleId(roleId);
        }
        return null;
    }
}
