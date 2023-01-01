package com.software.oss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.software.dto.QueryRequest;
import com.software.oss.entity.OssAttachment;

/**
 * @author Wang Hao
 * @date 2023/1/1 21:00
 */
public interface OssAttachmentService extends IService<OssAttachment> {

    /**
     * 添加附件信息信息
     *
     * @param ossAttachment 附件信息
     * @return
     */
    boolean add(OssAttachment ossAttachment);

    /**
     * 分页查询附件信息
     *
     * @param ossAttachment 附件信息
     * @param queryRequest  查询参数
     * @return 附件信息分页查询列表
     */
    Page<OssAttachment> queryPage(OssAttachment ossAttachment, QueryRequest queryRequest);
}
