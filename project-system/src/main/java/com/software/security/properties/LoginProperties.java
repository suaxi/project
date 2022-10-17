package com.software.security.properties;

import com.software.security.enums.LoginCodeEnum;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @author Wang Hao
 * @date 2022/10/15 12:51
 */
@Data
@Component
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

    /**
     * redis缓存登录用户key
     */
    public static final String CACHE_KEY = "USER-LOGIN-DATA";

    /**
     * 是否限制单账号登录
     */
    private boolean singleLogin = false;

    /**
     * 验证码类型
     */
    private LoginCodeEnum codeType;

    /**
     * 验证码有效期（分钟）
     */
    private Long expiration = 2L;

    /**
     * 验证码内容长度（2位）
     */
    private int length = 2;

    /**
     * 宽度
     */
    private int width = 111;

    /**
     * 验证码高度
     */
    private int height = 36;

    /**
     * 验证码字体
     */
    private String fontName;

    /**
     * 字体大小
     */
    private int fontSize = 25;

    /**
     * 获取验证码
     *
     * @return
     */
    public Captcha getCaptcha() {
        if (codeType != null) {
            return this.switchCaptcha(codeType);
        }
        return this.switchCaptcha(LoginCodeEnum.ARITHMETIC);
    }

    /**
     * 根据配置生成验证码
     *
     * @param codeType 验证码类型
     * @return
     */
    public Captcha switchCaptcha(LoginCodeEnum codeType) {
        Captcha captcha = null;
        switch (codeType) {
            case ARITHMETIC:
                //算术类型
                captcha = new FixedArithmeticCaptcha(width, height);
                captcha.setLen(length);
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(width, height);
                captcha.setLen(length);
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(width, height);
                captcha.setLen(length);
                break;
            case GIF:
                captcha = new GifCaptcha(width, height);
                captcha.setLen(length);
                break;
            case SPEC:
                captcha = new SpecCaptcha(width, height);
                captcha.setLen(length);
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(fontName)) {
            captcha.setFont(new Font(fontName, Font.PLAIN, fontSize));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            //生成随机数和运算符
            int n1 = num(1, 10);
            int n2 = num(1, 10);
            int opt = num(3);

            //计算结果
            int result = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(result);
            return chars.toCharArray();
        }
    }
}
