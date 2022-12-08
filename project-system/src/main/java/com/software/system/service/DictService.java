package com.software.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.software.dto.QueryRequest;
import com.software.system.entity.Dict;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/12/8 21:54
 */
public interface DictService {

    /**
     * 新增
     *
     * @param dict 数据字典信息
     * @return
     */
    boolean add(Dict dict);

    /**
     * 修改
     *
     * @param dict 数据字典信息
     * @return
     */
    boolean update(Dict dict);

    /**
     * 删除
     *
     * @param ids ids
     * @return
     */
    boolean delete(List<Long> ids);

    /**
     * 根据id查询数据字典信息
     *
     * @param id id
     * @return
     */
    Dict queryById(Long id);

    /**
     * 根据名称查询数据字典信息
     *
     * @param name 名称
     * @return
     */
    Dict queryByName(String name);

    /**
     * 查询数据字典列表
     *
     * @return
     */
    List<Dict> queryList();

    /**
     * 分页查询
     *
     * @param dict         数据字典信息
     * @param queryRequest 查询参数
     * @return
     */
    Page<Dict> queryPage(Dict dict, QueryRequest queryRequest);
}
