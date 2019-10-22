package com.brother.excel.models;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 */
public class TestList {
    // 姓名
    private static String date0 = "贺美定";
    // 正常
    private static String date1 = "正常";
    // 休息
    private static String date2 = "休息";
    // 上班迟到
    private static String date3 = "上班迟到";
    // 正常加班
    private static String date4 = "正常,加班09-02 19:00到09-02 21:30 2.5小时";
    // 周末加班
    private static String date5 = "休息并打卡,加班09-08 13:02到09-08 18:02 5小时";
    // 事假
    private static String date6 = "事假09-12 14:00到09-12 18:00 4小时";
    // 病假
    private static String date7 = "病假09-12 14:00到09-12 18:00 4小时";

    private static String[] dateArr = new String[8];

    public static String[] initList() {
        // 模拟 一个人的7天考勤
        dateArr[0] = date0;
        dateArr[1] = date1;
        dateArr[2] = date2;
        dateArr[3] = date3;
        dateArr[4] = date4;
        dateArr[5] = date5;
        dateArr[6] = date6;
        dateArr[7] = date7;
        return dateArr;
    }
}
