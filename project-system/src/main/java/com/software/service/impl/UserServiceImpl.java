package com.software.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.entity.User;
import com.software.mapper.UserMapper;
import com.software.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(User user) {
        //TODO 创建人信息
        return this.save(user);
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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(user.getUsername())) {
            queryWrapper.lambda().eq(User::getUsername, user.getUsername());
        }
        if (user.getDeptId() != null) {
            queryWrapper.lambda().eq(User::getDeptId, user.getDeptId());
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            queryWrapper.lambda().eq(User::getUsername, user.getUsername());
        }
        if (StringUtils.isNotBlank(user.getNickName())) {
            queryWrapper.lambda().eq(User::getNickName, user.getNickName());
        }
        if (StringUtils.isNotBlank(user.getPhone())) {
            queryWrapper.lambda().eq(User::getPhone, user.getPhone());
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            queryWrapper.lambda().eq(User::getEmail, user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            queryWrapper.lambda().eq(User::getEmail, user.getEmail());
        }
        if (user.getEnabled() != null) {
            queryWrapper.lambda().eq(User::getEnabled, user.getEnabled());
        }
        if (StringUtils.isNotBlank(queryRequest.getOrder())) {
            queryWrapper.orderBy(true, StringConstant.ASC.equals(queryRequest.getOrder()), queryRequest.getField());
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }
        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
