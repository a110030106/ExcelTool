package com.brother.excel.manager;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

/**
 * 创建人：王福顺  创建时间：2019/10/15
 * 单元格样式 编辑 类
 */
@Component
public class ExcelStyleManager {


    /**
     * 全局样式
     *
     * @param workbook
     * @return
     */
    public CellStyle getAllCellStyle(Workbook workbook) {
        // 设置公式
//        workbook.setForceFormulaRecalculation(true);
        // 设置自动换行
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(true);
        // 设置边框
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        // 居中
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 设置居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 设置居中

        return cellStyle;
    }

    /**
     * 个别单元格样式
     *
     * @return
     */
    public void buildSelfStyle(CellStyle cellStyle, int rowNum, Workbook workbook, Sheet sheet) {

        // 奇数列（前6项的统计 时间与时长 列）
        if (rowNum > 0 && rowNum <= 12 && rowNum % 2 != 0) {
            // 比例 0.00325
            sheet.setColumnWidth(rowNum, 5538);
        }

        // 偶数列（前6项的统计 时间与时长 列）
        if (rowNum > 0 && rowNum <= 12 && rowNum % 2 == 0) {
            // 比例 0.00325
            sheet.setColumnWidth(rowNum, 3077);
        }
    }


}
