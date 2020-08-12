package com.agile.json.serviceimpl;

import com.agile.json.pojo.Metadata;
import com.agile.json.service.JsonDbHandle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @program: AgileSql
 * @description: 处理json数据
 * @author: 张睿
 * @create: 2020-08-07 16:59
 **/
public class JsonDbHandleImpl implements JsonDbHandle {
    private final Connection conn;

    public JsonDbHandleImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public String JsonDb(String data) {
        String queryData = null;
        JSONObject dataJson = JSON.parseObject(data);
        JSONArray metadataJson = (JSONArray) dataJson.get("metadata");
        JSONArray dataInfoJson = (JSONArray) dataJson.get("data");
        String tableName = (String) dataJson.get("tableName");
        String sqlContent = (String) dataJson.get("sqlContent");

        List<Metadata> metadataList = new ArrayList<>();
        for (Object o:metadataJson){
            JSONObject var = (JSONObject) o;
            Metadata metadata =JSON.parseObject(var.toJSONString(),new TypeReference<Metadata>(){});
            metadataList.add(metadata);
        }
        MetadataHandleImpl metadataHandle = new MetadataHandleImpl.MetadataHandleBuild().setMetadataList(metadataList).setTableName(tableName).build();
        List<String> allSql = metadataHandle.generateSql();
        try (Statement sqlStatement = conn.createStatement();){
            // 执行建表语句
            sqlStatement.executeUpdate(allSql.get(0));
            // 插入数据
            PreparedStatement insertPreparedStatement =conn.prepareStatement(allSql.get(1));
            int i =1;
           for (Object dataObj:dataInfoJson){
               JSONObject dataJsonObject = (JSONObject) dataObj;
               metadataHandle.prepareInsertSql(insertPreparedStatement,dataJsonObject);
               insertPreparedStatement.addBatch();
               if (i % 1000 == 0) {
                   insertPreparedStatement.executeBatch();
                   insertPreparedStatement.clearBatch();
               }
               i++;
           }
            insertPreparedStatement.executeBatch();
           ResultSet querySet = sqlStatement.executeQuery(sqlContent);
           ResultSetHandleImpl resultSetHandle = new ResultSetHandleImpl();
           queryData = resultSetHandle.handle(querySet);
            insertPreparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryData;
    }
}
