package com.zuoye.binlog.mysql;

import lombok.Data;

/**
 * 字段属性对象
 *
 * @author zrj
 * @since 2021/7/27
 **/
@Data
public class Colum {
    public int inx;
    public String colName; // 列名
    public String dataType; // 类型
    public String schema; // 数据库
    public String table; // 表

    public Colum(String schema, String table, int idx, String colName, String dataType) {
        this.schema = schema;
        this.table = table;
        this.colName = colName;
        this.dataType = dataType;
        this.inx = idx;
    }
}
