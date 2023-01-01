package com.software.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.oss.entity.OssAttachment;
import com.software.oss.mapper.OssAttachmentMapper;
import com.software.oss.service.OssAttachmentService;
import com.software.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @author Wang Hao
 * @date 2023/1/1 21:03
 */
@Service
public class OssAttachmentServiceImpl extends ServiceImpl<OssAttachmentMapper, OssAttachment> implements OssAttachmentService {

    @Override
    public boolean add(OssAttachment ossAttachment) {
        ossAttachment.setUserId(SecurityUtils.getCurrentUserId());
        return this.save(ossAttachment);
    }

    @Override
    public Page<OssAttachment> queryPage(OssAttachment ossAttachment, QueryRequest queryRequest) {
        LambdaQueryWrapper<OssAttachment> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ossAttachment.getBucketName())) {
            lambdaQuery().eq(OssAttachment::getBucketName, ossAttachment.getBucketName());
        }
        if (StringUtils.isNotBlank(ossAttachment.getBusinessId())) {
            queryWrapper.eq(OssAttachment::getBusinessId, ossAttachment.getBusinessId());
        }
        if (StringUtils.isNotBlank(ossAttachment.getBusinessType())) {
            queryWrapper.likeRight(OssAttachment::getBusinessType, ossAttachment.getBusinessType());
        }
        if (StringUtils.isNotBlank(ossAttachment.getSubBusinessId())) {
            queryWrapper.eq(OssAttachment::getSubBusinessId, ossAttachment.getSubBusinessId());
        }
        if (StringUtils.isNotBlank(ossAttachment.getSubBusinessType())) {
            queryWrapper.likeRight(OssAttachment::getSubBusinessType, ossAttachment.getSubBusinessType());
        }
        if (StringUtils.isNotBlank(ossAttachment.getFileName())) {
            queryWrapper.eq(OssAttachment::getFileName, ossAttachment.getFileName());
        }
        if (StringUtils.isNotBlank(ossAttachment.getFileType())) {
            queryWrapper.eq(OssAttachment::getFileType, ossAttachment.getFileType().toUpperCase(Locale.ROOT).split(StringConstant.COMMA));
        }
        if (StringUtils.isNotBlank(ossAttachment.getStoreServer())) {
            queryWrapper.eq(OssAttachment::getStoreServer, ossAttachment.getStoreServer());
        }
        if (StringUtils.isNotBlank(ossAttachment.getStorePath())) {
            queryWrapper.eq(OssAttachment::getStorePath, ossAttachment.getStorePath());
        }
        if (StringUtils.isNotBlank(ossAttachment.getGroupId())) {
            queryWrapper.eq(OssAttachment::getGroupId, ossAttachment.getGroupId());
        }
        if (ossAttachment.getUserId() != null) {
            queryWrapper.eq(OssAttachment::getUserId, ossAttachment.getUserId());
        }
        Page<OssAttachment> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }
}
