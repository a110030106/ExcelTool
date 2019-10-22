package com.brother.excel.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 创建人：王福顺  创建时间：2019/10/12
 */
public interface ExcelService {

    /**
     * 生成 某个月的 excel  到 目录 /usr/jetty-80-ExcelTool/YYYY-MM-DD.xxxx
     *
     * @param uploadFile
     */
    public void createExcel(MultipartFile uploadFile);

    /**
     * 读取excel文件
     */
    public void downLoadExcel(HttpServletResponse response);
}
