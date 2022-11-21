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
import com.software.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        //用户关联属性
        if (flag) {
            return this.setUserAssociatedProperties(user);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(User user) {
        //删除关联数据
        Long[] userIds = Collections.singletonList(user.getId()).toArray(new Long[0]);
        userRoleService.deleteUserRoleByUserId(userIds);
        userJobService.deleteUserJobByUserId(userIds);

        user.setUpdateBy(SecurityUtils.getCurrentUserId());
        boolean flag = this.updateById(user);
        //用户关联属性
        if (flag) {
            return this.setUserAssociatedProperties(user);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] ids) {
        if (ids.length > 0) {
            boolean flag = this.removeByIds(Arrays.asList(ids));
            if (flag) {
                userRoleService.deleteUserRoleByUserId(ids);
                userJobService.deleteUserJobByUserId(ids);
            }
            return flag;
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
        List<Long> dataScope = SecurityUtils.getCurrentUserDataScope();
        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        SortUtil.handlePageSort(queryRequest, page, "updateTime", StringConstant.DESC, true);
        return this.baseMapper.queryPage(page, user, dataScope);
    }

    /**
     * 设置用户关联属性（角色、岗位）
     *
     * @param user 用户信息
     * @return 关联属性保存结果
     */
    private boolean setUserAssociatedProperties(User user) {
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(user.getRoleIds().split(StringConstant.COMMA)).map(Long::new).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        if (userRoleService.saveBatch(userRoles)) {
            List<UserJob> userJobs = new ArrayList<>();
            Arrays.stream(user.getJobIds().split(StringConstant.COMMA)).map(Long::new).forEach(jobId -> {
                UserJob userJob = new UserJob();
                userJob.setUserId(user.getId());
                userJob.setJobId(jobId);
                userJobs.add(userJob);
            });
            return userJobService.saveBatch(userJobs);
        }
        return false;
    }
}
