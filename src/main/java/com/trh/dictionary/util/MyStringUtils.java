package com.trh.dictionary.util;

import com.trh.dictionary.service.BuildPDF;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2021/7/8.
 *
 * @author 敬小虎
 * Description:
 */
public class MyStringUtils {

    /**
     * 处理注释换行问题
     * @param  commentDesc : 数据库字段的注释
     */
    public static String resolveLineFeed(String commentDesc){
        if(!StringUtils.isEmpty(commentDesc)){
            //去换行符
            return  StringUtils.delete(commentDesc,"\\r|\\n");
        }
        return  "";

    }

    /**
     * 清理注释中的  \r\n
     * @param comment
     * @return
     */
    public  static   String  delete_R_N(String comment){
        Pattern myPattern = Pattern.compile("\\s*|\r|\n");
        Matcher m = myPattern.matcher(comment);
        return  m.replaceAll("");
    }

    public  static  String  delete_Pattern(String source){
        return  BuildPDF.dest(BuildPDF.dest(source, SignEnum.line_break.getDesc()),SignEnum.blank.getDesc());
    }

    /**
     * 删除指定字符
     * @param source
     * @return
     */
    public  static  String  delete_Specified_Character(String source,String ... specifiedCharacter){
        StringBuffer sb = new StringBuffer(source);
        Arrays.asList(specifiedCharacter).stream().forEach(xx->{
            String temp  =  sb.toString().replaceAll(xx,"");
            sb.delete(0,sb.length());
            sb.append(temp);
        });
        return  sb.toString();
    }

}
