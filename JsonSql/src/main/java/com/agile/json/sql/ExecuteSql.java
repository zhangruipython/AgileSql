package com.agile.json.sql;


import com.agile.json.serviceimpl.JsonDbHandleImpl;
import com.agile.json.util.CreateDataConn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.stream.Stream;
import static com.google.common.base.Preconditions.checkArgument;

import static java.util.Objects.requireNonNull;

/**
 * @program: AgileSql
 * @description: json 结构化解析入口
 * @author: 张睿
 * @create: 2020-08-12 10:05
 **/
public class ExecuteSql {
    /**
    *
    * @author 张睿
    * @date 2020-08-12
    * @param jsonParameter json字符串
     * <pre>
     *     jsonParameter= {
     *     "tableName": "demo01",
     *     "metadata": [
     *         {
     *             "fieldName": "name",
     *             "type": "varchar",
     *             "mapping": "name"
     *         },
     *         {
     *             "fieldName": "id",
     *             "type": "int",
     *             "mapping": "id"
     *         }
     *     ],
     *     "data": [
     *         {
     *             "name": "jack",
     *             "id": 1
     *         },
     *         {
     *             "name": "jack",
     *             "id": 2
     *         }
     *     ],
     *     "sqlContent": "select count(*) from demo01;"
     * }
     * </pre>
    * @return java.lang.String
    */

    public static String operateJsonStr(String jsonParameter) throws ClassNotFoundException {
        requireNonNull(jsonParameter,"json str is null");
        Class.forName("org.sqlite.JDBC");
        String queryData = null;
        try (Connection connection = new CreateDataConn().createConn();){
            connection.setAutoCommit(false);
            JsonDbHandleImpl jsonDbHandle = new JsonDbHandleImpl(connection);
            queryData = jsonDbHandle.JsonDb(jsonParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryData==null?"no data":queryData;
    }
    /**
    *
    * @author 张睿
    * @date 2020-08-12
    * @param jsonPath, more
     *<pre>
     * jsonPath = "data-1.json"
     *</pre>
    * @return java.lang.String
    */

    public static String operateJsonPath(String jsonPath, String... more) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String queryData = null;
        requireNonNull(jsonPath,"json path is null");
        Path path = Paths.get(jsonPath);
        checkArgument(Files.isReadable(path),"json path is unreadable");

        try (Connection connection = new CreateDataConn().createConn();){
            Stream<String> lineStream = Files.lines(path,StandardCharsets.UTF_8);
            String jsonParameter = lineStream.reduce("",(s1,s2)->s1+s2);
            connection.setAutoCommit(false);
            JsonDbHandleImpl jsonDbHandle = new JsonDbHandleImpl(connection);
            queryData = jsonDbHandle.JsonDb(jsonParameter);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return queryData==null?"no data":queryData;
    }
}
