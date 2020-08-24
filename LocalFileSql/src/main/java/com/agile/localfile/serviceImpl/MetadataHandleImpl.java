package com.agile.localfile.serviceImpl;

import com.agile.localfile.pojo.Metadata;
import com.alibaba.fastjson.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


/** 处理 metadata 生成建表语句和数据插入语句
 * @author 张睿
 * @create 2020-08-07 16:18
 **/
public class MetadataHandleImpl {
    private final List<Metadata> metadataList;
    private final String tableName;
    private MetadataHandleImpl(MetadataHandleBuild metadataHandleBuild){
        this.metadataList = metadataHandleBuild.metadataList;
        this.tableName=metadataHandleBuild.tableName;
    }
    public static class MetadataHandleBuild{
        private List<Metadata> metadataList;
        private String tableName;
        public MetadataHandleBuild setMetadataList(List<Metadata> metadataList){this.metadataList=metadataList;return this;}
        public MetadataHandleBuild setTableName(String tableName){this.tableName=tableName;return this;}
        public MetadataHandleImpl build(){return new MetadataHandleImpl(this);}
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

    public void prepareInsertSql(PreparedStatement preparedStatement, String[] data) throws SQLException {
        int i = 1;
        for (Metadata metadata:metadataList){
            String fieldType = metadata.getType();
            switch (fieldType){
                case "varchar":
                    preparedStatement.setString(i,data[i-1]);
                    break;
                case "int":
                    preparedStatement.setInt(i,Integer.parseInt(data[i-1]));
                    break;
                case "boolean":
                    preparedStatement.setBoolean(i,Boolean.parseBoolean(data[i-1]));
                    break;
                case "double":
                    preparedStatement.setDouble(i,Double.parseDouble(data[i-1]));
                    break;
                case "float":
                    preparedStatement.setFloat(i,Float.parseFloat(data[i-1]));
                    break;
                default:
                    preparedStatement.setObject(i,data[i-1]);
            }
            i++;
        }
    }
}

