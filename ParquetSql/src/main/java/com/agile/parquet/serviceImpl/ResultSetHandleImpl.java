package com.agile.parquet.serviceImpl;




import com.agile.parquet.service.ResultSetHandle;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @program: AgileSql
 * @description: ResultSet处理为json字符串
 * @author: 张睿
 * @create: 2020-08-12 10:54
 **/
public class ResultSetHandleImpl implements ResultSetHandle {

    @Override
    public String handle(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()){
            JSONObject jsonObject = new JSONObject();
            handleJson(resultSet,resultSetMetaData,jsonObject);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toJSONString();
    }
    private void handleJson(ResultSet resultSet, ResultSetMetaData resultSetMetaData, JSONObject jsonObject){
        try {
            int columnNum = resultSetMetaData.getColumnCount();
            for (int i=0;i<columnNum;i++){
                String columnName = resultSetMetaData.getColumnName(i+1);
                switch (resultSetMetaData.getColumnType(i+1)){
                    case Types.ARRAY:
                        jsonObject.put(columnName,resultSet.getArray(columnName));
                        break;
                    case Types.BOOLEAN:
                        jsonObject.put(columnName, resultSet.getBoolean(columnName));
                        break;
                    case Types.DOUBLE:
                        jsonObject.put(columnName, resultSet.getDouble(columnName));
                        break;
                    case Types.FLOAT:
                        jsonObject.put(columnName, resultSet.getFloat(columnName));
                        break;
                    case Types.INTEGER:
                        jsonObject.put(columnName, resultSet.getInt(columnName));
                    case Types.VARCHAR:
                        jsonObject.put(columnName, resultSet.getString(columnName));
                        break;
                    case Types.DATE:
                        jsonObject.put(columnName, resultSet.getDate(columnName));
                        break;
                    case Types.TIMESTAMP:
                        jsonObject.put(columnName, resultSet.getTimestamp(columnName));
                        break;
                    default:
                        jsonObject.put(columnName,resultSet.getObject(columnName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
