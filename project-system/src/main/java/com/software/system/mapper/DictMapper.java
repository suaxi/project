package com.software.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.software.system.dto.DictDto;
import com.software.system.entity.Dict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/12/8 21:52
 */
@Repository
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 根据名称查询数据字典、数据字典详情
     *
     * @param dictNameList 数据字典名称
     * @return 数据字典集合
     */
    List<DictDto> queryListByDictName(@Param("dictNameList") List<String> dictNameList);
}
