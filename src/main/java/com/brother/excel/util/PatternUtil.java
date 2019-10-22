package com.brother.excel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 * 正则工具
 */
public class PatternUtil {

    /**
     * 2个字符
     */
    private static Integer charNum = 2;

    /**
     * 是否有数字 (正常，休息。  不统计)
     * @param string
     * @return
     */
    public static boolean isHasTwoCharPattern(String string) {
        // 取出其中的非数字，与源字符串对比。
        if (charNum == string.length()) {
            return true;
        }
        return false;
    }

    /**
     * 获取 月-日   （用于 分字符串后，取 前缀中文）
     * @param string
     * @return
     */
    public static String getStartDate(String string) {
        // 匹配 几月几号 （09-02）
        String regex = "\\d+\\-\\d+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(string);
        String ss = "";
        if (matcher.find()) {
            ss = matcher.group();
        }
        return ss;
    }

    /**
     * 只匹配数字 兼容 小数
     * @param string
     * @return
     */
    public static String onlyNumberPattern(String string) {
        // 时长 5小时
        String regex = "\\d+\\.\\d+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(string);
        String whenNumber = "";
        if (matcher.find()) {
            whenNumber = matcher.group();
        }else {
            String regexScale = "\\d+";
            p = Pattern.compile(regexScale);
            matcher = p.matcher(string);
            if (matcher.find()) {
                whenNumber = matcher.group();
                return whenNumber;
            }
        }
        return whenNumber;
    }

    /**
     * 只匹配字符串
     * @param string
     * @return
     */
    public static String onlyStringPattern(String string) {
        // 时长 5小时
        String regex = "\\s+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(string);
        String whenInt = "";
        if (matcher.find()) {
            whenInt = matcher.group();
        }
        return whenInt;
    }



    public static void main(String[] args) {
//        System.out.println(PatternUtil.isHasTwoCharPattern("aj"));
//        System.out.println(PatternUtil.onlyNumberPattern("6小时"));

//        String str = "事假09-29 09:00到09-30 18:00 15小时";
//        String startDate = PatternUtil.getStartDate(str);
//        System.out.println("开始日期 : " + startDate);
//
//        String endDate = str.split(" ")[1].split("到")[1];
//        System.out.println("结束日期 : " + endDate);

        System.out.println(PatternUtil.onlyNumberPattern("1小时"));

    }

}
