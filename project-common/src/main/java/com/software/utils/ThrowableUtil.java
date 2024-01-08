package com.software.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Wang Hao
 * @date 2024/1/8 21:40
 * @description ThrowableUtil
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     *
     * @param throwable throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
