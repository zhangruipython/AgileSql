package com.agile.localfile.sql;
import com.agile.localfile.serviceImpl.LocalFileDBHandleImpl;
import com.agile.localfile.util.CreateDataConn;

import java.sql.Connection;

import static java.util.Objects.requireNonNull;
/**
 * @program: AgileSql
 * @description:
 * @author: 张睿
 * @create: 2020-08-24 16:47
 **/
public class ExecuteSql {
    /**
    *
    * @author 张睿
    * @date 2020-08-24
    * @param [parameter] json字符串
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
     *     "sqlContent": "select count(*) from demo01;",
     *     "localFilePath":"data.txt"
     *
     * }
     * </pre>
    * @return java.lang.String
    */

    public static String operateLocalFile(String parameter) throws ClassNotFoundException {
        requireNonNull(parameter,"local file json parameter is null");
        Class.forName("org.sqlite.JDBC");
        String queryData = null;
        try (Connection connection = new CreateDataConn().createConn();){
            connection.setAutoCommit(false);
            LocalFileDBHandleImpl localFileDbHandle = new LocalFileDBHandleImpl(connection);
            queryData = localFileDbHandle.LocalFileDb(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryData==null?"no data":queryData;
    }

}
