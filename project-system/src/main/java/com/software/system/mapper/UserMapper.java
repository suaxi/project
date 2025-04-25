package com.software.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.system.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return
     */
    User queryById(Integer id);

    /**
     * 分页模糊查询
     *
     * @param page      分页参数
     * @param user      user
     * @param dataScope 数据权限
     * @return 用户列表（分页）
     */
    Page<User> queryPage(Page<User> page, @Param("user") User user, @Param("dataScope") List<Long> dataScope);

}
