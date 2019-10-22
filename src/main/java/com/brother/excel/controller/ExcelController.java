package com.brother.excel.controller;

import com.brother.excel.constants.Constants;
import com.brother.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 创建人：王福顺  创建时间：2019/10/12
 */
@Controller
public class ExcelController {

    @Autowired
    private ExcelService excelServiceApi;

    @Autowired
    private HttpServlet httpServlet;

    @RequestMapping("/upLoadExcel")
    public String upLoadExcel(Map map, @RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile) {
        // 安全策略，文件满20个 不让上传
        File fileParent = new File(Constants.OUT_PUT_PATH);
        if (!uploadFile.isEmpty()) {
            if (fileParent.list().length < 20) {
                excelServiceApi.createExcel(uploadFile);
                map.put("message", "上传成功");
                map.put("status", "1");
            } else {
                map.put("message", "文件服务器已满");
                map.put("status", "0");
            }
        } else {
            map.put("message", "未上传文件");
            map.put("status", "0");
        }
        return "index";
    }


    @RequestMapping("/downLoadExcel")
    // 不加此注解 会报错 但是不影响功能
    // 原因： 在使用此注解之后不会再走试图处理器，而是直接将数据写入到流中，他的效果等同于通过response对象输出指定格式的数据。
    @ResponseBody
    public String downLoadExcel(HttpServletResponse response, Map map) {
        excelServiceApi.downLoadExcel(response);
        // 设置 http 头
        response.setHeader("Content-Disposition", "attachment;filename=" + Constants.ZIP_FILENAME);
        // 设置 指定。。什么什么
        String mimeType = httpServlet.getServletContext().getMimeType(Constants.ZIP_FILENAME);
        response.setContentType(mimeType);

        return "index";
    }


}