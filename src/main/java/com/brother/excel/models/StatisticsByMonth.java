package com.brother.excel.models;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 * 此类 表示 一个人的 各种 统计数据
 */
@Data
@ToString
public class StatisticsByMonth {

    /**
     * 姓名
     */
    private String name;

    /**
     * 工作日加班
     */
    private List<StatisticsByDay> overTime;

    /**
     * 周末加班
     */
    private List<StatisticsByDay> weekEndOverTime;

    /**
     * 迟到次数 （直接取值）
     */
    private String beLateCount;

    /**
     * 迟到时间 （直接取值）
     */
    private String beLateWhen;

    /**
     * 早退次数 （直接取值）
     */
    private String earlyLeaveCount;

    /**
     * 早退时间 （直接取值）
     */
    private String earlyLeaveWhen;

    /**
     * 上班缺卡次数 （直接取值）
     */
    private String onLackCardCount;

    /**
     * 下班缺卡次数 （直接取值）
     */
    private String offLackCardCount;

    /**
     * 调休,休息调休
     */
    private List<StatisticsByDay> takeWork;

    /**
     * 年假,休息年假
     */
    private List<StatisticsByDay> annualVacation;

    /**
     * 病假,休息病假
     */
    private List<StatisticsByDay> sickLeave;

    /**
     * 事假,休息事假
     */
    private List<StatisticsByDay> thingLeave;


}


