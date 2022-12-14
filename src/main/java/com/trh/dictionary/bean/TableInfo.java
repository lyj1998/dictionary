package com.trh.dictionary.bean;

import java.util.List;

/**
 * 表信息
 *
 * @author
 * @create 2022-11-28 14:17
 */
public class TableInfo {
    /**
     * 表名
     */
    private String tableName= "";

    /**
     * 表注释
     */
    private String tableComment = "";

    public String getTablePri() {
        return tablePri;
    }

    public void setTablePri(String tablePri) {
        this.tablePri = tablePri;
    }

    /**
     * 是否含有主键
     */
    private String tablePri = "";

    /**
     * 字符集
     */
    private String orderType= "";
    /**
     * 存储引擎
     */
    private String storageEngine= "";
    /**
     * 描述
     */
    private String description= "";
    /**
     * 所有列名
     */
    private List<ColumnInfo> columnList;

    private List<IndexInfo> indexInfoList;

    public List<IndexInfo> getIndexInfoList() {
        return indexInfoList;
    }

    public void setIndexInfoList(List<IndexInfo> indexInfoList) {
        this.indexInfoList = indexInfoList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        if(tableName==null){
            tableName = "";
        }
        this.tableName = tableName;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        if(tableComment==null){
            tableComment = "";
        }
        this.tableComment = tableComment;
    }

    public void setOrderType(String orderType) {
        if(orderType==null){
            orderType = "";
        }
        this.orderType = orderType;
    }

    public String getStorageEngine() {
        return storageEngine;
    }

    public void setStorageEngine(String storageEngine) {
        if(storageEngine==null){
            storageEngine = "";
        }
        this.storageEngine = storageEngine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description==null){
            description = "";
        }
        this.description = description;
    }

    public List<ColumnInfo> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnInfo> columnList) {
        this.columnList = columnList;
    }
    
}
