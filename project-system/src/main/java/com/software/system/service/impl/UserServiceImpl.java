package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.system.entity.User;
import com.software.system.entity.UserJob;
import com.software.system.entity.UserRole;
import com.software.system.mapper.UserMapper;
import com.software.system.service.UserJobService;
import com.software.system.service.UserRoleService;
import com.software.system.service.UserService;
import com.software.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String PASSWORD = "123456";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserJobService userJobService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(User user) {
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setCreateBy(SecurityUtils.getCurrentUserId());
        boolean flag = this.save(user);

        //用户角色关联关系
        if (flag) {
            boolean setUserRolesResult = this.setUserRoles(user.getId(), user.getRoleIds().split(StringConstant.COMMA));
            if (setUserRolesResult) {
                //用户岗位关联关系
                return this.setUserJobs(user.getId(), user.getJobIds().split(StringConstant.COMMA));
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(User user) {
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] ids) {
        if (ids.length > 0) {
            return this.removeByIds(Arrays.asList(ids));
        }
        return false;
    }

    @Override
    public User queryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("用户id不能为空");
        }
        return this.getById(id);
    }

    @Override
    public User queryByName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(User::getUsername, name);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<User> queryList() {
        return this.list();
    }

    @Override
    public Page<User> queryPage(User user, QueryRequest queryRequest) {
        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.baseMapper.queryPage(page, user);
    }

    /**
     * 保存用户角色关联数据
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @return 用户角色关联数据保存结果
     */
    private boolean setUserRoles(Long userId, String[] roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roleIds).map(Long::new).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        return userRoleService.saveBatch(userRoles);
    }

    /**
     * 保存用户岗位关联数据
     *
     * @param userId 用户id
     * @param jobIds 岗位id
     * @return 用户岗位关联数据保存结果
     */
    private boolean setUserJobs(Long userId, String[] jobIds) {
        List<UserJob> userJobs = new ArrayList<>();
        Arrays.stream(jobIds).map(Long::new).forEach(jobId -> {
            UserJob userJob = new UserJob();
            userJob.setUserId(userId);
            userJob.setJobId(jobId);
            userJobs.add(userJob);
        });
        return userJobService.saveBatch(userJobs);
    }
}
