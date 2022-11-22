package com.trh.dictionary.bean;

import org.springframework.core.codec.StringDecoder;
import org.springframework.util.DigestUtils;

public class HistoryConnet {

    private String selector;
    private String ip;
    private String port;
    private String password;
    private String username;
    private String database;
    private String md5Str;

    public String getMd5Str() {
        return md5Str;
    }

    public void setMd5Str(String md5Str) {
        this.md5Str = md5Str;
    }

    public HistoryConnet() {
    }

    public HistoryConnet(String selector, String ip, String port, String password, String username, String database) {
        this.selector = selector;
        this.ip = ip;
        this.port = port;
        this.password = password;
        this.username = username;
        this.database = database;
    }


    public String MakeMd5Str() {
        //加密后的字符串
        String str = selector + ip +  port+ password+ username+database;
        String encodeStr= DigestUtils.md5DigestAsHex(str.getBytes());
        return encodeStr;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
