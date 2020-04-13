package org.breeze.core.utils.string;

import java.util.Random;

/**
 * @Description: 字符串相关方法
 * @Auther: 黑面阿呆
 * @Date: 2019/2/9 20:35
 * @Version: 1.0.0
 */
public class UtilString {

    private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 判断字符串是否为null
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    /**
     * 判断字符串是否为空串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return "".equals(str);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return isEmpty(str) || isNull(str);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str) && !isNull(str);
    }

    /**
     * 判断字符串是否以指定字符开头
     *
     * @param str
     * @param startStr
     * @return
     */
    public static boolean startWith(String str, String startStr) {
        if (isNullOrEmpty(str) || isNullOrEmpty(startStr)) {
            return false;
        }
        return str.startsWith(startStr);
    }

    /**
     * 判断字符串是否以指定字符开头
     *
     * @param str
     * @param startStr
     * @return
     */
    public static boolean startWith(String str, char startStr) {
        return startWith(str, String.valueOf(startStr));
    }

    /**
     * 判断字符串是否以指定字符开头
     *
     * @param str
     * @param startStr
     * @return
     */
    public static boolean startWith(String str, int startStr) {
        return startWith(str, String.valueOf(startStr));
    }

    /**
     * 判断字符串是否以指定字符结尾
     *
     * @param str
     * @param endStr
     * @return
     */
    public static boolean endWith(String str, String endStr) {
        if (isNullOrEmpty(str) || isNullOrEmpty(endStr)) {
            return false;
        }
        return str.endsWith(endStr);
    }

    /**
     * 判断字符串是否以指定字符结尾
     *
     * @param str
     * @param endStr
     * @return
     */
    public static boolean endWith(String str, char endStr) {
        return endWith(str, String.valueOf(endStr));
    }

    /**
     * 判断字符串是否以指定字符结尾
     *
     * @param str
     * @param endStr
     * @return
     */
    public static boolean endWith(String str, int endStr) {
        return endWith(str, String.valueOf(endStr));
    }

    /**
     * 获取字符串长度,若为空则返回-1
     *
     * @param str
     * @return
     */
    public static long getLength(String str) {
        if (isNull(str)) {
            return -1;
        }
        return str.length();
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @return
     */
    public static String subString(String str, int start) {
        if (isNullOrEmpty(str) || start < 0 || str.length() < start) {
            return null;
        }
        return str.substring(start);
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String subString(String str, int start, int end) {
        if (isNullOrEmpty(str) || start < 0 || str.length() < end) {
            return null;
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        return str.substring(start, end);
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @return
     */
    public static String subString(String str, String start) {
        if (isNullOrEmpty(str) || isNullOrEmpty(start)) {
            return null;
        }
        int number = str.indexOf(start);
        if (number < 0) {
            return str;
        }
        return str.substring(number + start.length());
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @param length
     * @return
     */
    public static String subString(String str, String start, int length) {
        return subString(str, start, length, true);
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @param length
     * @param notNull
     * @return
     */
    public static String subString(String str, String start, int length, boolean notNull) {
        if (isNullOrEmpty(str) || isNullOrEmpty(start) || length < 0 || length > getLength(str)) {
            return null;
        }
        int number = str.indexOf(start);
        if (number < 0) {
            if (notNull) {
                return str;
            } else {
                return null;
            }
        }
        int startNum = number + start.length();
        int endNum = startNum + length;
        if (endNum > str.length()) {
            endNum = str.length();
        }
        return str.substring(startNum, endNum);
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static String subString(String str, String start, String end) {
        return subString(str, start, end, true);
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param start
     * @param end
     * @param notNull
     * @return
     */
    public static String subString(String str, String start, String end, boolean notNull) {
        if (isNullOrEmpty(str) || isNullOrEmpty(start) || isNullOrEmpty(end)) {
            return null;
        }
        int snumber = str.indexOf(start);
        if (snumber < 0) {
            return subString(str, str.length(), end, notNull);
        }
        int startNum = snumber + start.length();
        int endNum = str.substring(startNum).indexOf(end);
        if (endNum < 0) {
            return subString(str, start, str.length(), notNull);
        }
        endNum += startNum;
        return str.substring(startNum, endNum);
    }

    /**
     * 字符串截取
     *
     * @param str    待截取字符串
     * @param length 截取长度
     * @param end    字符串截止符
     * @return
     */
    public static String subString(String str, int length, String end) {
        return subString(str, length, end, true);
    }

    /**
     * 字符串截取
     *
     * @param str
     * @param length
     * @param end
     * @param notNull
     * @return
     */
    public static String subString(String str, int length, String end, boolean notNull) {
        if (isNullOrEmpty(str) || isNullOrEmpty(end) || length < 0 || length > getLength(str)) {
            return null;
        }
        int number = str.indexOf(end);
        if (number < 0) {
            if (notNull) {
                return str;
            } else {
                return null;
            }
        }
        int startNum = number - length;
        if (startNum < 0) {
            startNum = 0;
        }
        return str.substring(startNum, number);
    }

    /**
     * 判断字符串是否包含
     *
     * @param str
     * @param contain
     * @return
     */
    public static boolean contains(String str, String contain) {
        if (isNullOrEmpty(str) || isNullOrEmpty(contain)) {
            return false;
        }
        return str.indexOf(contain) > -1;
    }

    /**
     * 生成指定位数的随机字符串
     *
     * @param number
     * @return
     */
    public static String getRandomStr(int number) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < number; i++) {
            sb.append(RANDOM_STR.charAt(random.nextInt(RANDOM_STR.length())));
        }
        return sb.toString();
    }
}
