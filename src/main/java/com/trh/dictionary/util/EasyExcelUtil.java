package com.trh.dictionary.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.trh.dictionary.controller.DatabaseController;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 导出工具类
 */
public class EasyExcelUtil {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(EasyExcel.class);

    public static void exportExcelWeb(HttpServletResponse response, List<List<?>> dataLists, List<Class> clazz, String fileName, List<String> sheetName){
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //这里URLEncoder.encode可以防止中文乱码，当然和easyexcel没有关系
            String fileSuffix = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            fileName = fileSuffix.concat("-").concat(fileName);
            String fileNameAfterEncode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileNameAfterEncode + ".xlsx");
            //多sheet导出
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            //表级信息
            int i = 0;
            while (i<clazz.size()){
                WriteSheet sheet1 = EasyExcel.writerSheet(i, sheetName.get(i)).head(clazz.get(i)).build();
                excelWriter.write(dataLists.get(i), sheet1);
                i++;
            }
            excelWriter.finish();
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败"+e.getMessage());
            try {
                JSONObject jsonObject = new JSONObject(map);
                response.getWriter().println(jsonObject.toString());
            } catch (IOException e1) {
                logger.error("文件错误信息返回失败{}", e);
            }
            logger.error("导出文件失败{}", e);
        }
    }


}
