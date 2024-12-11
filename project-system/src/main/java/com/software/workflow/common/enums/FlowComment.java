package com.software.workflow.common.enums;

import com.software.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author suaxi
 * @date 2024/12/10 19:01
 */
@Getter
@AllArgsConstructor
public enum FlowComment {

    NORMAL("1", "正常意见"),
    REBACK("2", "退回意见"),
    REJECT("3", "驳回意见"),
    DELEGATE("4", "委派意见"),
    ASSIGN("5", "转办意见"),
    STOP("6", "终止流程");

    private String type;

    private String remark;

    /**
     * 通过 type 转化为具体的枚举类型
     *
     * @param type
     * @return
     */
    public static FlowComment parseOf(String type) {
        for (FlowComment item : values()) {
            if (item.getType().equalsIgnoreCase(type)) {
                return item;
            }
        }
        throw new BadRequestException("状态[" + type + "]不匹配!");
    }
}
