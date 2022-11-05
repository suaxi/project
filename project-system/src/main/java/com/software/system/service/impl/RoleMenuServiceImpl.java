package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.system.entity.RoleMenu;
import com.software.system.mapper.RoleMenuMapper;
import com.software.system.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author Wang Hao
 * @date 2022/11/5 16:53
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleMenuByRoleId(Long[] roleIds) {
        if (roleIds.length > 0) {
            this.baseMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, Arrays.asList(roleIds)));
        }
    }
}
