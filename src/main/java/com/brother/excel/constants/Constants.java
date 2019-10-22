package com.brother.excel.constants;

import lombok.Getter;

import java.io.File;

/**
 * 创建人：王福顺  创建时间：2019/10/12
 */
@Getter
public class Constants {

    /**
     * 前缀名 识别cmbi内部 和 外部员工
     */
    public static final String FILENAME_PREFIX_CMBI = "cmbi";
    /**
     * 前缀名 识别cmbi内部 和 外部员工
     */
    public static final String FILENAME_PREFIX_OTHER = "other";

    /**
     * 2003版本 后缀
     */
    public static final String XLS_SUFFIX = "xls";
    /**
     * 2007版本 后缀
     */
    public static final String XLSX_SUFFIX = "xlsx";

    /**
     * 生成的 excel 存放地址   /usr/jetty-80-ExcelTool/excelFile/
     */
//    public static final String OUT_PUT_PATH = File.separator + "usr" + File.separator + "serviceFile" + File.separator + "jetty-80-ExcelTool" + File.separator;
    public static final String OUT_PUT_PATH = "D:\\abc\\";

    /**
     * 生成的 zip 文件名
     */
    public static final String ZIP_FILENAME = "cmbiTeam.zip";

    /**
     * 每天最大工作小时
     */
    public static final float WORK_HOUR = 7.5f;

    /**
     * 表格中 姓名所在列    0 - A
     */
    public static final int NAME_CELL = 0;
    /**
     * 日期开始列
     */
    public static final int DAY_START_CELL = 31;
    /**
     * 姓名开始行
     */
    public static final int NAME_START_ROW = 5;


    /**
     * 生成的新excel 名头
     */
    public static final String EXCEL_TITLE = "考勤表";
    /**
     * 生成的新excel 字段头
     */
    public static final String[] FIELD_NAME = new String[]{"姓名", "请年假日期", "时长H", "请事假日期", "时长H", "请病假日期", "时长H", "平时加班日期", "时长H", "周末加班日期", "时长H", "调休日期", "时长H",
                                                           "迟到次数", "迟到时长(分)", "早退次数","早退时长(分)", "上班缺卡次数", "下班缺卡次数"};

    /**
     * 正常加班前缀
     */
    public static final String OVER_TIME = "正常,加班";
    /**
     * 正常迟到加班前缀 (3.0需求  也要算进加班)
     */
    public static final String BE_LATE_OVER_TIME = "上班迟到,加班";
    /**
     * 周末加班前缀
     */
    public static final String WEEKEND_OVER_TIME = "休息并打卡,加班";
    /**
     * 调休前缀   （3.0 需求）
     */
    public static final String TAKE_WORK = "调休";
    public static final String REST_TAKE_WORK = "休息,调休";
    /**
     * 年假前缀
     */
    public static final String ANNUAL_VACATION = "年假";
    public static final String REST_ANNUAL_VACATION = "休息,年假";
    /**
     * 事假前缀
     */
    public static final String THING_LEAVE = "事假";
    public static final String REST_THING_LEAVE = "休息,事假";
    /**
     * 病假前缀
     */
    public static final String SICK_LEAVE = "病假";
    public static final String REST_SICK_LEAVE = "休息,病假";
    /**
     * 婚假前缀
     */
    public static final String MERRY_LEAVE = "婚嫁";
    public static final String REST_MERRY_LEAVE = "休息,婚嫁";



}
