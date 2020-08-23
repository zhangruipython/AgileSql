package localfile.sql;

/**
 * @program: AgileSql
 * @description: 执行以本地文件为数据源的结构化SQL语句
 * @author: 张睿
 * @create: 2020-08-18 15:52
 **/
public class ExecuteSql {
    public String OperateLocalFile(String localFilePath) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        String  queryData = null;

        return null;
    }
}
