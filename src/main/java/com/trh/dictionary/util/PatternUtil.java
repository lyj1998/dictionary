package com.trh.dictionary.util;

import com.trh.dictionary.dto.EnumCodeInfoDto;
import com.trh.dictionary.service.mysql.BuildMysqlPdf;
import org.apache.poi.ss.formula.functions.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则工具类
 */
public class PatternUtil {
    static Logger logger = LoggerFactory.getLogger(PatternUtil.class);
    /**
     *
     * @param s 字符串
     * @param flag  1代表获取是否是枚举值   2获取字段值  3 获取每个枚举值
     */
    public static Map<String, Object> getEnum(String s, EnumCodeInfoDto enumCodeInfoDto, int flag){
        Map<String, Object> map = new HashMap<>(3);
        //匹配的子串  多个数字  分隔符：0个或者多个空格 1个及1个以上-    1个及以上字母数字或者下划线或者一个及一个以上中文
        Matcher m = Pattern.compile("(\\d+)(\\s*|-{1,}|\\uff1a+|:{1,})(\\w+|[\\u4e00-\\u9fa5]+)").matcher(s);//        //每个匹配到的子串分组的个数
        int count = 0;
        String columnComment = "";
        List<EnumCodeInfoDto> dictList = null;
        if (flag == 1){
            count = columnIsEnum(m);
        }
        if (flag == 2){
            columnComment = getColumnComment(m, s);
        }
        if (flag == 3){
            dictList = getEnumCode(m, enumCodeInfoDto);
        }
        map.put("count", count);
        map.put("columnComment", columnComment);
        map.put("dictList", dictList);
        return map;
    }

    /**
     * 通过匹配次数判断是否是枚举值
     * @param m
     * @return
     */
    private static int columnIsEnum(Matcher m) {
        int count = 0;
        while (m.find()){
            logger.info("匹配到的子串:"+m.group());
            count++;
//            for (int i = 1; i<=group; i++){
//                System.out.println("分组"+i+":"+m.group(i));
//            }
        }
        return count;
    }

    /**
     * 获取枚举值
     * @param m
     * @param enumCodeInfoDto
     * @return
     */
    public static List<EnumCodeInfoDto> getEnumCode(Matcher m, EnumCodeInfoDto enumCodeInfoDto){
        List<EnumCodeInfoDto> dictList = new ArrayList<>();
        int group = m.groupCount();
        EnumCodeInfoDto dto = null;
        while (m.find()){
//            logger.info("匹配到的子串:"+m.group());
                dto = (EnumCodeInfoDto) enumCodeInfoDto.clone();
                dto.setCode(m.group(1));
                dto.setCodeDes(m.group(3));
                dictList.add(dto);
//            for (int i = 1; i<=group; i++){
//                logger.info("分组"+i+":"+m.group(i));
//            }
        }
        return dictList;
    }

    /**
     * 获取字段注释
     * @param s
     * @return
     */
    private static String getColumnComment(Matcher m, String s) {
        //count大于等于2说明是枚举值
        int count = columnIsEnum(m);
        if (count >= 2){
            m = Pattern.compile("(\\d+)(\\s*|-{1,}|\\uff1a+|:{1,})(\\w+|[\\u4e00-\\u9fa5]+)").matcher(s);
            while (m.find()){
//                logger.info("m.group=========>"+m.group());
                //匹配到的第一个子串
//                System.out.println("匹配到的第一个子串"+m.group());
                s = s.replaceAll(m.group(), "");
            }
            return clearComment(s);
        }
        return s;
    }

    /**
     * 去除空格，括号，逗号等特殊字符
     * @param str
     * @return
     */
    public static String clearComment(String str){
        //可以替换大部分空白字符， 不限于空格 . 说明:\s 可以匹配空格、制表符、换页符等空白字符的其中任意一个
        //清除所有符号,只留下字母 数字 汉字 共3类.
        String s=str.replaceAll("\\s*","").replaceAll("[\\pP\\p{Punct}]","");
        return s;
    }

}
