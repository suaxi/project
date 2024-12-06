package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.dto.QueryRequest;
import com.software.system.entity.DictDetail;
import com.software.system.mapper.DictDetailMapper;
import com.software.system.service.DictDetailService;
import com.software.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/12/8 22:04
 */
@Service
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    @Override
    public boolean add(DictDetail dictDetail) {
        dictDetail.setCreateUser(SecurityUtils.getCurrentUsername());
        return this.save(dictDetail);
    }

    @Override
    public boolean update(DictDetail dictDetail) {
        dictDetail.setUpdateUser(SecurityUtils.getCurrentUsername());
        return this.updateById(dictDetail);
    }

    @Override
    public boolean delete(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public DictDetail queryById(Long id) {
        return this.getById(id);
    }

    @Override
    public List<DictDetail> queryByDictId(Long dictId) {
        if (dictId != null) {
            return this.list(new LambdaQueryWrapper<DictDetail>().eq(DictDetail::getDictId, dictId));
        }
        return Collections.emptyList();
    }

    @Override
    public List<DictDetail> queryList() {
        return this.list();
    }

    @Override
    public Page<DictDetail> queryPage(DictDetail dictDetail, QueryRequest queryRequest) {
        QueryWrapper<DictDetail> queryWrapper = new QueryWrapper<>();
        if (dictDetail.getDictId() != null) {
            queryWrapper.lambda().eq(DictDetail::getDictId, dictDetail.getDictId());
        }
        Page<DictDetail> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
