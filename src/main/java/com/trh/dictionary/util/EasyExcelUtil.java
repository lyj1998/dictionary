package com.trh.dictionary.util;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导出工具类
 */
public class EasyExcelUtil {
    public static void exportExcelWeb(HttpServletResponse response, List<T> list, Class clazz, String fileName, String sheetName){
        if (com.alibaba.excel.util.StringUtils.isBlank(sheetName)){
            sheetName = "sheet1";
        }
        if (com.alibaba.excel.util.StringUtils.isBlank(fileName)){
            sheetName = "数据字典";
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //这里URLEncoder.encode可以防止中文乱码，当然和easyexcel没有关系
            String fileSuffix = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            fileName = fileSuffix.concat("-").concat(fileName);
            String fileNameAfterEncode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition","attachment;filename*=filename*=utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
