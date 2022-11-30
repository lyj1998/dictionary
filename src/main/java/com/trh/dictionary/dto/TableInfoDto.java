package com.trh.dictionary.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 表级信息，对应excel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ContentRowHeight(25)
@HeadRowHeight(25)
@ColumnWidth(25)
@Accessors(chain = true)
public class TableInfoDto{
    @ExcelProperty("*模式名(Schema)")
    private String dbName;

    @ExcelProperty("*表英文名")
    private String tableName;

    @ExcelProperty("*表中文名")
    private String tableComment;

    @ExcelProperty("*是否存在主键")
    private String tablePri;

}
