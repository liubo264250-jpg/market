package com.liubo.types.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 时间工具类（基于 Hutool 实现）
 *
 * @author liubo
 * @date 2026/6/28
 */
public class DateUtils {

    // ==================== 常用格式 ====================

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_MONTH = "yyyy-MM";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATETIME_COMPACT = "yyyyMMddHHmmss";

    // ==================== 获取当前时间 ====================

    /**
     * 当前时间
     */
    public static Date now() {
        return DateUtil.date();
    }

    /**
     * 当前时间字符串，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String nowStr() {
        return DateUtil.now();
    }

    /**
     * 当前日期字符串，格式：yyyy-MM-dd
     */
    public static String todayStr() {
        return DateUtil.today();
    }

    /**
     * 当前时间戳（毫秒）
     */
    public static long currentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 当前时间戳（秒）
     */
    public static long currentSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    // ==================== 格式化 ====================

    /**
     * 格式化为 yyyy-MM-dd HH:mm:ss
     */
    public static String format(Date date) {
        return DateUtil.format(date, PATTERN_DATETIME);
    }

    /**
     * 格式化为 yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return DateUtil.format(date, PATTERN_DATE);
    }

    /**
     * 格式化为 yyyy-MM
     */
    public static String formatMonth(Date date) {
        return DateUtil.format(date, PATTERN_MONTH);
    }

    /**
     * 格式化为 HH:mm:ss
     */
    public static String formatTime(Date date) {
        return DateUtil.format(date, PATTERN_TIME);
    }

    /**
     * 格式化为 yyyyMMddHHmmss
     */
    public static String formatCompact(Date date) {
        return DateUtil.format(date, PATTERN_DATETIME_COMPACT);
    }

    /**
     * 自定义格式化
     */
    public static String format(Date date, String pattern) {
        return DateUtil.format(date, pattern);
    }

    // ==================== 解析 ====================

    /**
     * 字符串解析为 Date，格式：yyyy-MM-dd HH:mm:ss
     */
    public static Date parse(String str) {
        return DateUtil.parse(str, PATTERN_DATETIME);
    }

    /**
     * 字符串解析为 Date，格式：yyyy-MM-dd
     */
    public static Date parseDate(String str) {
        return DateUtil.parse(str, PATTERN_DATE);
    }

    /**
     * 字符串解析为 Date，自定义 pattern
     */
    public static Date parse(String str, String pattern) {
        return DateUtil.parse(str, pattern);
    }

    // ==================== 类型转换 ====================

    /**
     * Date 转时间戳（毫秒）
     */
    public static long toEpochMilli(Date date) {
        return date == null ? 0L : date.getTime();
    }

    /**
     * Date 转时间戳（秒）
     */
    public static long toEpochSecond(Date date) {
        return date == null ? 0L : date.getTime() / 1000;
    }

    /**
     * 时间戳（毫秒）转 Date
     */
    public static Date fromEpochMilli(long epochMilli) {
        return DateUtil.date(epochMilli);
    }

    /**
     * 时间戳（秒）转 Date
     */
    public static Date fromEpochSecond(long epochSecond) {
        return DateUtil.date(epochSecond * 1000);
    }

    // ==================== 加减运算 ====================

    public static Date plusYears(Date date, int years) {
        return DateUtil.offset(date, DateField.YEAR, years);
    }

    public static Date plusMonths(Date date, int months) {
        return DateUtil.offset(date, DateField.MONTH, months);
    }

    public static Date plusDays(Date date, int days) {
        return DateUtil.offsetDay(date, days);
    }

    public static Date plusHours(Date date, int hours) {
        return DateUtil.offsetHour(date, hours);
    }

    public static Date plusMinutes(Date date, int minutes) {
        return DateUtil.offsetMinute(date, minutes);
    }

    public static Date plusSeconds(Date date, int seconds) {
        return DateUtil.offsetSecond(date, seconds);
    }

    public static Date minusYears(Date date, int years) {
        return DateUtil.offset(date, DateField.YEAR, -years);
    }

    public static Date minusMonths(Date date, int months) {
        return DateUtil.offset(date, DateField.MONTH, -months);
    }

    public static Date minusDays(Date date, int days) {
        return DateUtil.offsetDay(date, -days);
    }

    public static Date minusHours(Date date, int hours) {
        return DateUtil.offsetHour(date, -hours);
    }

    public static Date minusMinutes(Date date, int minutes) {
        return DateUtil.offsetMinute(date, -minutes);
    }

    public static Date minusSeconds(Date date, int seconds) {
        return DateUtil.offsetSecond(date, -seconds);
    }

    // ==================== 时间差 ====================

    /**
     * 相差天数（绝对值）
     */
    public static long betweenDays(Date start, Date end) {
        return DateUtil.between(start, end, DateUnit.DAY);
    }

    /**
     * 相差小时数（绝对值）
     */
    public static long betweenHours(Date start, Date end) {
        return DateUtil.between(start, end, DateUnit.HOUR);
    }

    /**
     * 相差分钟数（绝对值）
     */
    public static long betweenMinutes(Date start, Date end) {
        return DateUtil.between(start, end, DateUnit.MINUTE);
    }

    /**
     * 相差秒数（绝对值）
     */
    public static long betweenSeconds(Date start, Date end) {
        return DateUtil.between(start, end, DateUnit.SECOND);
    }

    // ==================== 判断 ====================

    /**
     * 是否在 start 和 end 之间（含边界）
     */
    public static boolean isBetween(Date target, Date start, Date end) {
        return DateUtil.isIn(target, start, end);
    }

    /**
     * 是否是今天
     */
    public static boolean isToday(Date date) {
        return DateUtil.isSameDay(date, DateUtil.date());
    }

    /**
     * 是否已过期（早于当前时间）
     */
    public static boolean isExpired(Date date) {
        return date.before(DateUtil.date());
    }

    /**
     * date1 是否在 date2 之前
     */
    public static boolean isBefore(Date date1, Date date2) {
        return date1.before(date2);
    }

    /**
     * date1 是否在 date2 之后
     */
    public static boolean isAfter(Date date1, Date date2) {
        return date1.after(date2);
    }

    // ==================== 边界时间 ====================

    /**
     * 当天开始时间 00:00:00
     */
    public static Date startOfDay(Date date) {
        return DateUtil.beginOfDay(date);
    }

    /**
     * 当天结束时间 23:59:59
     */
    public static Date endOfDay(Date date) {
        return DateUtil.endOfDay(date);
    }

    /**
     * 本月第一天 00:00:00
     */
    public static Date startOfMonth(Date date) {
        return DateUtil.beginOfMonth(date);
    }

    /**
     * 本月最后一天 23:59:59
     */
    public static Date endOfMonth(Date date) {
        return DateUtil.endOfMonth(date);
    }

    /**
     * 本周周一 00:00:00
     */
    public static Date startOfWeek(Date date) {
        return DateUtil.beginOfWeek(date);
    }

    /**
     * 本周周日 23:59:59
     */
    public static Date endOfWeek(Date date) {
        return DateUtil.endOfWeek(date);
    }
}