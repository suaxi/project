package com.software.utils;

/**
 * @author Wang Hao
 * @date 2022/10/10 22:48
 * @description 在SpringContextHolder初始化前提交一个回调方法，初始化完成后进行回调使用
 */
public interface CallBack {

    /**
     * 回调方法
     */
    void executor();

    /**
     * 该次回调任务名称
     *
     * @return
     */
    default String getCallBackName() {
        return Thread.currentThread().getId() + ":" + this.getClass().getName();
    }
}
