package com.trh.dictionary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trh.dictionary.bean.HistoryConnet;
import com.trh.dictionary.bean.TableInfo;
import com.trh.dictionary.dao.ConnectionFactory;
import com.trh.dictionary.service.BuildPDF;
import com.trh.dictionary.service.db2.Db2Executor;
import com.trh.dictionary.service.mysql.BuildMysqlPdf;
import com.trh.dictionary.service.oracleservice.OracleDatabase;
import com.trh.dictionary.service.postgreSQL.BuildPgSqlPdf;
import com.trh.dictionary.service.sqlserver.BuildSqlserverPDF;
import com.trh.dictionary.service.sqlserver.WriteSqlserverMarkDown;
import com.trh.dictionary.util.SqlExecutor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.*;

/**
 * @author: LuYuJie
 * @program: database-dictionary
 * @description: API
 * @create: 2022/11/20
 **/
@Controller
public class DatabaseController {
    static Logger logger = LoggerFactory.getLogger(DatabaseController.class);


    @GetMapping("/{url}")
    public String url(@PathVariable("url")String url){
        return url;
    }

    @RequestMapping(value = "/history.action",method = RequestMethod.GET)
    public String history(Model model){

        /*List<List> list = new ArrayList<>();
        List<String> list_sub = new ArrayList<String>();
        list_sub.add("history 001");list_sub.add("history 002");list_sub.add("history 003");
        list.add(list_sub);


        list_sub = new ArrayList<String>();
        list_sub.add("history 005");list_sub.add("history 006");list_sub.add("history 007");
        list.add(list_sub);

        list_sub = new ArrayList<String>();
        list_sub.add("history a001");list_sub.add("history a002");list_sub.add("history a003");
        list.add(list_sub);

        list_sub = new ArrayList<String>();
        list_sub.add("history a005");list_sub.add("history a006");list_sub.add("history a007");
        list.add(list_sub);*/

        List<List<HistoryConnet>> list =  read_recordHistory();

        model.addAttribute("history", list);
        return "history";
    }

    @RequestMapping(path = "/login.action", method = RequestMethod.POST)
    public String loginDB(HttpServletRequest request, HttpServletResponse response, String selector, String ip, String port, String password, String username, String database) {
        List<TableInfo> tableInfo = null;
        try {
            String markdown = null;
            Boolean success = false;
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
                    success = ConnectionFactory.testConnection(url, username, password, "mySql");
                    break;
                case "oracle":
                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
                    break;
                case "SQL server":
                    markdown = WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
                    break;

                case "PostgreSQL":
                    markdown = BuildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
                    break;
                case "DB2":
                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
                    break;
            }
            if(success){
                return "main";
            }else {
                return "index";
            }
        } catch (Exception e) {
            logger.error("login==>"+e.getMessage());
            return "index";
        }
    }




//    @RequestMapping("/login.action")
//    public String login(Model model, String selector, String ip, String port, String password, String username, String database) {
//        List<TableInfo> tableInfo = null;
//        try {
//            String markdown = null;
//            switch (selector) {
//                case "mysql":
//                    //得到生成数据
//                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
//                    Connection connection = ConnectionFactory.getConnection(url, username, password, "mySql");
//                    tableInfo = BuildMysqlPdf.getBuildPdfTableData(BuildMysqlPdf.getTables(connection, database));
//                    break;
//                case "oracle":
//                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
//                    break;
//                case "SQL server":
//                    markdown = WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
//                    break;
//
//                case "PostgreSQL":
//                    markdown = BuildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
//                    break;
//                case "DB2":
//                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
//                    break;
//            }
//
//
//            if (tableInfo !=null ){
//
//                if (tableInfo.size() == 0) {
//                    model.addAttribute("markdown", "## 数据库无数据");
//                    return "markdown";
//                }
//                markdown = BuildPDF.writeMarkdown(tableInfo);
//            }
//
//
//            model.addAttribute("markdown", markdown);
//            //记录历史连接
//            HistoryConnet historyConnet = new HistoryConnet( selector,  ip,  port,  password,  username,  database);
//            recordHistory(markdown,historyConnet);
//            return "markdown";
//        } catch (Exception e) {
//            logger.error("error==>"+e);
//            model.addAttribute("markdown", "### "+e.getMessage());
//            return "markdown";
//        }
//    }

    @RequestMapping("/getDataBaseNameList")
    @ResponseBody
    public List<String> getDataBaseNmaeList(String selector,String ip, String port, String password, String username, String database){
        List<String> list = new ArrayList<>();
        try {
            switch (selector) {
                case "mysql":
                    return BuildMysqlPdf.getDataBaseName(ip,database,port,username,password);
                case "oracle":
                    list.add(database);
                    return list;
                case "SQL server":

                    return WriteSqlserverMarkDown.getDatabasesList(ip, database, port, username, password);

                case "PostgreSQL":
                    return BuildPgSqlPdf.getDataBaseName(ip,database,port,username,password);
                case "DB2":
                    return Db2Executor.databases(SqlExecutor.newDB2Connection(ip,Integer.valueOf(port),database,username,password));
            }

        } catch (Exception e) {
            logger.error("error==>"+e);
        }
        return list;
    }

    @RequestMapping("/getTableNameList")
    public List<String> getTableNameList(String selector,String ip, String port, String password, String username, String database){
        List<String> list = new ArrayList<>();
        try {
            switch (selector) {
                case "mysql":
                    return BuildMysqlPdf.getDataBaseName(ip,database,port,username,password);
                case "oracle":
                    list.add(database);
                    return list;
                case "SQL server":

                    return WriteSqlserverMarkDown.getDatabasesList(ip, database, port, username, password);

                case "PostgreSQL":
                    return BuildPgSqlPdf.getDataBaseName(ip,database,port,username,password);
                case "DB2":
                    return Db2Executor.databases(SqlExecutor.newDB2Connection(ip,Integer.valueOf(port),database,username,password));
            }

        } catch (Exception e) {
            logger.error("error==>"+e);
        }
        return list;
    }

    @RequestMapping("/getMarkdownString")
    @ResponseBody
    public String getMarkdownString(Model model, String selector, String ip, String port, String password, String username, String database) {
        List<TableInfo> tableInfo = null;
        try {
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
                    Connection connection = ConnectionFactory.getConnection(url, username, password, "mySql");
                    tableInfo = BuildMysqlPdf.getBuildPdfTableData(BuildMysqlPdf.getTables(connection, database));
                    break;
                case "oracle":
                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
                    break;
                case "SQL server":
                    return WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
                case "PostgreSQL":
                    return  BuildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
                case "DB2":
                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
                    break;
            }
            if (tableInfo!=null){
                if (tableInfo.size() == 0) {
                    return "## 数据库无数据";
                }
            }

            String markdown = BuildPDF.writeMarkdown(tableInfo);

            return markdown;
        } catch (Exception e) {
            logger.error("error==>"+e);
            return "### "+e.getMessage();
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void testDownload(HttpServletResponse res, String selector, String ip, String port, String password, String username, String database) {
        //1.先生成pdf文件
        String filePath = System.getProperty("user.dir");
        try {
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    BuildMysqlPdf.MakeMysqlPdf(ip, database, port, username, password, res);
                    break;
                case "oracle":
                    List<TableInfo> tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
                    if (tableInfo.size() == 0) {
                        return;
                    }
                    //带目录
                    BuildPDF.getDocumentBuild(tableInfo,res);
                    break;
                case "SQL server":
                    BuildSqlserverPDF.MakePdf(ip, database, port, username, password,res);
                    break;
                case "PostgreSQL":
                    BuildPgSqlPdf.buildPdf(ip, database, port, username, password,res);
                    break;
                case "DB2":
                    List<TableInfo> Db2tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database, username, password);
                    if (Db2tableInfo.size() == 0) {
                        return;
                    }
                    //带目录
                    BuildPDF.getDocumentBuild( Db2tableInfo,res);
                    break;
            }
        } catch (Exception e) {
            logger.error("error==>" + e);
        }
    }


    @RequestMapping(value = "/exportDict", method = RequestMethod.GET)
    public void testDict(HttpServletResponse res, String selector, String ip, String port, String password, String username, String dbName) {
        //1.先生成excel文件
        String filePath = System.getProperty("user.dir");
        System.out.println("生成路径"+filePath);
        try {
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    BuildMysqlPdf.MakeMysqlExcel(ip, dbName, port, username, password,res);
                    break;
                case "oracle":
                    List<TableInfo> tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + dbName + "", username, password);
                    if (tableInfo.size() == 0) {
                        return;
                    }
                    //带目录
                    BuildPDF.getDocumentBuild(tableInfo,res);
                    break;
                case "SQL server":
                    BuildSqlserverPDF.MakePdf(ip, dbName, port, username, password,res);
                    break;
                case "PostgreSQL":
                    BuildPgSqlPdf.buildPdf(ip, dbName, port, username, password,res);
                    break;
                case "DB2":
                    List<TableInfo> Db2tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), dbName, username, password);
                    if (Db2tableInfo.size() == 0) {
                        return;
                    }
                    //带目录
                    BuildPDF.getDocumentBuild( Db2tableInfo,res);
                    break;
            }
        } catch (Exception e) {
            logger.error("error==>" + e);
        }
    }

    /****************记录连接历史****************/
    //记录连接历史
    private void  recordHistory(String markdown,HistoryConnet historyConnet){
        try {
            if(!StringUtils.isEmpty(markdown) && markdown.length() > 30){
                //MD5
                historyConnet.setMd5Str(historyConnet.MakeMd5Str());

                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(historyConnet);
                //这里记录连接成功的记录
                File fileToWrite1 = FileUtils.getFile("history/history.txt");
                if(!fileToWrite1.getParentFile().exists()){
                    try {
//                        logger.info("writing lines to a file.");
                        fileToWrite1.getParentFile().mkdirs();
                        fileToWrite1.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //检查是否有重复
                List<String> list_string = FileUtils.readLines(fileToWrite1, Charset.defaultCharset());
                if(null != list_string && !list_string.contains(jsonString)){
                    Collection lines = new ArrayList<>();
                    lines.add(jsonString);
                    FileUtils.writeLines(fileToWrite1, lines,true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("记录连接历史错误..................");
        }
    }

    //读取连接历史
    private List<List<HistoryConnet>>  read_recordHistory(){
        try {
            List<List<HistoryConnet>> list = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();

            //这里记录连接成功的记录
            logger.info("writing lines to a file.");
            File fileToWrite1 = FileUtils.getFile("history/history.txt");
            if(!fileToWrite1.exists()){
                return null;
            }
            List<String> list_string = FileUtils.readLines(fileToWrite1, Charset.defaultCharset());
            if(null != list_string && list_string.size() > 0){

                List<HistoryConnet> list_sub = null;
                int count = list_string.size();
                for (int i = 0; i < count; i++) {
                    if( i % 4 == 0){
                        list.add(list_sub);
                        list_sub = new ArrayList<>();
                    }

                    HistoryConnet historyConnet = mapper.readValue(list_string.get(i), HistoryConnet.class);
                    list_sub.add(historyConnet);

                    //最后一次检验
                    if(i == count - 1  && list_sub.size() > 0){
                        list.add(list_sub);
                    }
                }
            }
            return list;

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("记录连接历史错误..................");
            return null;
        }
    }

    /**
     * 获取所有的表信息
     * @param selector
     * @param ip
     * @param port
     * @param password
     * @param username
     * @param database
     * @return
     */
    @PostMapping(value = "/getTables")
    public ModelAndView list(String selector, String ip, String port, String password, String username, String database){
        ModelAndView modelAndView = new ModelAndView();
        List<TableInfo> tables = new ArrayList<>();
        try {
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
                    Connection connection = ConnectionFactory.getConnection(url, username, password, "mySql");
                    tables = BuildMysqlPdf.getAllTables(connection, database);
                    break;
//                case "oracle":
//                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
//                    break;
//                case "SQL server":
//                    return WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
//                case "PostgreSQL":
//                    return BuildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
//                case "DB2":
//                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
//                    break;
            }
        }catch (Exception e){
            logger.error("getAllTablesError==>"+e);
        }
        modelAndView.addObject("tables", tables);
        System.out.println(tables);
        modelAndView.setViewName("table_list");
        return modelAndView;
    }



//    @RequestMapping(value = "/filterTable", method = RequestMethod.POST)
//    public Map<String, Object> showTables(PageUtil page, String search, String selector, String ip, String port, String password, String username, String database) {
//        Map<String, Object> map = new HashMap<>();
//        Integer count = 0;
//        try {
//            switch (selector) {
//                case "mysql":
//                    //得到生成数据
//                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
//                    Connection connection = ConnectionFactory.getConnection(url, username, password, "mySql");
//                    map = BuildMysqlPdf.getAllTables(connection, database, search, page.getOffset(), page.getLimit());
//                    break;
////                case "oracle":
////                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
////                    break;
////                case "SQL server":
////                    return WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
////                case "PostgreSQL":
////                    return BuildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
////                case "DB2":
////                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
////                    break;
//            }
//
//            //设置map
//            map.put("code", 0);
//            map.put("msg", "success");
//            System.out.println(map.get("count"));
////            map.put("count", pageInfo.getTotal());
////            //设置分页好的列表
////            map.put("data", pageInfo.getList());
//
//            //return tableService.filterMemebers(param);
//
////            String markdown = BuildPDF.writeMarkdown(tableInfo);
//
//            return map;
//        }catch (Exception e){
//            logger.error("error==>"+e);
//            map.put("data", e.getMessage());
//            map.put("code", 1);
//            map.put("msg", "error");
//            map.put("count", 0);
//            return map;
//        }
//
//    }

}