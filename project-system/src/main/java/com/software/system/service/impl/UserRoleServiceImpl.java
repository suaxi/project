package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.system.entity.UserRole;
import com.software.system.mapper.UserRoleMapper;
import com.software.system.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Wang Hao
 * @date 2022/10/25 21:49
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public void deleteUserRoleByUserId(Integer[] userIds) {
        if (userIds.length > 0) {
            this.baseMapper.delete(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, Arrays.asList(userIds)));
        }
    }
}
