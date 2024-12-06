package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.system.entity.Dict;
import com.software.system.mapper.DictMapper;
import com.software.system.service.DictService;
import com.software.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/12/8 21:55
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    public boolean add(Dict dict) {
        dict.setCreateUser(SecurityUtils.getCurrentUsername());
        return this.save(dict);
    }

    @Override
    public boolean update(Dict dict) {
        dict.setUpdateUser(SecurityUtils.getCurrentUsername());
        return this.updateById(dict);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Dict queryById(Long id) {
        return this.getById(id);
    }

    @Override
    public Dict queryByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            return this.getOne(new LambdaQueryWrapper<Dict>().like(Dict::getName, name));
        }
        return null;
    }

    @Override
    public List<Dict> queryList() {
        return this.list();
    }

    @Override
    public Page<Dict> queryPage(Dict dict, QueryRequest queryRequest) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dict.getName())) {
            queryWrapper.lambda().like(Dict::getName, dict.getName());
        }
        Page<Dict> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
