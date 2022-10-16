package com.software.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Wang Hao
 * @date 2022/10/5 17:08
 */
public class WebUtil {

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param str      指定字符串
     * @return
     */
    public static String renderString(HttpServletResponse response, String str) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
