package com.software.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author Wang Hao
 * @date 2023/1/2 21:45
 */
@Slf4j
public class DateUtils {

    /**
     * 日期格式
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * 日期格式(简化)
     */
    public static final String PATTERN_SIMPLE_DATE = "yyyyMMdd";

    /**
     * 日期格式（本地）
     */
    public static final String PATTERN_LOCAL_DATE = "yyyy年MM月dd日";

    /**
     * 日期时间格式
     */
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间格式（简化）
     */
    public static final String PATTERN_SIMPLE_DATE_TIME = "yyyyMMddHHmmss";

    /**
     * 日期时间格式（本地）
     */
    public static final String PATTERN_LOCAL_DATE_TIME = "yyyy年MM月dd日 HH时mm分ss秒";

    /**
     * 系统默认失时区
     */
    private final ZoneId ZONE_ID = ZoneId.systemDefault();

    /**
     * 时区
     */
    private final ZoneOffset ZONE_OFFSET = ZoneOffset.of("+8");

    private LocalDateTime localDateTime = null;

    private static List<String> dateFormatPattern = new ArrayList<>();

    static {
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss.S z");
        dateFormatPattern.add("yyyy-MM-dd G HH:mm:ss.S z");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss.S 'UTC'");
        dateFormatPattern.add("yyyy-MM-dd G HH:mm:ss.S 'UTC'");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss.S z");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss.S a");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ssz");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss z");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ss 'UTC'");
        dateFormatPattern.add("yyyy-MM-dd'T'HH:mm:ss.SX");
        dateFormatPattern.add("yyyy-MM-dd'T'HH:mm:ssX");
        dateFormatPattern.add("yyyy-MM-dd'T'HH:mmX");
        dateFormatPattern.add("yyyy-MM-dd HH:mm:ssa");
        dateFormatPattern.add("yyyy/MM/dd");
        dateFormatPattern.add("yyyy/M/d");
        dateFormatPattern.add("yyyy-MM-dd");
        dateFormatPattern.add("yyyy.M.d");
        dateFormatPattern.add("yyyy-M-d");
        dateFormatPattern.add("yyyy/M/d");
        dateFormatPattern.add("yyyy年M月d日");
        dateFormatPattern.add("yyyy年MM月dd日");
        dateFormatPattern.add("yyyy-MM-dd'T'HH:mm:ss.SSS+0800");
    }

    /**
     * 构造函数
     */
    public DateUtils() {
        this.localDateTime = LocalDateTime.now();
    }

    /**
     * 构造函数
     *
     * @param localDateTime
     */
    public DateUtils(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    /**
     * 构造函数
     *
     * @param date
     */
    public DateUtils(Date date) {
        this.localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZONE_ID);
    }

    /**
     * 构造函数
     *
     * @param milliseconds
     */
    public DateUtils(long milliseconds) {
        this.localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZONE_ID);
    }

    /**
     * 构造函数
     *
     * @param dateStr
     */
    public DateUtils(String dateStr) {
        this.localDateTime = parse(dateStr);
    }

    /**
     * 构造函数
     *
     * @param dateStr
     * @param pattern
     */
    public DateUtils(String dateStr, String pattern) {
        this.localDateTime = parse(dateStr, pattern);
    }

    /**
     * 获取时间戳（13位）
     *
     * @return
     */
    public long getTimestamp() {
        return this.localDateTime.toInstant(ZONE_OFFSET).toEpochMilli();
    }

    /**
     * 获取时间戳（10位）
     *
     * @return
     */
    public long getTimestamp10() {
        return this.localDateTime.toEpochSecond(ZONE_OFFSET);
    }

    /**
     * 获取年
     *
     * @return 年
     */
    public int getYear() {
        return this.localDateTime.get(ChronoField.YEAR);
    }

    /**
     * 获取月份
     *
     * @return 月份
     */
    public int getMonth() {
        return this.localDateTime.get(ChronoField.MONTH_OF_YEAR);
    }

    /**
     * 获取某月的第几天
     *
     * @return 几号
     */
    public int getMonthOfDay() {
        return this.localDateTime.get(ChronoField.DAY_OF_MONTH);
    }

    /**
     * @return
     */
    public Date getDate() {
        return Date.from(this.localDateTime.atZone(ZONE_ID).toInstant());
    }

    /**
     * 获得某天最小时间 yyyy-MM-dd 00:00:00
     *
     * @return
     */
    public DateUtils startTimeOfDay() {
        this.localDateTime = this.localDateTime.with(LocalTime.MIN);
        return this;
    }

    /**
     * 获得某天最大时间 yyyy-MM-dd 23:59:59
     *
     * @return
     */
    public DateUtils endTimeOfDay() {
        this.localDateTime = this.localDateTime.with(LocalTime.MAX);
        return this;
    }

    /**
     * 获取月份第一天
     *
     * @return
     */
    public DateUtils startDayOfMonth() {
        this.localDateTime = this.localDateTime.with(TemporalAdjusters.firstDayOfMonth());
        return this;
    }

    /**
     * 获取月份第最后一天
     *
     * @return
     */
    public DateUtils endDayOfMonth() {
        this.localDateTime = this.localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        return this;
    }

    /**
     * 获取年份一月
     *
     * @return
     */
    public DateUtils startMonthOfYear() {
        this.localDateTime = this.localDateTime.withMonth(1);
        return this;
    }

    /**
     * 获取年份12月
     *
     * @return
     */
    public DateUtils endMonthOfYear() {
        this.localDateTime = this.localDateTime.withMonth(12);
        return this;
    }

    /**
     * 增加天数
     *
     * @param days
     * @return
     */
    public DateUtils plusDays(long days) {
        this.localDateTime = this.localDateTime.plusDays(days);
        return this;
    }

    /**
     * 增加月数
     *
     * @param months
     * @return
     */
    public DateUtils plusMonths(long months) {
        this.localDateTime = this.localDateTime.plusMonths(months);
        return this;
    }

    /**
     * 增加年数
     *
     * @param years
     * @return
     */
    public DateUtils plusYears(long years) {
        this.localDateTime = this.localDateTime.plusYears(years);
        return this;
    }

    /**
     * 减少天数
     *
     * @param days
     * @return
     */
    public DateUtils minusDays(long days) {
        this.localDateTime = this.localDateTime.minusDays(days);
        return this;
    }

    /**
     * 减少月数
     *
     * @param months
     * @return
     */
    public DateUtils minusMonths(long months) {
        this.localDateTime = this.localDateTime.minusMonths(months);
        return this;
    }

    /**
     * 减少年数
     *
     * @param years
     * @return
     */
    public DateUtils minusYears(long years) {
        this.localDateTime = this.localDateTime.minusYears(years);
        return this;
    }

    /**
     * 格式化日期为字符串
     *
     * @param pattern
     * @return 日期字符串
     */
    public String format(String pattern) {
        return this.localDateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.CHINA));
    }

    /**
     * 格式化日期为字符串（标准日期: yyyy-MM-dd）
     *
     * @return
     */
    public String formatToDate() {
        return format(PATTERN_DATE);
    }

    /**
     * 格式化日期为字符串（标准日期时间: yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public String formatToDateTime() {
        return format(PATTERN_DATE_TIME);
    }

    /**
     * 格式化日期为字符串（标准日期: yyyyMMdd）
     *
     * @return
     */
    public String formatToSimpleDate() {
        return format(PATTERN_SIMPLE_DATE);
    }

    /**
     * 格式化日期为字符串（标准日期时间: yyyyMMddHHmmss）
     *
     * @return
     */
    public String formatToSimpleDateTime() {
        return format(PATTERN_SIMPLE_DATE_TIME);
    }

    /**
     * 格式化日期为字符串（本地日期: yyyy年MM月dd日）
     *
     * @return
     */
    public String formatToLocalDate() {
        return format(PATTERN_LOCAL_DATE);
    }

    /**
     * 格式化日期为字符串（本地日期时间: yyyy年MM月dd日 HH时mm分ss秒）
     *
     * @return
     */
    public String formatToLocalDateTime() {
        return format(PATTERN_LOCAL_DATE_TIME);
    }

    /**
     * 解析字符串日期
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    private LocalDateTime parse(String dateStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.CHINA);
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
        } catch (DateTimeParseException e1) {
            try {
                LocalDate localDate = LocalDate.parse(dateStr, dateTimeFormatter);
                localDateTime = Objects.isNull(localDate) ? null : localDate.atStartOfDay();
            } catch (DateTimeParseException e2) {
                throw e2;
            }
        }
        return localDateTime;
    }

    /**
     * 解析字符串日期 (格式未知)
     *
     * @param dateString
     * @return
     */
    private LocalDateTime parse(String dateString) throws DateTimeException {
        for (String formatPattern : dateFormatPattern) {
            try {
                return parse(dateString, formatPattern);
            } catch (DateTimeParseException e) {
                log.debug("日期格式[{}]解析失败", formatPattern);
            }
        }
        throw new DateTimeException("无法解析字符串[" + dateString + "]为日期类型");
    }

    /**
     * 判断当前时间是否在指定时间范围
     *
     * @param from 开始时间
     * @param to   结束时间
     * @return 结果
     */
    public static boolean between(LocalTime from, LocalTime to) {
        LocalTime now = LocalTime.now();
        return now.isAfter(from) && now.isBefore(to);
    }

}
