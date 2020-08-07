package com.agile.json.serviceimpl;

import com.agile.json.pojo.Metadata;
import com.alibaba.fastjson.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/** 处理 metadata 生成建表语句和数据插入语句
 * @author 张睿
 * @create 2020-08-07 16:18
 **/
public class MetadataHandleImpl {
    private final List<Metadata> metadataList;
    private final String tableName;

    public MetadataHandleImpl(List<Metadata> metadataList,String tableName) {
        requireNonNull(metadataList,"metadataList is null");
        requireNonNull(tableName,"table name is null");
        this.metadataList = metadataList;
        this.tableName = tableName;
    }

    public List<String> generateSql(){
        StringBuilder initializeCreateSql = new StringBuilder("create table " + tableName + " (");
        StringBuilder initializeInsertSql = new StringBuilder("insert into "+tableName+" values (");
        for(Metadata metadata:metadataList){
            String mapName = metadata.getMapping();
            String fieldType = metadata.getType();
            initializeCreateSql.append(mapName).append(" ").append(fieldType).append(",");
            initializeInsertSql.append("?,");
        }
        initializeCreateSql.deleteCharAt(initializeCreateSql.length()-1).append(")");
        initializeInsertSql.deleteCharAt(initializeInsertSql.length()-1).append(")");
        return Arrays.asList(initializeCreateSql.toString(),initializeInsertSql.toString());
    }

    public void prepareInsertSql(PreparedStatement preparedStatement, JSONObject insertData) throws SQLException {
        int i = 1;
        for (Metadata metadata:metadataList){
            String name = metadata.getName();
            String fieldType = metadata.getType();
            switch (fieldType){
                case "varchar":
                    preparedStatement.setString(i,insertData.getString(name));
                    break;
                case "int":
                    preparedStatement.setInt(i,insertData.getInteger(name));
                    break;
                case "boolean":
                    preparedStatement.setBoolean(i,insertData.getBoolean(name));
                    break;
                case "double":
                    preparedStatement.setDouble(i,insertData.getDouble(name));
                    break;
                case "float":
                    preparedStatement.setFloat(i,insertData.getFloat(name));
                    break;
                default:
                    preparedStatement.setObject(i,insertData.getFloat(name));
            }
            i++;
        }
    }
}

