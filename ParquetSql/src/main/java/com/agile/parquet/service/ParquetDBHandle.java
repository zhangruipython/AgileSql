package com.agile.parquet.service;

import java.io.IOException;

/**
 * @program: AgileSql
 * @description: 处理parquet中数据
 * @author: 张睿
 * @create: 2020-08-24 15:41
 **/
public interface ParquetDBHandle {
    public abstract String ParquetDb(String parameter) throws IOException;
}
