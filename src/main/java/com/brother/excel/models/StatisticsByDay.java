package com.brother.excel.models;

import lombok.Data;
import lombok.ToString;

/**
 * 创建人：王福顺  创建时间：2019/10/15
 */
@Data
@ToString
public class StatisticsByDay {

    /**
     * 情况
     */
    private String conditionName;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 用时
     */
    private String hasTime;
}
