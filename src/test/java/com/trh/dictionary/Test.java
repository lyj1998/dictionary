package com.trh.dictionary;

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

import java.io.File;
import java.sql.*;
import java.util.List;

/**
 * @author
 * @create 2019-07-25 11:04
 */
public class Test {

    static Logger logger = LoggerFactory.getLogger(Test.class);

    @org.junit.Test
    public void testMakeMySqlPdf() {
        String FILE_DIR = "F:/pdf/";
        //生成PDF文件
//        BuildPDF.createPdf("localhost", "cd_core", "3306", "root", "root", FILE_DIR, "cd_core");
        //生成MARKDOWN文件
        BuildPDF.MakeMarkDown("localhost", "cd_core", "3306", "root", "root", FILE_DIR);

        String proFilePath = System.getProperty("user.dir");
        System.out.println(proFilePath);
        String fontDir = BuildPDF.class.getResource("/").getPath().replaceAll("target/classes/", "").replaceAll("target/test-classes/", "");
        fontDir += "src/main/resources/";
        System.out.println(fontDir);
    }

    @org.junit.Test
    public void testMakePgSqlPdf() {
        String FILE_DIR = "F:/pdf/";
        //生成PDF文件
        try {
//            BuildPgSqlPdf.buildPdf("192.168.161.3", "mydata", "54321", "postgres", "root", FILE_DIR, "mydata");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //生成MARKDOWN文件
//        BuildPDF.MakeMarkDown("localhost", "cd_core", "3306", "root", "root",FILE_DIR);
    }


    @org.junit.Test
    public void testMakeSqlServerPdf() {
        String FILE_DIR = "F:/pdf/";
//        BuildSqlserverPDF.MakePdf("192.168.161.3", "zhou", "1433", "SA", "zhoufan123AAA", FILE_DIR, "zhou");
    }

    @org.junit.Test
    public void testMakeOraclePdf() {
        try {
            List<TableInfo> tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//127.0.0.1:1521/orcl", "root", "123456");
            if (tableInfo.size() == 0) {
                return;
            }
            String filePath = "F:/pdf/";
            FileUtils.forceMkdir(new File(filePath));
            //带目录
            BuildPDF.build(filePath, tableInfo, "Oraclecd_core12");
            String markdown = BuildPDF.writeMarkdown(tableInfo);
            System.out.println(markdown);
            System.out.println("生成数据字典完毕,一共生成了" + tableInfo.size() + "条数据");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testMakeDb2ServerPdf() throws Exception {
        Connection connection = SqlExecutor.newDB2Connection("192.168.171.230", 50000, "TEST", "db2", "system");
        List<TableInfo> tableInfo = Db2Executor.getDB2Tables(connection, "TEST");
        if (tableInfo.size() == 0) {
            return;
        }
        String filePath = "E:/pdf/";
        FileUtils.forceMkdir(new File(filePath));
        //带目录
        BuildPDF.build(filePath, tableInfo, "Db2");
        BuildPDF.writeMarkdown(tableInfo);
    }


    @org.junit.Test
    public void testMakeSqlServerMarkdown() {
        String FILE_DIR = "F:/pdf/";
        WriteSqlserverMarkDown.MakeMarkdown("192.168.161.3", "zhou", "1433", "SA", "zhoufan123AAA", FILE_DIR);
    }

    @org.junit.Test
    public void testMakeSqlServerMarkdownStrig() {
        logger.info(WriteSqlserverMarkDown.MakeMarkdownString("192.168.161.3", "zhou", "1433", "SA", "zhoufan123AAA"));
    }
@org.junit.Test
    public void testConnect(){
        /**
         * 1.加载驱动
         * 2.连接数据库
         * 3.写下想要执行的sql
         * 4.执行sql语句
         * 5.关闭连接
         */
        String url = "jdbc:mysql://localhost:3306/srb_core?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
        Connection conn = null;
        Statement stmt = null;

        try {
            //注册jdbc驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("打开数据库连接");
//            Connection connection = ConnectionFactory.getConnection(url, "root", "123456", "mySql");
            //tableInfo = BuildMysqlPdf.getBuildPdfTableData(BuildMysqlPdf.getTables(connection, database));
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(url,"root","123456");

            // 执行查询
            System.out.println("输入sql语句后并执行...");
            stmt = conn.createStatement();
            String sql;
            sql = "select * from emp";// 这里填写需要的sql语句
            //执行sql语句
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                int id  = rs.getInt("id");//获取id值
                String name = rs.getString("name");//获取name值
                int age = rs.getInt("age");
                // 输出数据
                System.out.println("id: " + id);
                System.out.println("名字: " + name);
                System.out.println("年龄" + age);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("\n执行成功！");
    }


}
