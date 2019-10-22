package com.brother.excel.manager;

import com.brother.excel.constants.Constants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：王福顺  创建时间：2019/10/12
 * 用于读取  excel表格
 */
@Component
public class InputExcelManager {

    /**
     * 读取Excel 文件  并 生成 List<List<String[]>>
     * 第一层为 sheet
     * 第二、三层为  行 与 列      类似二维数组
     * @param file
     * @return
     */
    public List<List<String[]>> loadExcel(MultipartFile file) {
        List<List<String[]>> sheetList = new ArrayList<>();
        // 检查文件

        // 获取excel表格
        Workbook workBook = getWorkBook(file);
        // 获取 excel表
        if (null != workBook) {
            // 一张表的二维数组
            List<String[]> excelInfo;
            // 声名sheet 引用
            Sheet sheet;
            // 循环所有 sheet工作表
            for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
                // 获取 sheet
                sheet = workBook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                excelInfo = new ArrayList<>();
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                // 行数
                int rowSize = lastRowNum - firstRowNum + 1;
//                System.out.println("行数 : " + rowSize);
                // 从有信息的行开始 遍历 所有以下所有行
                for (int rowNum = firstRowNum; rowNum < firstRowNum + rowSize; rowNum++) {
                    // 获取行 对象
                    Row row = sheet.getRow(rowNum);
                    if (null == row) {
                        continue;
                    }
                    // 获取当前行的元素有几个
                    int cellSize = row.getLastCellNum();
//                    System.out.println("列数 : " + cellSize);
                    String[] rowInfo = new String[cellSize];
                    for (int i = 0; i < cellSize; i++) {
                        Cell cell = row.getCell(i);
                        if (null == cell) {
                            continue;
                        }
                        rowInfo[i] = cell.toString();
                    }
                    excelInfo.add(rowInfo);
                }
                sheetList.add(excelInfo);
            }
        }
        return sheetList;
    }

    /**
     * 获取excel对象  2003  或 2007
     * @param file
     * @return
     */
    private Workbook getWorkBook(MultipartFile file) {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 创建excel文件对象
        Workbook workbook = null;
        try {
            // 获取IO流
            InputStream inputStream = file.getInputStream();
            if (fileName.endsWith(Constants.XLS_SUFFIX)) {
                // 2003
                workbook = new HSSFWorkbook(inputStream);
            }else if(fileName.endsWith(Constants.XLSX_SUFFIX)) {
                // 2007
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }
}
