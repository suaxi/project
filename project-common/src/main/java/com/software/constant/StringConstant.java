package com.software.constant;

import io.swagger.annotations.ApiModel;

/**
 * @author Wang Hao
 * @date 2022/10/9 22:58
 */
@ApiModel("字符串常量")
public interface StringConstant {

    /**
     * 环境
     */
    String ENV_DEV = "dev";
    String ENV_TEST = "test";
    String ENV_PROD = "prod";

    /**
     * 年
     */
    String YEAR = "YEAR";

    /**
     * 季
     */
    String QUARTER = "QUARTER";

    /**
     * 月
     */
    String MONTH = "MONTH";

    /**
     * 四则运算
     */
    String ADD = "+";
    String SUBTRACT = "-";;
    String MULTIPLY = "*";;
    String DIVIDE = "/";;

    /**
     * 逻辑运算
     */
    String GT = ">";
    String GTE = ">=";
    String LT = "<";
    String LTE = "<=";
    String EQ = "=";
    String NE = "!=";

    String AMPERSAND = "&";
    String AND = "and";
    String AT = "@";
    String ASTERISK = "*";
    String STAR = "*";
    String BACK_SLASH = "\\";
    String COLON = ":";
    String DOUBLE_COLON = "::";
    String COMMA = ",";
    String DASH = "-";
    String DOLLAR = "$";
    String DOT = ".";
    String UNDER_LINE = "_";
    String DOUBLE_DOT = "..";
    String DOT_CLASS = ".class";
    String DOT_JAVA = ".java";
    String DOT_XML = ".xml";
    String DOT_SHP = ".shp";
    String EMPTY = "";
    String EQUALS = "=";
    String FALSE = "FALSE";
    String SLASH = "/";
    String HASH = "#";
    String HAT = "^";
    String LEFT_BRACE = "{";
    String LEFT_BRACKET = "(";
    String LEFT_CHEV = "<";
    String DOT_NEWLINE = ",\n";
    String NEWLINE = "\n";
    String N = "n";
    String NO = "no";
    String NULL = "null";
    String OFF = "off";
    String ON = "on";
    String PERCENT = "%";
    String PIPE = "|";
    String PLUS = "+";
    String QUESTION_MARK = "?";
    String EXCLAMATION_MARK = "!";
    String QUOTE = "\"";
    String RETURN = "\r";
    String TAB = "\t";
    String RIGHT_BRACE = "}";
    String RIGHT_BRACKET = ")";
    String RIGHT_CHEV = ">";
    String SEMICOLON = ";";
    String SINGLE_QUOTE = "'";
    String BACKTICK = "`";
    String SPACE = " ";
    String TILDA = "~";
    String LEFT_SQ_BRACKET = "[";
    String RIGHT_SQ_BRACKET = "]";
    String TRUE = "TRUE";
    String UNDERSCORE = "_";
    String UTF_8 = "UTF-8";
    String US_ASCII = "US-ASCII";
    String ISO_8859_1 = "ISO-8859-1";
    String Y = "y";
    String YES = "yes";
    String ZERO = "0";
    String ONE = "1";
    String TWO = "2";
    String DOLLAR_LEFT_BRACE = "${";
    String HASH_LEFT_BRACE = "#{";
    String CRLF = "\r\n";
    String ASC = "ASC";
    String DESC = "DESC";
}
