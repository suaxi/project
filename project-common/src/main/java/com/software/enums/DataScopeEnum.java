package com.software.enums;

import com.software.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Wang Hao
 * @date 2022/11/21 21:33
 * @description 数据权限枚举类
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /**
     * 全部
     */
    ALL("全部", "全部"),

    /**
     * 本级
     */
    THIS_LEVEL("本级", "本级"),

    /**
     * 自定义
     */
    CUSTOMIZE("自定义", "自定义");

    private String code;

    private String name;

    /**
     * 通过 Code 转化为具体的枚举类型
     *
     * @param code
     * @return
     */
    public static DataScopeEnum parseOf(String code) {
        for (DataScopeEnum item : values()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        throw new BadRequestException("状态[" + code + "]不匹配!");
    }
}
