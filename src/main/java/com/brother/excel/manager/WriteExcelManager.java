package com.brother.excel.manager;


import com.brother.excel.constants.Constants;
import com.brother.excel.models.StatisticsByDay;
import com.brother.excel.models.StatisticsByMonth;
import com.brother.excel.util.PatternUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 *  根据入参 写新的List
 */
@Component
public class WriteExcelManager {

    /**
     * 根据 信息 写 表
     * @param oneRowInfoList
     * @return
     */
    public List<List<String[]>> writeExcel(List<StatisticsByMonth> oneRowInfoList) {
        List<List<String[]>> newExcel = new ArrayList<>();

        // 由于 oneRowInfoList 只写了一页sheet 功能，  所以这里也只输出 1页 sheet
        List<String[]> sheet =  new ArrayList<>();
        // 赋值
        buildSheet(sheet, oneRowInfoList);
        newExcel.add(sheet);
        return newExcel;
    }

    /**
     * 初始化sheet  名头 列名  与  合并单元格
     * @param sheet
     * @param statisticsByNameList    每个人的详细 信息
     */
    private List<String[]> buildSheet(List<String[]> sheet, List<StatisticsByMonth> statisticsByNameList) {
        // 构建 表头，列名
        sheet.add(new String[]{Constants.EXCEL_TITLE});
        sheet.add(Constants.FIELD_NAME);
        // 定义 当前行  0 1 为表头与列名
        int nowRow = 2;
        // 构建详细信息
        for (StatisticsByMonth statisticsByName : statisticsByNameList) {
            // 构建当前行信息
            String[] nowRowInfo = buildNowRowInfo(statisticsByName);
            sheet.add(nowRow, nowRowInfo);
        }
        return sheet;

    }

    /**
     * 构建每行的 详细信息
     * @param statisticsByMonth
     * @return
     */
    private String[] buildNowRowInfo(StatisticsByMonth statisticsByMonth) {
        String[] nowRowInfo = new String[Constants.FIELD_NAME.length];
        // 姓名
        nowRowInfo[0] = statisticsByMonth.getName();
        // 年假,休息年假
        List<StatisticsByDay> annualVacation = statisticsByMonth.getAnnualVacation();
        if (isNotNullByList(annualVacation)) {
            String[] annualVacationInfo = buildInfo(annualVacation);
            nowRowInfo[1] = annualVacationInfo[0];
            nowRowInfo[2] = annualVacationInfo[1];
        }
        // 事假,休息事假
        List<StatisticsByDay> thingLeave = statisticsByMonth.getThingLeave();
        if (isNotNullByList(thingLeave)){
            String[] thingLeaveInfo = buildInfo(thingLeave);
            nowRowInfo[3] = thingLeaveInfo[0];
            nowRowInfo[4] = thingLeaveInfo[1];
        }
        // 病假,休息病假
        List<StatisticsByDay> sickLeave = statisticsByMonth.getSickLeave();
        if (isNotNullByList(sickLeave)) {
            String[] sickLeaveInfo = buildInfo(sickLeave);
            nowRowInfo[5] = sickLeaveInfo[0];
            nowRowInfo[6] = sickLeaveInfo[1];
        }
        // 平时加班,迟到加班
        List<StatisticsByDay> overTime = statisticsByMonth.getOverTime();
        if (isNotNullByList(overTime)) {
            String[] overTimeInfo = buildInfo(overTime);
            nowRowInfo[7] = overTimeInfo[0];
            nowRowInfo[8] = overTimeInfo[1];
        }
        // 周末加班
        List<StatisticsByDay> weekEndOverTime = statisticsByMonth.getWeekEndOverTime();
        if (isNotNullByList(weekEndOverTime)) {
            String[] weekEndOverTimeInfo = buildInfo(statisticsByMonth.getWeekEndOverTime());
            nowRowInfo[9] = weekEndOverTimeInfo[0];
            nowRowInfo[10] = weekEndOverTimeInfo[1];
        }
        // 调休  （只有cmbi内部员工有，所以放在后面）有就生成，没有就不生成
        List<StatisticsByDay> takeWork = statisticsByMonth.getTakeWork();
        if (isNotNullByList(takeWork)) {
            String[] takeWorkInfo = buildInfo(takeWork);
            nowRowInfo[11] = takeWorkInfo[0];
            nowRowInfo[12] = takeWorkInfo[1];
        }
        // 迟到次数、时间
        String beLateCount = statisticsByMonth.getBeLateCount();
        if (isNotNullByString(beLateCount)) nowRowInfo[13] = beLateCount;
        String beLateWhen = statisticsByMonth.getBeLateWhen();
        if (isNotNullByString(beLateWhen)) nowRowInfo[14] = beLateWhen;
        // 早退次数、时间
        String earlyLeaveCount = statisticsByMonth.getEarlyLeaveCount();
        if (isNotNullByString(earlyLeaveCount)) nowRowInfo[15] = earlyLeaveCount;
        String earlyLeaveWhen = statisticsByMonth.getEarlyLeaveWhen();
        if (isNotNullByString(earlyLeaveWhen)) nowRowInfo[16] = earlyLeaveWhen;
        // 上、下班缺卡次数
        String onLackCardCount = statisticsByMonth.getOnLackCardCount();
        if (isNotNullByString(onLackCardCount)) nowRowInfo[17] = onLackCardCount;
        String offLackCardCount = statisticsByMonth.getOffLackCardCount();
        if (isNotNullByString(offLackCardCount)) nowRowInfo[18] = offLackCardCount;

        return nowRowInfo;
    }

    /**
     * 查看string 为null 或为 ""
     * @param string
     * @return
     */
    private boolean isNotNullByString(String string) {
        if (null != string  &&  !"".equals(string)) {
            return true;
        }
        return false;
    }
    private boolean isNotNullByList(List list) {
        if (null != list  &&  0 != list.size()) {
            return true;
        }
        return false;
    }

    /**
     * 解析数据  构建 详细 与 时长 格
     * @param infoList
     * @return
     */
    private String[] buildInfo(List<StatisticsByDay>... infoList) {
        String[] infoAndWhenArr = new String[2];
        // 详细栏
        StringBuffer info = new StringBuffer();
        // 用时栏
        StringBuffer when = new StringBuffer();
        // 总时长
        float whenAll = 0;
        // 替换换行符的 占位符
        String enter = "\r\n";
        // 构建  9.2（19:00-21:00）
        for (List<StatisticsByDay> statisticsByDays : infoList) {
            for (StatisticsByDay statisticsByDay : statisticsByDays) {
                // 详细栏
                info.append(statisticsByDay.getStartDate() + " (");
                info.append(statisticsByDay.getStartTime() + "-");
                info.append(statisticsByDay.getEndTime() + ")");
                // 添加换行符， 在excel 中 用 Ctrl + f 替换成  换行  （excel输入换行  Ctrl + j）
                info.append(enter);
                // 时长栏
                when.append(statisticsByDay.getHasTime() + "+");
                // 计算总时长
                whenAll += Float.valueOf(statisticsByDay.getHasTime());
            }
        }
        // 去掉末尾的 +
        String whenStr = when.substring(0, when.length() - 1);
        // 去掉末尾的 换行符  '\r\n'
        String infoStr = info.substring(0, info.length() - enter.length());
        // 添加 =  并 去掉 整数时 后面的 .0
        whenStr += checkWhenAll(whenAll);
        // 如果只有1个元素 就不写  4=4  而写 4
        int size = 0;
        for (List<StatisticsByDay> statisticsByDays : infoList) {
            size += statisticsByDays.size();
        }
        if (1 == size) {
            whenStr = PatternUtil.onlyNumberPattern(whenStr);
        }
        infoAndWhenArr[0] = infoStr;
        infoAndWhenArr[1] = whenStr;
        return infoAndWhenArr;
    }

    /**
     * 去掉 整数时的小数位 (.0)
     * @param whenAll
     * @return
     */
    private String checkWhenAll(float whenAll) {
        String whenStr;
        int whenInt = (int)whenAll;
        if (whenInt == whenAll) {
            whenStr =  "=" + String.valueOf(whenInt);
        }else {
            whenStr =   ("=" + whenAll);
        }
        return whenStr;
    }

    /**
     * 查看最长的 是多少行 （需要合并单元格 放名字及其他）
     * @return  ( 已弃用)
     */
//    private int maxRowMerge(StatisticsByMonth oneRowInfo) {
//        List<StatisticsByDay> overTimeList = oneRowInfo.getOverTime();
//        List<StatisticsByDay> weekEndOverTimeList =  oneRowInfo.getWeekEndOverTime();
//        List<StatisticsByDay> annualVacationList =  oneRowInfo.getAnnualVacation();
//        List<StatisticsByDay> sickLeaveList =  oneRowInfo.getSickLeave();
//        List<StatisticsByDay> thingLeaveList =  oneRowInfo.getThingLeave();
//        // 获取最长的list size
//        int maxSize = getMaxNumber(overTimeList.size(), weekEndOverTimeList.size(), annualVacationList.size(), sickLeaveList.size(),
//                    thingLeaveList.size());
//        return maxSize;
//    }
    /**
     * 获取最大值    （已弃用）
     * @param ints
     * @return
     */
//    private int getMaxNumber(int...ints) {
//        int maxNumber = 0;
//        for (int i : ints) {
//            maxNumber = Math.max(maxNumber, i);
//        }
//        return maxNumber;
//    }


}
