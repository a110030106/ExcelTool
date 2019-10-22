package com.brother.excel.manager;

import com.brother.excel.constants.Constants;
import com.brother.excel.models.StatisticsByDay;
import com.brother.excel.models.StatisticsByMonth;
import com.brother.excel.util.DateUtil;
import com.brother.excel.util.PatternUtil;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 * 读取 一行 的 考勤信息
 */
@Component
public class ReadExcelManager {


    /**
     * 获取该月的有用信息
     *
     * @param testList 传入一行的 信息
     */
    public List<StatisticsByMonth> readExcel(List<List<String[]>> testList) {
        // 测试数据
//        testList = TestList.initList();
        List<StatisticsByMonth> resDto = new ArrayList<>();
        StatisticsByMonth oneRow;
        // 只获取 第一个 sheet
        List<String[]> stringsList = testList.get(0);
        // sheet
        for (int i = 0; i < stringsList.size(); i++) {
            // 姓名行以上的信息 暂时不用
            if (i <= Constants.NAME_START_ROW - 2) continue;
            // 行
            String[] strings = stringsList.get(i);
            // 相关信息
            oneRow = new StatisticsByMonth();
            // 姓名
            String name = strings[Constants.NAME_CELL];
            oneRow.setName(name);
            // 统计正常加班, 迟到加班
            List<StatisticsByDay> overTimeList = readExcelRow(strings, Constants.OVER_TIME, Constants.BE_LATE_OVER_TIME);
            oneRow.setOverTime(overTimeList);
            // 统计周末加班
            List<StatisticsByDay> weekendOverTimeList = readExcelRow(strings, Constants.WEEKEND_OVER_TIME);
            oneRow.setWeekEndOverTime(weekendOverTimeList);
            // 调休
            List<StatisticsByDay> takeWorkTimeList = readExcelRow(strings, Constants.TAKE_WORK);
            // 连续请假的 处理
            checkContinuousLeave(takeWorkTimeList);
            oneRow.setTakeWork(takeWorkTimeList);
            // 统计年假，休息年假
            List<StatisticsByDay> annualVacationList = readExcelRow(strings, Constants.ANNUAL_VACATION);
            checkContinuousLeave(annualVacationList);
            oneRow.setAnnualVacation(annualVacationList);
            // 统计病假，休息病假
            List<StatisticsByDay> sickLeaveList = readExcelRow(strings, Constants.SICK_LEAVE);
            checkContinuousLeave(sickLeaveList);
            oneRow.setSickLeave(sickLeaveList);
            // 统计事假，休息事假
            List<StatisticsByDay> thingLeaveList = readExcelRow(strings, Constants.THING_LEAVE);
            checkContinuousLeave(thingLeaveList);
            oneRow.setThingLeave(thingLeaveList);
            // 统计迟到次数、时长
            oneRow.setBeLateCount(PatternUtil.onlyNumberPattern(strings[7]));
            oneRow.setBeLateWhen(strings[8]);
            // 统计早退次数、时长
            oneRow.setEarlyLeaveCount(PatternUtil.onlyNumberPattern(strings[9]));
            oneRow.setEarlyLeaveWhen(strings[10]);
            // 上班缺卡次数
            oneRow.setOnLackCardCount(PatternUtil.onlyNumberPattern(strings[11]));
            // 下班缺卡次数
            oneRow.setOffLackCardCount(PatternUtil.onlyNumberPattern(strings[12]));

            System.out.println(oneRow);
//                oneRow = readExcelManager.readExcel(stringsList.get(i));
            // 姓名与相关信息
            resDto.add(oneRow);
        }
        return resDto;
    }

    /**
     * 构建返回值，根据传入的常量，获取 这个月 该常量的 信息
     *
     * @param rowsArr    行信息
     * @param thingInfos 指定 统计内容
     * @return
     */
    private List<StatisticsByDay> readExcelRow(String[] rowsArr, String... thingInfos) {
        List<StatisticsByDay> conditionList = new ArrayList<>(31);
        // 初始化时间
        int month = 0;
        Date date = DateUtil.getDate(month, 1);   // month无意义   todo   ing  第一个格 为 第一天 才生效
        StatisticsByDay conditionInfo;
        for (int i = 0; i < rowsArr.length; i++) {
            // 从日期开始的列 （1号）
            if (i <= Constants.DAY_START_CELL - 2) continue;
            String str = rowsArr[i];
            // 如果位置是空 则 加一天日期 跳过
            if ("".equals(str) || null == str) {
                // date 加一天
                date = DateUtil.dateAddOnDay(date, 1);
                continue;
            }
            conditionInfo = new StatisticsByDay();
            // 获取具体情况 （正常/病假/加班/周末加班 等等等等）
            String startDate = PatternUtil.getStartDate(str);
            String[] strArr = str.split(startDate);
            String desc = strArr[0];
            // 遍历整行所有信息
            for (String thingInfo : thingInfos) {
                // 只匹配相关项 进行统计
                if (thingInfo.equals(desc)) {
                    // 解析单元格内的 字符串
                    String[] whens = str.split(" ");
                    String[] times = whens[1].split("到");
                    // 情况
                    conditionInfo.setConditionName(thingInfo);
                    // 开始日期
                    if (month == 0) {
                        month = Integer.valueOf(PatternUtil.onlyNumberPattern(whens[0]).split("-")[0]);
                        date = DateUtil.setMonth(date, month);
                    }
                    conditionInfo.setStartDate(DateUtil.dateToStringOnlyMonthDay(date));
                    // 开始时间
                    conditionInfo.setStartTime(times[0]);
                    // 结束日期
                    conditionInfo.setEndDate(times[1]);
                    // 结束时间
                    conditionInfo.setEndTime(whens[2]);
                    // 用时
                    conditionInfo.setHasTime(PatternUtil.onlyNumberPattern(whens[3]));

                    conditionList.add(conditionInfo);
                }
            }
            // date 加一天
            date = DateUtil.dateAddOnDay(date, 1);
        }
        return conditionList;
    }

    /**
     * 找出 连续请假 并 处理
     *
     * @param statisticsByDayList
     */
    private void checkContinuousLeave(List<StatisticsByDay> statisticsByDayList) {

        String startDate;
        String endDate;
        // 标记对象  多个连续 放进 list
        List<List<StatisticsByDay>> statisticsByDaysList = new ArrayList<>();
        List<StatisticsByDay> tempStatisticsByDayList = new ArrayList<>();
        StatisticsByDay statisticsByDay;
        // 标记  连续加班
        boolean isContinuou = false;
        for (int i = 0; i < statisticsByDayList.size(); i++) {
            startDate = statisticsByDayList.get(i).getStartDate();
            endDate = statisticsByDayList.get(i).getEndDate();
            // 2个日期不相同 （说明是 连续请假）
            if (!startDate.equals(endDate)) {
                statisticsByDay = statisticsByDayList.get(i);
                tempStatisticsByDayList.add(statisticsByDay);
                isContinuou = true;
            } else {
                // 当之前为连续，那么此时为最后一天，也要进行标记
                if (isContinuou) {
                    statisticsByDay = statisticsByDayList.get(i);
                    tempStatisticsByDayList.add(statisticsByDay);
                    // 最后一天标记后，取消连续标识
                    isContinuou = false;
                }
                // 连续加班结束，再创建一个连续加班的 list
                if (tempStatisticsByDayList.size() != 0) {
                    statisticsByDaysList.add(tempStatisticsByDayList);
                    tempStatisticsByDayList = new ArrayList<>();
                }
            }
        }
        // 当只有一段、或 结尾 是连续的 一段    的 请假 情况
        if (null != tempStatisticsByDayList && 0 != tempStatisticsByDayList.size()) {
            statisticsByDaysList.add(tempStatisticsByDayList);
        }

        // 处理标记
        String allHasTime;
        Boolean isRemainder;
        if (null != statisticsByDaysList && 0 != statisticsByDaysList.size()) {
            for (int leaveCount = 0; leaveCount < statisticsByDaysList.size(); leaveCount++) {
                // 此次连续请假的总时间
                allHasTime = statisticsByDaysList.get(leaveCount).get(0).getHasTime();
                // 是否能整除（是否是 全天请假）
                isRemainder = isRemainder(allHasTime);
                int statisticsByDaySize = statisticsByDaysList.get(leaveCount).size();
                for (int leaveDay = 0; leaveDay < statisticsByDaySize; leaveDay++) {
                    // 获取对象
                    statisticsByDay = statisticsByDaysList.get(leaveCount).get(leaveDay);
                    // 处理用时
                    statisticsByDay.setHasTime(String.valueOf(Constants.WORK_HOUR));
                    // 不能整除，最后一天 赋值 余数
                    if (!isRemainder) {
                        // 求余数， 并取消 整数时的 小数点
                        String remainder = clearScale(getRemainder(allHasTime));
                        statisticsByDaysList.get(leaveCount).get(statisticsByDaySize - 1).setHasTime(remainder);
                    }
                }
            }
        }
    }

    /**
     * 字符串日期 加 n天
     *
     * @param dateStr
     * @return
     */
    private String stringDateAddOne(String dateStr, int n) {
        // string-->date ,  date + n天  ,  date > string
        return DateUtil.dateToStringOnlyMonthDay(DateUtil.dateAddOnDay(DateUtil.stringToDate(dateStr), n));
    }

    /**
     * 被一天的工作时间 除后  是否有余数
     *
     * @param string
     * @return
     */
    private Boolean isRemainder(String string) {
        Float f = Float.valueOf(string);
        Float remainder = f % Constants.WORK_HOUR;
        if (remainder == 0) {
            return true;
        }
        return false;
    }

    /**
     * 求余数 并保留1为小数
     *
     * @param string
     * @return
     */
    private float getRemainder(String string) {
        Float f = Float.valueOf(string);
        float remainder = (float) (Math.round((f % Constants.WORK_HOUR) * 10)) / 10;
        return remainder;
    }

    /**
     * 去除 小数点后的0
     *
     * @param f
     * @return
     */
    private String clearScale(float f) {
        int fInt = (int) f;
        if (fInt == f) {
            return String.valueOf(fInt);
        }
        return String.valueOf(f);
    }

    public static void main(String[] args) {
        System.out.println(Float.valueOf("7.568"));
    }

}
