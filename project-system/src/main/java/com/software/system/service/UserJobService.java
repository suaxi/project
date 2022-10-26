package com.software.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.software.system.entity.UserJob;

/**
 * @author Wang Hao
 * @date 2022/10/26 21:23
 */
public interface UserJobService extends IService<UserJob> {

    /**
     * 根据用户id删除用户岗位关联数据
     * @param userIds 用户ids
     */
    void deleteUserJobByUserId(Long[] userIds);
}
