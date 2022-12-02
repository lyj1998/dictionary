package com.trh.dictionary.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.slf4j.LoggerFactory;

/**
 * 字段代码信息，对应excel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ContentRowHeight(25)
@ColumnWidth(25)
@Accessors(chain = true)
public class EnumCodeInfoDto implements Cloneable{
    @ExcelProperty("*模式名(Schema)")
    private String dbName;

    @ExcelProperty("*表英文名")
    private String tableName;

    @ExcelProperty("*字段英文名")
    private String ColumnName;

    @ExcelProperty("*代码取值")
    private String code;

    @ExcelProperty("*代码注释")
    private String codeDes;

    @Override
    public Object clone() {
        EnumCodeInfoDto dto = null;
        try{
            dto = (EnumCodeInfoDto)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
