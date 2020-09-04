package com.agile.parquet.pojo;

import java.util.List;

/**
 * @program: AgileSql
 * @description:
 * @author: 张睿
 * @create: 2020-09-04 14:08
 **/
public class ParquetData {
    private String parquetName;
    private List<String> columnNames;
    private List<String> columnTypes;
    private List<List<Object[]>> allColumnData;

    public String getParquetName() {
        return parquetName;
    }

    public void setParquetName(String parquetName) {
        this.parquetName = parquetName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(List<String> columnTypes) {
        this.columnTypes = columnTypes;
    }

    public List<List<Object[]>> getAllColumnData() {
        return allColumnData;
    }

    public void setAllColumnData(List<List<Object[]>> allColumnData) {
        this.allColumnData = allColumnData;
    }
}
