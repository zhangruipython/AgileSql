package com.agile.json.service;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: AgileSql
 * @description: ResultSet处理
 * @author: 张睿
 * @create: 2020-08-12 10:52
 **/
public interface ResultSetHandle<T> {
    T handle (ResultSet resultSet) throws SQLException;
}
