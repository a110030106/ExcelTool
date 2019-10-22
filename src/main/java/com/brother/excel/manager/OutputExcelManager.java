package com.brother.excel.manager;

import com.brother.excel.constants.Constants;
import com.brother.excel.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 创建人：王福顺  创建时间：2019/10/12
 * 用于生成excel表格  并写入
 */
@Component
public class OutputExcelManager {

    @Autowired
    private ExcelStyleManager excelStyleManager;

    /**
     *  创建Excel 并写入
     * @param file
     * @param sheets
     */
    public void createExcel(MultipartFile file, List<List<String[]>> sheets) {

        String fileName = file.getOriginalFilename();
        // 创建excel对象
        Workbook workBook = getWorkBook(file.getOriginalFilename());
        // 创建sheet对象
        Sheet newSheet;
        // 创建第一行
        Row newRow;
        // 创建第一行的 第一列
        Cell newCell = null;
        // 遍历所有sheet
        for (int sheetNum = 0; sheetNum < sheets.size(); sheetNum++) {
            newSheet = workBook.createSheet();
            // 合并单元格
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, Constants.FIELD_NAME.length - 1);
            newSheet.addMergedRegion(region);
            operationSheet(newSheet);
            // 遍历每个sheet所有行
            List<String[]> rows = sheets.get(sheetNum);
            for (int rowNum = 0; rowNum < rows.size(); rowNum++) {
                newRow = newSheet.createRow(rowNum);
                // 遍历每行的 列
                String[] cells = rows.get(rowNum);
                for (int cellNum = 0; cellNum < cells.length; cellNum++) {

                    String cell = cells[cellNum];
                    newCell = newRow.createCell(cellNum);
                    // 获取全局样式
                    CellStyle cellStyle = excelStyleManager.getAllCellStyle(workBook);
                    // 设置单元格样式：
                    excelStyleManager.buildSelfStyle(cellStyle, cellNum, workBook, newSheet);
                    // 全局样式 ：
                    newCell.setCellStyle(cellStyle);
                    if (null == cell) {
                        newCell.setCellValue("");
                        continue;
                    }
                    // 值 ：
                    newCell.setCellValue(checkInt(cell));
                }
            }
        }
        try{
            // 生成输出 文件名
            String newFileName = createFileName(fileName);
            //创建输出文件位置
            FileOutputStream f = new FileOutputStream(Constants.OUT_PUT_PATH + newFileName);
            workBook.write(f);
            f.flush();
            f.close();

        }catch(IOException e){
            System.out.println("IO异常");
        }
    }

    /**
     * 生成新文件名
     * @param fileName
     * @return
     */
    private String createFileName(String fileName) {
        String newFileName = "";
        // 文件前缀（区分cmbi内部 与 外部员工）
        String fileNamePre = fileName.split("_")[0];
        if (fileNamePre.equals(Constants.FILENAME_PREFIX_CMBI)) {
            newFileName += Constants.FILENAME_PREFIX_CMBI + "_";
        }else {
            newFileName += (int)(100 + (Math.random() * 900)) + "-" + Constants.FILENAME_PREFIX_OTHER + "_";
        }
        // 文件后缀（文件类型）
        if (fileName.endsWith(Constants.XLS_SUFFIX)) {
            // 2003
            newFileName += DateUtil.dateToStringOnlyDate(new Date()) + "." +Constants.XLS_SUFFIX;
        }else if(fileName.endsWith(Constants.XLSX_SUFFIX)) {
            // 2007
            newFileName += DateUtil.dateToStringOnlyDate(new Date()) + "." + Constants.XLSX_SUFFIX;
        }
        return newFileName;
    }

    /**
     * 设置sheet
     * @param sheet
     */
    private void operationSheet(Sheet sheet) {
        // 设置 宽度 自适应 （失效 。。。）
        sheet.autoSizeColumn(1, true);
    }


    /**
     *  写Excel时 创建空表
     * @param fileName
     * @return
     */
    private Workbook getWorkBook(String fileName) {
        // 创建excel文件对象
        Workbook workbook = null;
        if (fileName.endsWith(Constants.XLS_SUFFIX)) {
            // 2003
            workbook = new HSSFWorkbook();
        }else if(fileName.endsWith(Constants.XLSX_SUFFIX)) {
            // 2007
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    /**
     * 检查是否是整数  ，是：则取消小数位显示 （默认会保留1位小数   1.0   2.0）
     * @param cell
     * @return
     */
    private String checkInt(String cell) {
        try{
            // 未发生异常，说明是Number类型
            Double cellDouble = Double.valueOf(cell);
            // 取整
            Integer cellInt = (int)Math.floor(cellDouble);
            // 查看是否整数，整数则 不保留小数位
            if (cellDouble % cellInt == 0){
                return cellInt.toString();
            }else {
                return cellDouble.toString();
            }
        }catch (NumberFormatException e){
            return cell;
        }
    }

}
