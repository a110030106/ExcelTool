package com.brother.excel.serviceImpl;

import com.brother.excel.constants.Constants;
import com.brother.excel.manager.InputExcelManager;
import com.brother.excel.manager.OutputExcelManager;
import com.brother.excel.manager.ReadExcelManager;
import com.brother.excel.manager.WriteExcelManager;
import com.brother.excel.models.StatisticsByMonth;
import com.brother.excel.service.ExcelService;
import com.brother.excel.util.DateUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 创建人：王福顺  创建时间：2019/10/14
 * 读取  excel表格  并统计后，输出 一个 新 excel表格
 */
@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private InputExcelManager inputExcelManager;

    @Autowired
    private OutputExcelManager outputExcelManager;

    @Autowired
    private ReadExcelManager readExcelManager;

    @Autowired
    private WriteExcelManager writeExcelManager;

    /**
     * 读取Excel，并生成文件
     * @param uploadFile
     */
    @Override
    public void createExcel(MultipartFile uploadFile) {

        // 加载   excel表格 -->  List数组
        List<List<String[]>> excelList = inputExcelManager.loadExcel(uploadFile);

        // 获取 统计信息
        List<StatisticsByMonth> oneRowInfoList = readExcelManager.readExcel(excelList);

        // 将 统计信息  写入 新list中
        List<List<String[]>> newExcelList = writeExcelManager.writeExcel(oneRowInfoList);

        // 输出   List数组 -->  excel表格
        outputExcelManager.createExcel(uploadFile, newExcelList);

    }

    /**
     * 下载Excel文件
     */
    @Override
    public void downLoadExcel(HttpServletResponse response) {

        File fileParent = new File(Constants.OUT_PUT_PATH);
        List<String> filesPath = createFilesPath(fileParent.list());
        // 创建 zip文件
        createZip(filesPath);
        // 下载 zip文件
        buildResponseSteam(response);
    }

    /**
     * 找到今天上传的 excel 全路径
     * @param filesName 参数 都是 文件名（无路径）
     * @return
     */
    private List<String> createFilesPath(String[] filesName) {
        List<String> filesPathList = new ArrayList<>();
        String excelName1NoSuf = Constants.OUT_PUT_PATH + Constants.FILENAME_PREFIX_CMBI + "_" + DateUtil.dateToStringOnlyDate(new Date());
        String excelName2NoSuf = Constants.OUT_PUT_PATH + Constants.FILENAME_PREFIX_OTHER + "_" + DateUtil.dateToStringOnlyDate(new Date());
        String cmbiExcel2003 = excelName1NoSuf + "." + Constants.XLS_SUFFIX;
        String otherExcel2003 = excelName2NoSuf + "." + Constants.XLS_SUFFIX;
        String cmbiExcel2007 = excelName1NoSuf + "." + Constants.XLSX_SUFFIX;
        String otherExcel2007 = excelName2NoSuf + "." + Constants.XLSX_SUFFIX;
        for (int i = 0; i < filesName.length; i++) {
            String noRandomFileName = Constants.OUT_PUT_PATH + filesName[i].substring(4);
            filesName[i] = Constants.OUT_PUT_PATH + filesName[i];
            System.out.println("没有数字前缀 ：" + noRandomFileName);
            System.out.println("实际文件名 ：" + filesName[i]);
            System.out.println("比较用文件名 ： " + cmbiExcel2007 + " | " + otherExcel2007);
            if (filesName[i].equals(cmbiExcel2003)) {
                filesPathList.add(filesName[i]);
            }else if (noRandomFileName.equals(otherExcel2003)) {
                filesPathList.add(filesName[i]);
            }else if (filesName[i].equals(cmbiExcel2007)) {
                filesPathList.add(filesName[i]);
            }else if (noRandomFileName.equals(otherExcel2007)) {
                filesPathList.add(filesName[i]);
            }
        }
        return filesPathList;
    }

    /**
     * 创建zip压缩文件
     */
    private void createZip(List<String> targetFilePathList) {
        // 目标zip文件 生成
        File targetZipFile = new File(Constants.OUT_PUT_PATH + Constants.ZIP_FILENAME);
        InputStream in = null;
        FileOutputStream fos = null;
        ZipOutputStream zipOutputStream = null;
        try {
            fos = new FileOutputStream(targetZipFile);
            zipOutputStream = new ZipOutputStream(fos);
            for (String csvFilePath : targetFilePathList) {
                in = new FileInputStream(csvFilePath);
                String csvFileName = csvFilePath.substring(csvFilePath.lastIndexOf(File.separator) + 1);
                zipOutputStream.putNextEntry(new ZipEntry(csvFileName));
                IOUtils.copy(in, zipOutputStream);
                zipOutputStream.closeEntry();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输入流 放入 response的 输出流中  提供下载
     * @param response
     */
    private void buildResponseSteam(HttpServletResponse response) {
        try {
            // 获取 response 输出流
            ServletOutputStream out = response.getOutputStream();
            out.flush();
            FileInputStream in = new FileInputStream(Constants.OUT_PUT_PATH + Constants.ZIP_FILENAME);
            byte[] buffer = new byte[1024];
            int len = 0;
            // 将 输入流写到   response 输出流
            while((len=in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成新文件名
     * @return
     */
//    private String findFileName() {
//        String newFileName = "";
//        // 文件前缀（区分cmbi内部 与 外部员工）
//        String fileNamePre = fileName.split("_")[0];
//        if (fileNamePre.equals(Constants.FILENAME_PREFIX_CMBI)) {
//            newFileName += Constants.FILENAME_PREFIX_CMBI + "_";
//        }else {
//            newFileName += Constants.FILENAME_PREFIX_OTHER;
//        }
//        // 文件后缀（文件类型）
//        if (fileName.endsWith(Constants.XLS_SUFFIX)) {
//            // 2003
//            newFileName += DateUtil.dateToStringOnlyDate(new Date()) + "." +Constants.XLS_SUFFIX;
//        }else if(fileName.endsWith(Constants.XLSX_SUFFIX)) {
//            // 2007
//            newFileName += DateUtil.dateToStringOnlyDate(new Date()) + "." + Constants.XLSX_SUFFIX;
//        }
//        return newFileName;

//    }
}



