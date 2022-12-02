package com.trh.dictionary.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字段级信息，对应excel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ContentRowHeight(25)
@ColumnWidth(25)
@Accessors(chain = true)
public class ColumnInfoDto {
    @ExcelProperty("*模式名(Schema)")
    private String dbName;

    @ExcelProperty("*表英文名")
    private String tableName;

    @ExcelProperty("*字段序号")
    private String order;

    @ExcelProperty("*字段英文名")
    private String ColumnName;

    @ExcelProperty("*字段中文名")
    private String ColumnComment;

    @ExcelProperty("*字段数据类型")
    private String dataType;

    @ExcelProperty("*是否主键")
    private String isIndex;

    @ExcelProperty("*是否允许空值")
    private String ColumnISNull;

    @ExcelProperty("*是否代码字段")
    private String ColumnIsEnum;

    @ExcelProperty("原始字段中文名")
    private String oldColumnName;
}
