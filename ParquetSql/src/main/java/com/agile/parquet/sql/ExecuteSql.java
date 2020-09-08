package com.agile.parquet.sql;

import com.agile.parquet.serviceImpl.ParquetDBHandleImpl;
import com.agile.parquet.util.CreateDataConn;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: AgileSql
 * @description: sql执行入口
 * @author: 张睿
 * @create: 2020-09-08 15:10
 **/
public class ExecuteSql {
    private final String parquetFilePath;
    private final String tempTableName;
    private final String sqlContent;

    private ExecuteSql(ExecuteSqlConf executeSqlConf) {
        this.parquetFilePath = executeSqlConf.parquetFilePath;
        this.tempTableName = executeSqlConf.tempTableName;
        this.sqlContent = executeSqlConf.sqlContent;
    }

    public static class ExecuteSqlConf{
        private  String parquetFilePath;
        private  String tempTableName;
        private  String sqlContent;

        public ExecuteSqlConf setParquetFilePath(String parquetFilePath) {
            this.parquetFilePath = parquetFilePath;
            return this;
        }

        public ExecuteSqlConf setTempTableName(String tempTableName) {
            this.tempTableName = tempTableName;
            return this;
        }

        public ExecuteSqlConf setSqlContent(String sqlContent) {
            this.sqlContent = sqlContent;
            return this;
        }
        public ExecuteSql build(){return new ExecuteSql(this);}
    }
    public String execute(){
        String queryData = null;
        try (Connection connection = new CreateDataConn().createConn();){
            Class.forName("org.sqlite.JDBC");
            connection.setAutoCommit(false);
            ParquetDBHandleImpl parquetDBHandle = new ParquetDBHandleImpl(connection);
            queryData = parquetDBHandle.ParquetDb(parquetFilePath,tempTableName,sqlContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryData==null?"no data":queryData;
    }
    public void show(String data){
        JSONArray var = (JSONArray) JSONArray.parse(data);
        if (var.size()>0){
            JSONObject var01 =(JSONObject) var.get(0);
            System.out.print("|");
            for (Map.Entry<String,Object> entry:var01.entrySet()){
                System.out.print(entry.getKey()+"|");
            }
            System.out.println("\n");
            for (Object var02:var){
                System.out.print("|");
                JSONObject var03 = (JSONObject) var02;
                for (Map.Entry<String,Object> entry:var03.entrySet()){
                    System.out.print(entry.getValue()+"|");
                }
                System.out.println("\n");
            }
        }


    }

    public static void main(String[] args) {
        ExecuteSql executeSql = new ExecuteSqlConf().setSqlContent("select * from demo01;")
                .setParquetFilePath("D:/data/demo.parquet").setTempTableName("demo01").build();
        String data = executeSql.execute();
        executeSql.show(data);
    }
}
