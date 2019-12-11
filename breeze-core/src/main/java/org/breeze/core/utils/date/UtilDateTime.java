package org.breeze.core.utils.date;

import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Description:
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 21:40
 * @Version: 1.0.0
 */
public class UtilDateTime {

    public static final String datePartition = "-";
    private static Log log = LogFactory.getLog(UtilDateTime.class);
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 得到当前系统实现（毫秒级）
     *
     * @return
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 得到当前系统实现（秒级）
     *
     * @return
     */
    public static long currentTimeSeconds() {
        return currentTimeMillis() / 1000;
    }


    /**
     * 获取当前日期及时间字符串 格式为YYYY-MM-DD HH:mm:ss.SSS
     *
     * @return java.lang.String
     */
    public static String nowMillisTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String s = df.format(System.currentTimeMillis());
        return s;
    }

    /**
     * @return long    返回类型
     * @throws
     * @Title: getDateMillis
     * @Description: 得到时间的毫秒数
     * @author: songhn
     */
    public static long getDateMillis(String dateStr, String formatStr) {
        if (formatStr == null) {
            formatStr = DEFAULT_FORMAT;
        }
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat(formatStr).parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }

    /**
     * @return long    返回类型
     * @throws
     * @Title: getDateMillisPhp
     * @Description: 得到支持PHP的时间毫秒数
     * @author: songhn
     */
    public static long getDateMillisPhp(String dateStr, String formatStr) {
        if (formatStr == null) {
            formatStr = DEFAULT_FORMAT;
        }
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat(formatStr).parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis() / 1000;
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String nowDateTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * yyyy-MM-dd
     *
     * @return
     */
    public static String nowDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
    }

    /**
     * @return String
     * @Description: yyyyMMdd  纯数字日期字符串
     * @auther lidecai
     * @date 2015年12月29日
     */
    public static String nowDateNumString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());
    }

    /**
     * 得到当前的年月，格式为yyyyMM
     *
     * @return
     */
    public static String nowDateYearMonthString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        return df.format(new Date());
    }

    /**
     * 得到当前的年，格式为yyyy
     *
     * @return
     */
    public static String nowDateYearString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(new Date());
    }

    /**
     * @return String
     * @Description: yyyyMMddHHmmss  纯数字时间字符串
     * @auther lidecai
     * @date 2015年12月29日
     */
    public static String nowDateTimeNumString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    // add by 刘欣宇 取给定日期的下一天,输入:2008-7-9,返回2008-7-10
    public static String getNextDay(String time) {
        int year = Integer.parseInt(time.substring(0, time.indexOf("-")));
        int month = Integer.parseInt(time.substring(time.indexOf("-") + 1, time
                .lastIndexOf("-")));
        int day = Integer.parseInt(time.substring(time.lastIndexOf("-") + 1));
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar nextDate = Calendar.getInstance();
        nextDate.set(year, month - 1, day);
        nextDate.add(Calendar.DAY_OF_MONTH, 1);
        Date date = nextDate.getTime();
        String nextdaystr = f2.format(date);
        return nextdaystr;
    }

    // add by 张亮 (Felankia)

    /**
     * @param d
     * @return yyyy年
     */
    public static String formatCNYear(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年");
        return df.format(d);
    }

    // add by 张亮 (Felankia)

    /**
     * yyyy年MM月
     *
     * @param d
     * @return
     */
    public static String formatCNMonth(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月");
        return df.format(d);
    }

    // add by 张亮 (Felankia)

    /**
     * yyyy年MM月dd日
     *
     * @param d
     * @return
     */
    public static String formatCNDate(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(d);
    }

    // add by 张亮 (Felankia)

    /**
     * yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param d
     * @return
     */
    public static String formatCNTime(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return df.format(d);
    }

    // add by 张亮 (Felankia)
    public static String formatDate(Date d, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    /**
     * 返回当前时间 格式：2004-03-12 16:34:16.594
     *
     * @return java.sql.Timestamp对象
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 返回当前日期 格式：Fri Mar 12 16:34:16 CST 2004
     *
     * @return java.util.Date对象
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 返回当前日期 格式：2004-03-12
     *
     * @return java.sql.Date
     */
    public static Date nowSqlDate() {
        return new java.sql.Date(nowDate().getTime());
    }

    /**
     * 取得给定时间的开始时间 例如：2003-10-12 00:00:00.0
     *
     * @param stamp 给定的日期
     * @return 返回当天的起始时间
     */
    public static Timestamp getDayStart(Timestamp stamp) {
        return getDayStart(stamp, 0);
    }

    /**
     * 取得给定时间多少天以后的开始时间 例如：2003-10-12 00:00:00.0
     *
     * @param stamp     给定的日期
     * @param daysLater 推迟的天数
     * @return 返回推迟天数以后的起始时间
     */
    public static Timestamp getDayStart(Timestamp stamp,
                                        int daysLater) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH),
                tempCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 给定时间的下一天的开始时间
     *
     * @param stamp 日期
     * @return 得到的日期
     */
    public static Timestamp getNextDayStart(Timestamp stamp) {
        return getDayStart(stamp, 1);
    }

    /**
     * 给定时间当天的结束时间
     *
     * @param stamp 日期
     * @return 得到的日期
     */
    public static Timestamp getDayEnd(Timestamp stamp) {
        return getDayEnd(stamp, 0);
    }

    /**
     * 取得给定时间多少天以后的结束时间 例如：2003-10-12 00:00:00.0
     *
     * @param stamp     给定的日期
     * @param daysLater 推迟的天数
     * @return 返回推迟天数以后的结束时间
     */
    public static Timestamp getDayEnd(Timestamp stamp,
                                      int daysLater) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH),
                tempCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 将字符串的日期转换为java.sql.Date格式
     *
     * @param date 字符串格式的日期
     * @return A java.sql.Date made from the date String
     */
    public static java.sql.Date toSqlDate(String date) {
        Date newDate = toDate(date, "00:00:00");

        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 将字符串的日期转换为java.sql.Date格式
     *
     * @param dateTime 字符串格式的日期+时间
     * @return A java.sql.Date made from the dateTime String
     */
    public static java.sql.Date toSqlDateForDateTime(String dateTime) {
        Date newDate = toDate(dateTime);
        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 创建一个日期对象 java.sql.Date
     *
     * @param monthStr 月的字符串
     * @param dayStr   日的字符串
     * @param yearStr  年的字符串
     * @return 一个java.sql.Date类型的日期格式
     */
    public static java.sql.Date toSqlDate(String monthStr, String dayStr,
                                          String yearStr) {
        Date newDate = toDate(monthStr, dayStr, yearStr, "0", "0",
                "0");

        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 创建一个日期对象 java.sql.Date
     *
     * @param month 月的数字
     * @param day   日的数字
     * @param year  年的数字
     * @return 一个java.sql.Date类型的日期格式
     */
    public static java.sql.Date toSqlDate(int month, int day, int year) {
        Date newDate = toDate(month, day, year, 0, 0, 0);

        if (newDate != null) {
            return new java.sql.Date(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 将时间格式的字符串转换为java.sql.Time类型
     *
     * @param time 时间字符串: 格式 HH:MM:SS
     * @return 转换完成的java.sql.Time
     */
    public static java.sql.Time toSqlTime(String time) {
        // java.util.Date newDate = toDate("1/1/1970", time);
        Date newDate = toDate("1970" + datePartition + "1"
                + datePartition + "1", time);
        if (newDate != null) {
            return new java.sql.Time(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 构造java.sql.Time类型
     *
     * @param hourStr   小时的字符串
     * @param minuteStr 分钟的字符串
     * @param secondStr 秒的字符串
     * @return 转换完成的java.sql.Time
     */
    public static java.sql.Time toSqlTime(String hourStr, String minuteStr,
                                          String secondStr) {
        Date newDate = toDate("0", "0", "0", hourStr, minuteStr,
                secondStr);

        if (newDate != null) {
            return new java.sql.Time(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 构造java.sql.Time类型
     *
     * @param hour   小时的整数
     * @param minute 分钟的整数
     * @param second 秒的整数
     * @return 转换完成的java.sql.Time
     */
    public static java.sql.Time toSqlTime(int hour, int minute, int second) {
        Date newDate = toDate(0, 0, 0, hour, minute, second);

        if (newDate != null) {
            return new java.sql.Time(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 将给定的日期字符串转换为Timestamp类型
     *
     * @param dateTime 日期时间字符串 例如："2003-10-12 15:20:30"
     * @return 转换后的java.sql.Timestamp
     */
    public static Timestamp toTimestamp(String dateTime) {
        Date newDate = toDate(dateTime);
        if (newDate != null) {
            return new Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 将给定的日期字符串转换为Timestamp类型
     *
     * @param date 日期字符串 例如："2003-10-12"
     * @param time 时间字符串 例如："10:20:59"
     * @return 转换后的java.sql.Timestamp
     */
    public static Timestamp toTimestamp(String date, String time) {
        if ((date == null) || (time == null)) {
            return null;
        }
        return toTimestamp(date + " " + time);
    }

    /**
     * 将给定的日期字符串转换为Timestamp类型
     *
     * @param monthStr  月份字符串
     * @param dayStr    日字符串
     * @param yearStr   年字符串
     * @param hourStr   小时字符串
     * @param minuteStr 分钟字符串
     * @param secondStr 秒字符串
     * @return 转换后的java.sql.Timestamp
     */
    public static Timestamp toTimestamp(String monthStr,
                                        String dayStr, String yearStr, String hourStr, String minuteStr,
                                        String secondStr) {
        Date newDate = toDate(monthStr, dayStr, yearStr, hourStr,
                minuteStr, secondStr);

        if (newDate != null) {
            return new Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    /**
     * 将给定的日期字符串转换为Timestamp类型
     *
     * @param month  月份整数
     * @param day    日整数
     * @param year   年整数
     * @param hour   小时整数
     * @param minute 分钟整数
     * @param second 秒整数
     * @return 转换后的java.sql.Timestamp
     */
    public static Timestamp toTimestamp(int month, int day, int year,
                                        int hour, int minute, int second) {
        Date newDate = toDate(month, day, year, hour, minute, second);

        if (newDate != null) {
            return new Timestamp(newDate.getTime());
        } else {
            return null;
        }
    }

    public static Date toDateFormat(String dateTime, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将日期字符串转换为java.util.Date类型
     *
     * @param dateTime 日期时间格式的字符串 例如："2003-10-12 15:20:30"
     * @return 转换后的java.util.Date
     */
    public static Date toDate(String dateTime) {

        // String date = dateTime.substring(0, dateTime.indexOf(" "));
        // String time = dateTime.substring(dateTime.indexOf(" ") + 1);
        //
        // return toDate(date, time);

        try {
            return new Date(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                    DateFormat.MEDIUM, Locale.CHINA).parse(dateTime).getTime());
        } catch (ParseException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 将日期、时间字符串转换为java.util.Date类型
     *
     * @param date 日期字符串 格式：YYYY-MM-DD
     * @param time 时间字符串 格式： HH:MM or HH:MM:SS
     * @return 转换后的java.util.Date
     */
    public static Date toDate(String date, String time) {
        if ((date == null) || (time == null)) {
            return null;
        }

        return toDate(date + " " + time);
    }

    /**
     * 将日期、时间字符串转换为java.util.Date类型
     *
     * @param monthStr  月份字符串
     * @param dayStr    日字符串
     * @param yearStr   年字符串
     * @param hourStr   小时字符串
     * @param minuteStr 分钟字符串
     * @param secondStr 秒字符串
     * @return 转换后的java.util.Date
     */
    public static Date toDate(String monthStr, String dayStr,
                              String yearStr, String hourStr, String minuteStr, String secondStr) {
        int month, day, year, hour, minute, second;
        try {
            month = Integer.parseInt(monthStr);
            day = Integer.parseInt(dayStr);
            year = Integer.parseInt(yearStr);
            hour = Integer.parseInt(hourStr);
            minute = Integer.parseInt(minuteStr);
            second = Integer.parseInt(secondStr);
        } catch (Exception e) {
            return null;
        }
        return toDate(month, day, year, hour, minute, second);
    }

    /**
     * 将日期、时间的整数串转换为java.util.Date类型
     *
     * @param month  月份整数
     * @param day    日整数
     * @param year   年整数
     * @param hour   小时整数
     * @param minute 分钟整数
     * @param second 秒整数
     * @return 转换后的java.util.Date
     */
    public static Date toDate(int month, int day, int year, int hour,
                              int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.set(year, month - 1, day, hour, minute, second);
        } catch (Exception e) {
            return null;
        }
        return new Date(calendar.getTime().getTime());
    }

    /**
     * 将java.util.Date类型转换为字符串,取日期 格式: YYYY-MM-DD
     *
     * @param date 日期
     * @return 转换后的字符串 格式: YYYY-MM-DD
     */
    public static String toDateString(Date date) {
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String monthStr;
        String dayStr;
        String yearStr;

        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = "" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        yearStr = "" + year;
        // return monthStr + "/" + dayStr + "/" + yearStr;
        return yearStr + "-" + monthStr + "-" + dayStr;
    }

    /**
     * 将java.util.Date中的时间取出来，转换为Time格式的字符串 格式：HH:MM:SS
     *
     * @param date 日期时间
     * @return 转换后的字符串 格式：HH:MM:SS
     */
    public static String toTimeString(Date date) {
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        return toTimeString(calendar.get(Calendar.HOUR_OF_DAY), calendar
                .get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    /**
     * 将时间的整数转换为标准格式的字符串 格式：HH:MM:SS
     *
     * @param hour   小时整数
     * @param minute 分钟整数
     * @param second 秒整数
     * @return 符合格式的字符串
     */
    public static String toTimeString(int hour, int minute, int second) {
        String hourStr;
        String minuteStr;
        String secondStr;

        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (minute < 10) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        if (second < 10) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        if (second == 0) {
            return hourStr + ":" + minuteStr + ":" + secondStr;
        } else {
            return hourStr + ":" + minuteStr + ":" + secondStr;
        }
    }

    /**
     * 将java.util.Date中的时间取出来，转换为DateTime格式的字符串 格式：YYYY-MM-DD HH:MM:SS
     *
     * @param date 日期时间
     * @return 转换后的字符串 格式：YYYY-MM-DD HH:MM:SS
     */
    public static String toDateTimeString(Date date) {
        if (date == null) {
            return "";
        }
        String dateString = toDateString(date);
        String timeString = toTimeString(date);

        if ((dateString != null) && (timeString != null)) {
            return dateString + " " + timeString;
        } else {
            return "";
        }
    }

    /**
     * 将java.sql.Date中的时间取出来，转换为DateTime格式的字符串 格式：YYYY-MM-DD HH:MM:SS
     *
     * @param date 日期时间
     * @return 转换后的字符串 格式：YYYY-MM-DD HH:MM:SS
     */
    public static String toDateTimeString(java.sql.Date date) {
        if (date == null) {
            return "";
        }
        return toDateTimeString(new Date(date.getTime()));
    }

    /**
     * 得到当前月开始那天的开始时间
     *
     * @return java.sql.Timestamp类型
     */
    public static Timestamp monthBegin() {
        Calendar mth = Calendar.getInstance();
        mth.set(Calendar.DAY_OF_MONTH, 1);
        mth.set(Calendar.HOUR_OF_DAY, 0);
        mth.set(Calendar.MINUTE, 0);
        mth.set(Calendar.SECOND, 0);
        mth.set(Calendar.AM_PM, Calendar.AM);
        return new Timestamp(mth.getTime().getTime());
    }

    /**
     * 得到当前月结束那天的结束时间
     */
    public static Timestamp monthEnd() {
        Calendar mth = Calendar.getInstance();

        mth.set(Calendar.DAY_OF_MONTH, mth
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        mth.set(Calendar.HOUR_OF_DAY, 23);
        mth.set(Calendar.MINUTE, 59);
        mth.set(Calendar.SECOND, 59);
        return new Timestamp(mth.getTime().getTime());
    }

    /**
     * 得到月
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD
     * @return month
     */
    public static String getMonth(String date) {
        if ((date == null) || date.equals("")) {
            return "";
        }
        int dateSlash1 = date.indexOf(UtilDateTime.datePartition);
        int dateSlash2 = date.lastIndexOf(UtilDateTime.datePartition);
        return date.substring(dateSlash1 + 1, dateSlash2);
    }

    /**
     * 得到季度
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD
     * @return 当前季度 String
     */
    public static String getQuarter(String date) {
        int month = Integer.parseInt(getMonth(date));
        if (month < 4) {
            return "1";
        } else if (month < 7) {
            return "2";
        } else if (month < 10) {
            return "3";
        } else if (month < 13) {
            return "4";
        } else {
            return null;
        }
    }

    /**
     * 得到季度
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD
     * @return 当前季度 int
     */
    public static int getQuarterInt(String date) {
        int month = Integer.parseInt(getMonth(date));
        if (month < 4) {
            return 1;
        } else if (month < 7) {
            return 2;
        } else if (month < 10) {
            return 3;
        } else if (month < 13) {
            return 4;
        } else {
            return 0;
        }
    }

    /**
     * 得到年
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD
     * @return month
     */
    public static String getYear(String date) {
        if ((date == null) || date.equals("")) {
            return "";
        }
        int dateSlash1 = date.indexOf(UtilDateTime.datePartition);
        return date.substring(0, dateSlash1);
    }

    /**
     * 得到日
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD
     * @return Day
     */
    public static String getDay(String date) {
        if ((date == null) || date.equals("")) {
            return "";
        }
        int dateSlash2 = date.lastIndexOf(UtilDateTime.datePartition);
        return date.substring(dateSlash2 + 1, date.length() < dateSlash2 + 3 ? date.length() : dateSlash2 + 3);
    }

    /**
     * 得到小时
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD HH:MM:SS
     * @return
     */
    public static String getHour(String date) {
        if ((date == null) || date.equals("")) {
            return "";
        }
        int dateSlash1 = date.indexOf(":");
        return date.substring(dateSlash1 - 2 < 0 ? 0 : dateSlash1 - 2, dateSlash1).trim();
    }

    /**
     * 得到分钟
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD HH:MM:SS
     * @return
     */
    public static String getMinute(String date) {
        if ((date == null) || date.equals("")) {
            return "";
        }
        int dateSlash1 = date.indexOf(":");
        int dateSlash2 = date.lastIndexOf(":");
        if (dateSlash1 == dateSlash2) {
            return date.substring(dateSlash1 + 1, date.length() < dateSlash2 + 3 ? date.length() : dateSlash2 + 3).trim();
        }
        return date.substring(dateSlash1 + 1, dateSlash2).trim();
    }

    /**
     * 得到秒
     *
     * @param date 日期格式的字符串，格式：YYYY-MM-DD HH:MM:SS
     * @return
     */
    public static String getSecond(String date) {
        if ((date == null) || date.equals("")) {
            return "";
        }
        int dateSlash1 = date.indexOf(":");
        int dateSlash2 = date.lastIndexOf(":");
        if (dateSlash1 == dateSlash2) {
            return "0";
        }
        return date.substring(dateSlash2 + 1, date.length() < dateSlash2 + 3 ? date.length() : dateSlash2 + 3).trim();
    }

    /**
     * 得到年
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getYear(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getYear(str);
    }

    /**
     * 得到季度
     *
     * @param date java.sql.Date
     * @return
     */
    public static int getQuarterInt(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getQuarterInt(str);
    }

    /**
     * 得到季度
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getQuarter(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getQuarter(str);
    }

    /**
     * 得到年
     *
     * @param date java.util.Date
     * @return
     */
    public static String getYear(Date date) {
        String str = toDateTimeString(date);
        return getYear(str);
    }

    /**
     * 得到月
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getMonth(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getMonth(str);
    }

    /**
     * 得到月
     *
     * @param date java.util.Date
     * @return
     */
    public static String getMonth(Date date) {
        String str = toDateTimeString(date);
        return getMonth(str);
    }

    /**
     * 得到季度
     *
     * @param date java.sql.Date
     * @return
     */
    public static int getQuarterInt(Date date) {
        String str = toDateTimeString(date);
        return getQuarterInt(str);
    }

    /**
     * 得到季度
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getQuarter(Date date) {
        String str = toDateTimeString(date);
        return getQuarter(str);
    }

    /**
     * @param year
     * @param month
     * @param isNow
     * @return
     */
    public static String getQuarter(String year, String month, boolean isNow) {
        if ((year == null) || (month == null) || "".equals(year)
                || "".equals(month)) {
            if (isNow) {
                return getQuarter(nowDate());
            } else {
                return null;
            }
        }
        String str = new StringBuffer(year).append(UtilDateTime.datePartition).append(month).append(UtilDateTime.datePartition).append("01").toString();
        return getQuarter(str);
    }

    public static int getQuarterInt(String year, String month, boolean isNow) {
        if ((year == null) || (month == null) || "".equals(year)
                || "".equals(month)) {
            if (isNow) {
                return getQuarterInt(nowDate());
            } else {
                return 0;
            }
        }
        String str = new StringBuffer(year).append(UtilDateTime.datePartition)
                .append(month).append(UtilDateTime.datePartition).append("01")
                .toString();
        return getQuarterInt(str);
    }

    /**
     * 得到日
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getDay(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getDay(str);
    }

    /**
     * 得到日
     *
     * @param date java.util.Date
     * @return
     */
    public static String getDay(Date date) {
        String str = toDateTimeString(date);
        return getDay(str);
    }

    /**
     * 得到小时
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getHour(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getHour(str);
    }

    /**
     * 得到小时
     *
     * @param date java.util.Date
     * @return
     */
    public static String getHour(Date date) {
        String str = toDateTimeString(date);
        return getHour(str);
    }

    /**
     * 得到分钟
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getMinute(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getMinute(str);
    }

    /**
     * 得到分钟
     *
     * @param date java.util.Date
     * @return
     */
    public static String getMinute(Date date) {
        String str = toDateTimeString(date);
        return getMinute(str);
    }

    /**
     * 得到秒
     *
     * @param date java.sql.Date
     * @return
     */
    public static String getSecond(java.sql.Date date) {
        String str = toDateTimeString(date);
        return getSecond(str);
    }

    /**
     * 得到秒
     *
     * @param date java.util.Date
     * @return
     */
    public static String getSecond(Date date) {
        String str = toDateTimeString(date);
        return getSecond(str);
    }

    /**
     * 得到星期1-7对应周一-周日
     *
     * @param date java.util.Date
     * @return 字符串1，2，3，4，5，6，7
     */
    public static String getWeek(Date date) {
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if (weekNum == 0) {
            weekNum = 7;
        }
        return String.valueOf(weekNum);
    }

    /**
     * 得到星期1-7对应周一-周日
     *
     * @param date java.sql.Date
     * @return 字符串1，2，3，4，5，6，7
     */
    public static String getWeek(java.sql.Date date) {
        if (date == null) {
            return "";
        }
        return getWeek(new Date(date.getTime()));
    }

    /**
     * 得到星期1-7对应周一-周日
     *
     * @param date date格式的字符串，例如：2003-12-12
     * @return 字符串1，2，3，4，5，6，7
     */
    public static String getWeek(String date) {
        return getWeek(toSqlDate(date));
    }

    /**
     * 得到当前星期1-7对应周一-周日
     *
     * @return 字符串1，2，3，4，5，6，7
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        int weekNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekNum == 0) {
            weekNum = 7;
        }
        return String.valueOf(weekNum);
    }

    public static Timestamp getTimestamp(long time) {
        return new Timestamp(time);
    }

    public static String getTimeStr(long time) {
        return new Timestamp(time).toString();
    }

    /**
     * 得到当前当前时间 格式是 (2007-03-09 16:49:07.546)
     *
     * @return 当前时间
     */
    public static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String xzsj = sdf.format(date);
        return xzsj;
    }

    /**
     * 得到消耗的时间差 开始时间减去结束时间,返回的是毫秒.
     *
     * @param startTime 格式是 (2007-03-09 16:49:07.546)
     * @param endTime   格式是 (2007-03-09 16:49:09.576)
     * @return
     */
    public static long getExpendTime(String startTime, String endTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date start;
        Date end;
        long time = 0;
        try {
            start = sdf.parse(startTime);
            end = sdf.parse(endTime);
            time = start.getTime() - end.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }


    public static void countCalc() {
        if (log.isDebug()) {
            System.out.println(new StringBuffer("执行次数:").append(imm).append("\n执行总时间：").append(countTime).append("\n执行平均时间：").append((countTime / imm)).toString());
        }
        imm = 0;
        countTime = 0;
    }

    private static int imm = 0;
    private static long countTime = 0;

    /**
     * 打印开始时间，为了查看执行时间，需要与countEnd()配合使用
     *
     * @param desc 描述
     * @return
     */
    public static long countStart(String desc) {
        if (log.isDebug()) {
            StringBuffer endS = new StringBuffer();
            long time = System.currentTimeMillis();
            log.logDebug(endS.append("***").append(desc).append("***,开始时间：[")
                    .append(new Timestamp(time)).append("]").toString());
            return time;
        } else {
            return 0;
        }
    }

    public static long countStartInfo(String desc) {
        return countStartInfo(desc, false);
    }

    public static long countStartInfo(String desc, boolean alwaysLog) {
        long time = System.currentTimeMillis();
        if (!alwaysLog) {
            return time;
        }
        StringBuffer endS = new StringBuffer();
        log.logInfo(endS.append("***").append(desc).append("***,开始时间：[").append(new Timestamp(time)).append("]").toString());
        return time;
    }

    public static long countStartSysout(String desc) {
        StringBuffer endS = new StringBuffer();
        long time = System.currentTimeMillis();
        System.out.println(endS.append("***").append(desc).append("***,开始时间：[").append(new Timestamp(time)).append("]").toString());
        return time;
    }

    /**
     * 打印结束时间，为了查看执行时间，需要与countStart()配合使用
     *
     * @param desc      描述
     * @param startTime 开始时间的long数据
     */
    public static void countEnd(String desc, long startTime) {
        if (log.isDebug()) {
            long time = System.currentTimeMillis();
            StringBuffer endS = new StringBuffer();
            log.logDebug(endS.append("***").append(desc).append("***,结束时间：[")
                    .append(new Timestamp(time)).append("]").toString());
            long t = time - startTime;
            long sec = t / (1000 * 60);
            long miao = t % (1000 * 60) / 1000;
            long haomiao = t % (1000 * 60) % 1000;
            endS = new StringBuffer();
            log.logDebug(endS.append("***").append(desc).append("***,执行时间：").append(sec).append("分").append(miao).append("秒").append(haomiao).append("毫秒,总毫秒：").append(t).toString());
        }
    }

    public static void countEndInfo(String desc, long startTime) {
        countEndInfo(desc, startTime, false);
    }

    public static void countEndInfo(String desc, long startTime, boolean alwaysLog) {
        long time = System.currentTimeMillis();
        long t = time - startTime;
        if (t > 3000 || alwaysLog) {
            StringBuffer endS = new StringBuffer();
            log.logInfo(endS.append("***").append(desc).append("***,结束时间：[")
                    .append(new Timestamp(time)).append("]").toString());
            long sec = t / (1000 * 60);
            long miao = t % (1000 * 60) / 1000;
            long haomiao = t % (1000 * 60) % 1000;
            endS = new StringBuffer();
            log.logInfo(endS.append("***").append(desc).append("***,执行时间：").append(sec).append("分").append(miao).append("秒").append(haomiao).append("毫秒,总毫秒：").append(t).toString());
        }
    }

    public static void countEndSysout(String desc, long startTime) {
        long time = System.currentTimeMillis();
        StringBuffer endS = new StringBuffer();
        System.out.println(endS.append("***").append(desc).append("***,结束时间：[")
                .append(new Timestamp(time)).append("]").toString());
        long t = time - startTime;
        long sec = t / (1000 * 60);
        long miao = t % (1000 * 60) / 1000;
        long haomiao = t % (1000 * 60) % 1000;
        endS = new StringBuffer();
        System.out.println(endS.append("***").append(desc).append("***,执行时间：").append(sec).append("分").append(miao).append("秒").append(haomiao).append("毫秒,总毫秒：").append(t).toString());
    }

    /**
     * 取得给定时间的开始时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当周的起始时间 欧美算法：每周指周日到周六
     */
    public static Timestamp getWestWeekStart(Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(Calendar.DAY_OF_WEEK, tempCal
                .getActualMinimum(Calendar.DAY_OF_WEEK));
        tempCal.set(Calendar.HOUR_OF_DAY, 0);
        tempCal.set(Calendar.MINUTE, 0);
        tempCal.set(Calendar.SECOND, 0);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 取得给定时间结束时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当周的结束时间 欧美算法：每周指周日到周六
     */
    public static Timestamp getWestWeekEnd(Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(Calendar.DAY_OF_WEEK, tempCal
                .getActualMaximum(Calendar.DAY_OF_WEEK));
        tempCal.set(Calendar.HOUR_OF_DAY, 23);
        tempCal.set(Calendar.MINUTE, 59);
        tempCal.set(Calendar.SECOND, 59);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 取得给定时间的开始时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当周的起始时间 国内算法：每周指周一到周日
     */
    public static Timestamp getWeekStart(Timestamp stamp) {
        if ("7".equals(getWeek(stamp.toString()))) {
            return getNextDayStart(getWestWeekStart(getDayStart(stamp, -1)));
        } else {
            return getNextDayStart(getWestWeekStart(stamp));
        }

    }

    /**
     * 取得给定时间结束时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当周的结束时间 国内算法：每周指周一到周日
     */
    public static Timestamp getWeekEnd(Timestamp stamp) {
        if ("7".equals(getWeek(stamp.toString()))) {
            return getDayEnd(getNextDayStart(getWestWeekEnd(getDayStart(stamp,
                    -1))));
        } else {
            return getDayEnd(getNextDayStart(getWestWeekEnd(stamp)));
        }

    }

    /**
     * 取得给定时间的开始时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当月的起始时间
     */
    public static Timestamp getMonthStart(Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(Calendar.DAY_OF_MONTH, tempCal
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        tempCal.set(Calendar.HOUR_OF_DAY, 0);
        tempCal.set(Calendar.MINUTE, 0);
        tempCal.set(Calendar.SECOND, 0);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 取得给定时间结束时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当月的结束时间
     */
    public static Timestamp getMonthEnd(Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(Calendar.DAY_OF_MONTH, tempCal
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        tempCal.set(Calendar.HOUR_OF_DAY, 23);
        tempCal.set(Calendar.MINUTE, 59);
        tempCal.set(Calendar.SECOND, 59);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 取得给定时间的开始时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当年的起始时间
     */
    public static Timestamp getYearStart(Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(Calendar.DAY_OF_YEAR, tempCal
                .getActualMinimum(Calendar.DAY_OF_YEAR));
        tempCal.set(Calendar.HOUR_OF_DAY, 0);
        tempCal.set(Calendar.MINUTE, 0);
        tempCal.set(Calendar.SECOND, 0);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 取得给定时间结束时间 例如：2003-10-12 00:00:00.0 add by mliz
     *
     * @param stamp 给定的日期
     * @return 返回当年的结束时间
     */
    public static Timestamp getYearEnd(Timestamp stamp) {
        Calendar tempCal = Calendar.getInstance();

        tempCal.setTime(new Date(stamp.getTime()));
        tempCal.set(Calendar.DAY_OF_YEAR, tempCal
                .getActualMaximum(Calendar.DAY_OF_YEAR));
        tempCal.set(Calendar.HOUR_OF_DAY, 23);
        tempCal.set(Calendar.MINUTE, 59);
        tempCal.set(Calendar.SECOND, 59);
        return new Timestamp(tempCal.getTime().getTime());
    }

    /**
     * 得到指定时间是当月的第几个周
     *
     * @param d 指定的时间对象
     * @return int 返回第几周
     */
    public static int getWeekOfMonth(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (d == null) {
            d = new Date();
        }
        calendar.setTime(d);
        int weekNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekNum == 0) {
            weekNum = 7;
        }
        int v = Integer.parseInt(getDay(d)) - weekNum;
        if (v < 0) {
            return 1;
        } else {
            return (((Integer.parseInt(getDay(d)) - weekNum) + 1) / 7) + 2;
        }
    }

    /**
     * 得到指定时间是当月的第几个周
     *
     * @param dateString 指定的日期 例如 2007-12-12
     * @return int 返回第几周
     */
    public static int getWeekOfMonthFormDate(String dateString) {
        return getWeekOfMonth(toDate(dateString + " 00:00:01"));
    }

    /**
     * 得到指定时间是当月的第几个周
     *
     * @param dateTimeString 指定的日期时间 例如 2007-12-12 00:01:02
     * @return int 返回第几周
     */
    public static int getWeekOfMonthFormDateTime(String dateTimeString) {
        return getWeekOfMonth(toDate(dateTimeString));
    }

    /**
     * 得当天是本月的第几个周
     *
     * @return int 返回第几周
     */
    public static int getWeekOfMonth() {
        return getWeekOfMonth(null);
    }

    /**
     * @param date       当前日期
     * @param n          日期跨度，正数为之后N天，复数为之前N天
     * @param fromFormat 当前日期格式
     * @param toFormat   返回日期格式
     * @return String
     * @throws
     * @Title: getLastDay
     * @Description: 获取前/后N天的日期
     * @author: 黑面阿呆
     */
    public static String getLastDay(String date, long n, String fromFormat, String toFormat) {
        long dateTime = getDateMillis(date, fromFormat) + (86400000 * n);
        return formatDate(new Date(dateTime), toFormat);
    }

    /**
     * @param date 当前日期,默认格式为yyyyMMdd
     * @param n    日期跨度，正数为之后N天，复数为之前N天
     * @return String 返回日期，默认格式yyyyMMdd
     * @throws
     * @Title: getLastDay
     * @Description: 获取前/后N天的日期
     * @author: 黑面阿呆
     */
    public static String getLastDay(String date, long n) {
        return getLastDay(date, n, "yyyyMMdd", "yyyyMMdd");
    }

    /**
     * @param date
     * @param n
     * @param fromFormat
     * @param toFormat
     * @return String
     * @throws
     * @Title: getLastMonth
     * @Description: 获取前/后N月的日期
     * @author: 黑面阿呆
     */
    public static String getLastMonth(String date, long n, String fromFormat, String toFormat) {
        Date date1 = new Date(getDateMillis(date, fromFormat));
        int year = Integer.parseInt(getYear(date1));
        int month = Integer.parseInt(getMonth(date1));
        month += n;
        while (month < 1 || month > 12) {
            if (month < 1) {
                month += 12;
                year--;
            } else {
                month -= 12;
                year++;
            }
        }
        String month1 = "" + month;
        if (month < 10) {
            month1 = "0" + month1;
        }
        String date2 = year + month1 + "01";
        return formatDate(new Date(getDateMillis(date2, "yyyyMMdd")), toFormat);
    }

    /**
     * @param date
     * @param n
     * @return String
     * @throws
     * @Title: getLastMonth
     * @Description: 获取前/后N月的日期
     * @author: 黑面阿呆
     */
    public static String getLastMonth(String date, long n) {
        return getLastMonth(date, n, "yyyyMM", "yyyyMM");
    }

    public static String formatDateStrToStr(String date, String fromStr, String toStr) {
        return UtilDateTime.formatDate(new Date(UtilDateTime.getDateMillis(date, fromStr)), toStr);
    }
}
