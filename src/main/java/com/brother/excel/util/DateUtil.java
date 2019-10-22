package com.brother.excel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 */
public class DateUtil {

    /**
     * 获取某月某天的 date
     * @param month
     * @param day
     * @return
     */
    public static Date getDate(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 修改 date 月份
     * @param date
     * @param month
     */
    public static Date setMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getTime();
    }

    /**
     * 在 date 上 加一天
     * @param date
     * @param n
     */
    public static Date dateAddOnDay(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, day + n);
        return calendar.getTime();
    }

    /**
     * date 转 字符串（2019-10-10）
     * @param date
     * @return
     */
    public static String dateToStringOnlyDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        String result = format.format(date);
        return result;
    }
    /**
     * date 转 字符串（10-10）
     * @param date
     * @return
     */
    public static String dateToStringOnlyMonthDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String result = format.format(date);
        return result;
    }


    /**
     * String 转 date 格式
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        if ("".equals(str)) return null;
        try {
            if (str.length() == 5) {
                // 参数年月日 没有分隔符
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                Date date = sdf.parse(str);
                return date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
//        System.out.println( DateUtil.getDate(3,30));

//        Date date = new Date();
//        System.out.println(date);
//        Date date1 = DateUtil.dateAddOnDay(date);
//        System.out.println(date1);

//        Date date = DateUtil.stringToDate("20:45");
//        Date date2 = DateUtil.stringToDate("21:45");
//        Long getime = date2.getTime() - date.getTime();
//        Date datenew = new Date(getime);
//        System.out.println(datenew);

//        System.out.println(DateUtil.getDate(-2,1));
//        System.out.println(Math.random() * 1000);
//        for (int i = 0; i < 20; i++) {
//            System.out.println(String.valueOf(Long.toHexString(Long.valueOf((int)(Math.random() * 1000) + "" + System.currentTimeMillis()))).substring(0,8));
////            System.out.println(Long.toHexString((int)(Math.random() * 1000) + System.currentTimeMillis()));
//        }

        for (int i = 0; i < 20; i++) {
            double random = 100 + (Math.random() * 900);
            System.out.println(random);
            int n = (int)random;
            System.out.println(n);
        }
    }




}
