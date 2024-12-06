package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.system.entity.UserJob;
import com.software.system.mapper.UserJobMapper;
import com.software.system.service.UserJobService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Wang Hao
 * @date 2022/10/26 21:25
 */
@Service
public class UserJobServiceImpl extends ServiceImpl<UserJobMapper, UserJob> implements UserJobService {

    @Override
    public void deleteUserJobByUserId(Integer[] userIds) {
        if (userIds.length > 0) {
            this.baseMapper.delete(new LambdaQueryWrapper<UserJob>().in(UserJob::getUserId, Arrays.asList(userIds)));
        }
    }
}
