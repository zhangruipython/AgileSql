package com.agile.parquet.serviceImpl;


import com.agile.parquet.pojo.ParquetData;
import com.agile.parquet.service.ParquetDBHandle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ColumnDescriptor;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.MessageColumnIO;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: AgileSql
 * @description: 处理parquet数据
 * @author: 张睿
 * @create: 2020-08-24 15:44
 **/
public class ParquetDBHandleImpl implements ParquetDBHandle {
    private final Connection connection;
    public ParquetDBHandleImpl(Connection connection) {
        this.connection = connection;
    }
    /**
    *  读取parquet数据，将数据格式转为[["DocID","Links_Backward","Links_Forward","Language_code"],["int32","int32","int32","int32"],[[[1,1],[2,1],[2,1],[2,1]],[[1,1],[2,1],[2,1],[2,1]]]
     *  二维数组，第一条数组是字段（字段格式为group类型字段和基础类型字段拼接），第二条数组为字段类型，后续数组集合是具体数据
     *  默认以parquet Message name 作为表名
    * @author 张睿
    * @date 2020-08-24
    * @param filePath
    * @return java.util.List<java.lang.String>
    */

    public ParquetData readParquetFile(String filePath) throws IOException {
        ParquetFileReader reader = ParquetFileReader.open(HadoopInputFile.fromPath(new Path(filePath),new Configuration()));
        MessageType schema = reader.getFooter().getFileMetaData().getSchema();
        String schemaName = schema.getName();
        // 查看数据模式 根据元数据结构得到建表语句
        List<List<String>> allColumns = new ArrayList<>();
        for (ColumnDescriptor column:schema.getColumns()){
            List<String> columnList = new ArrayList<>();
            System.out.println(column.getPrimitiveType());
            String[] columnPath = column.getPath();
            System.out.println(Arrays.toString(columnPath));
            if (columnPath.length>1){
                String groupColumn = "repeated"+" group "+columnPath[0];
                columnList.add(groupColumn);
            }
            columnList.add(column.getPrimitiveType().toString());
            allColumns.add(columnList);
        }


        /** 根据列字段解析出对应的数据
         *  1、确定字段类型为 repeated/optional 还是 required
         *  2、如果字段类型为 repeated/optional，则获取字段值数量
         *  3、根据字段值数量遍历，将值添加至list中
         * @author 张睿
         * @date 2020-09-02
         * @param [parquetFilePath]
         * @return void
         */

        List<List<Object[]>> parquetData = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();
        for (List<String> a:allColumns){
            if (a.size()==1){
                columnNames.add(a.get(0).split(" ")[2]);
                columnTypes.add(a.get(0).split(" ")[1]);
            }
            else if (a.size()==2){
                columnNames.add(a.get(0).split(" ")[2]+"_"+a.get(1).split(" ")[2]);
                columnTypes.add(a.get(1).split(" ")[1]);
            }
        }


        PageReadStore pageReadStore;
        while ((pageReadStore = reader.readNextRowGroup())!=null){
            // 数据行数
            long rows = pageReadStore.getRowCount();
            MessageColumnIO columnIO = new ColumnIOFactory().getColumnIO(schema);
            RecordReader<Group> recordReader = columnIO.getRecordReader(pageReadStore,new GroupRecordConverter(schema));
            // 遍历总数据行
            for (int i=0;i<rows;i++){
                SimpleGroup simpleGroup = (SimpleGroup) recordReader.read();
                // 拿到一行数据中每一个字段对应的值
                List<Object[]> columnNum = new ArrayList<>();
                for (List<String> columnMesList:allColumns){
                    Object[] data = null;
                    // 判断是否为group
                    if (columnMesList.size()==1){
                        String[] columnMes = columnMesList.get(0).split(" ");
                        String columnProperty = columnMes[0];
                        String columnType = columnMes[1];
                        String columnName = columnMes[2];
                        int columnCount = simpleGroup.getFieldRepetitionCount(columnName);
                        data = new Object[columnCount];
                        switch (columnType){
                            case "int32":
                                for (int j=0;j<columnCount;j++){

                                    data[j] = simpleGroup.getInteger(columnName,j);
                                }
                                break;
                            case "int64":
                                for (int j=0;j<columnCount;j++){
                                    data[j] = simpleGroup.getLong(columnName,j);
                                }
                                break;
                            case "binary":
                                for (int j=0;j<columnCount;j++){
                                    data[j] = simpleGroup.getString(columnName,j);
                                }
                                break;
                        }
                    }
                    else if (columnMesList.size()==2){
                        String[] groupColumnMes = columnMesList.get(0).split(" ");
                        String groupColumnProperty = groupColumnMes[0];
                        String groupColumnType = groupColumnMes[1];
                        String groupColumnName = groupColumnMes[2];

                        String[] primitivesColumnMes = columnMesList.get(1).split(" ");
                        String primitivesColumnProperty = primitivesColumnMes[0];
                        String primitivesColumnType = primitivesColumnMes[1];
                        String primitivesColumnName = primitivesColumnMes[2];

                        int columnCount = simpleGroup.getGroup(groupColumnName,0).getFieldRepetitionCount(primitivesColumnName);
                        data = new Object[columnCount];
                        switch (primitivesColumnType){
                            case "int32":
                                for (int j=0;j<columnCount;j++){
                                    data[j] = simpleGroup.getGroup(groupColumnName,0).getInteger(primitivesColumnName,j);
                                }
                                break;
                            case "int64":
                                for (int j=0;j<columnCount;j++){
                                    data[j] = simpleGroup.getGroup(groupColumnName,0).getLong(primitivesColumnName,j);
                                }
                                break;
                            case "binary":
                                for (int j=0;j<columnCount;j++){
                                    data[j] = simpleGroup.getGroup(groupColumnName,0).getString(primitivesColumnName,j);
                                }
                                break;
                        }
                    }
                    columnNum.add(data);
                }
                parquetData.add(columnNum);

            }
        }

        reader.close();
        ParquetData parquetData1 = new ParquetData();
        parquetData1.setAllColumnData(parquetData);
        parquetData1.setColumnNames(columnNames);
        parquetData1.setColumnTypes(columnTypes);
        parquetData1.setParquetName(schemaName);
        return parquetData1;
    }
    /**
    *  生成建表语句和预编译插入语句
    * @author 张睿
    * @date 2020-09-04
    * @param [tableName, columnNames, columnTypes]
    * @return java.util.List<java.lang.String>
    */

    public List<String> generateSql(String tableName,List<String> columnNames,List<String> columnTypes){
        StringBuilder initializeCreateSql = new StringBuilder("create table " + tableName + " (");
        StringBuilder initializeInsertSql = new StringBuilder("insert into "+tableName+" values (");
        for (int i =0;i<columnNames.size();i++){
            String fieldName = columnNames.get(i);
            String fieldType = columnTypes.get(i);
            initializeCreateSql.append(fieldName).append(" ").append(fieldType).append(",");
            initializeInsertSql.append("?,");
        }
        initializeCreateSql.deleteCharAt(initializeCreateSql.length()-1).append(")");
        initializeInsertSql.deleteCharAt(initializeInsertSql.length()-1).append(")");
        return Arrays.asList(initializeCreateSql.toString(),initializeInsertSql.toString());
    }
    /**
    *  执行具体sql
    * @author 张睿
    * @date 2020-09-04
    * @param parameter
     * {
     *     "tableName": "demo01",
     *     "sqlContent": "select count(*) from demo01;",
     *     "parquetFile":"D:/data/data.txt"
     * }
    * @return java.lang.String
    */

    @Override
    public String ParquetDb(String parquetParameter) {
        JSONObject dataJson = JSON.parseObject(parquetParameter);

        return null;
    }
}
