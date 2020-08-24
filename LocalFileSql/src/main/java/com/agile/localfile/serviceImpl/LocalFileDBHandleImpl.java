package com.agile.localfile.serviceImpl;

import com.agile.localfile.pojo.Metadata;
import com.agile.localfile.service.LocalFileDBHandle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: AgileSql
 * @description: 处理本地文件数据
 * @author: 张睿
 * @create: 2020-08-24 15:44
 **/
public class LocalFileDBHandleImpl implements LocalFileDBHandle {
    private final Connection connection;

    public LocalFileDBHandleImpl(Connection connection) {
        this.connection = connection;
    }
    /**
    *  读取本地文件数据，本地文件默认按"  "进行字段分隔
    * @author 张睿
    * @date 2020-08-24
    * @param filePath
    * @return java.util.List<java.lang.String>
    */

    private List<String[]> readLocalFile(String filePath){
        Path path = Paths.get(filePath);
        List<String[]> data = null;
        if (Files.isReadable(path)){
            try(Stream<String>lineStream = Files.lines(path,StandardCharsets.UTF_8);) {
                data = lineStream.map(s->s.split("  ")).collect(Collectors.toList());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    @Override
    public String LocalFileDb(String parameter) {
        String queryData = null;
        JSONObject dataJson = JSON.parseObject(parameter);
        String tableName = (String) dataJson.get("tableName");
        String sqlContent = (String) dataJson.get("sqlContent");
        String localFilePath = (String) dataJson.get("localFilePath");
        List<String[]> fileData = readLocalFile(localFilePath);

        JSONArray metadataJson = (JSONArray) dataJson.get("metadata");
        List<Metadata> metadataList = new ArrayList<>();
        for (Object o:metadataJson){
            JSONObject var = (JSONObject) o;
            Metadata metadata =JSON.parseObject(var.toJSONString(),new TypeReference<Metadata>(){});
            metadataList.add(metadata);
        }
        MetadataHandleImpl metadataHandle = new MetadataHandleImpl.MetadataHandleBuild().setMetadataList(metadataList).setTableName(tableName).build();
        List<String> allSql = metadataHandle.generateSql();

        try (Statement sqlStatement = connection.createStatement();){
            // 执行建表语句
            sqlStatement.executeUpdate(allSql.get(0));
            // 插入数据
            PreparedStatement insertPreparedStatement =connection.prepareStatement(allSql.get(1));
            int i =1;
            for (String[] data:fileData){
                metadataHandle.prepareInsertSql(insertPreparedStatement,data);
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
