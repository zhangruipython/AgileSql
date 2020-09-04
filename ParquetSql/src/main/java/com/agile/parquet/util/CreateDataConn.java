package com.agile.parquet.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @program: AgileSql
 * @description: 创建连接池
 * @author: 张睿
 * @create: 2020-08-12 10:36
 **/
public class CreateDataConn {
    private final DataSource dataSource;

    public CreateDataConn() throws Exception {
        Properties properties = new Properties();
        properties.put("driverClassName","org.sqlite.JDBC");
        properties.put("url","jdbc:sqlite::memory:");
        properties.put("type","com.alibaba.druid.pool.DruidDataSource");
        properties.put("initialSize","5");
        properties.put("maxActive","10");
        properties.put("maxWait","300");
        this.dataSource = DruidDataSourceFactory.createDataSource(properties);
    }
    public Connection createConn() throws SQLException {
        return dataSource.getConnection();
    }
}
