package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.system.entity.DictDetail;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/12/8 21:54
 */
public interface DictDetailService {

    /**
     * 新增
     *
     * @param dictDetail 数据字典详情信息
     * @return
     */
    boolean add(DictDetail dictDetail);

    /**
     * 修改
     *
     * @param dictDetail 数据字典详情信息
     * @return
     */
    boolean update(DictDetail dictDetail);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Long> ids);

    /**
     * 删除
     *
     * @param dictIds dictIds
     * @return
     */
    boolean deleteByDictIds(List<Long> dictIds);

    /**
     * 根据id查询数据字典详情信息
     *
     * @param id id
     * @return
     */
    DictDetail queryById(Long id);

    /**
     * 根据字典id查询对应数据字典详情信息
     *
     * @param dictId 字典id
     * @return
     */
    List<DictDetail> queryByDictId(Long dictId);

    /**
     * 查询数据字典详情列表
     *
     * @return
     */
    List<DictDetail> queryList();

    /**
     * 分页查询
     *
     * @param dictDetail   数据字典详情信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<DictDetail> queryPage(DictDetail dictDetail, QueryRequest queryRequest);
}
