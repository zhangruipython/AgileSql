# AgileSql for All Data

## (解析各类数据为结构化数据，使之能够执行SQL语句)

----

[TOC]

### Requirements

----

- Java 8 Update 151 or higher (8u151+), 64-bit. Both Oracle JDK and OpenJDK are supported. 

- Maven 3.3.9+ (for building) 

### Building AgileSql

---

AgileSql是一个maven项目，执行以下命令将AgileSql添加至您maven项目依赖中

- git clone项目，进入项目主目录，执行以下命令生成jar包：

  ```
  mvn clean compile assembly:single
  ```

- 添加jar包至你的maven项目依赖中：

  ```
  mvn install:install-file -Dfile=AgileSql.jar -DgroupId=com.zrui -DartifactId=AgileSql -Dversion=1.0 -Dpackaging=jar
  
  参数说明
  -Dfile：jar所在绝对路径
  其余按照默认配置即可
  ```

- 配置idea中pom.xml文件

  ```
  在pom.xml文件中添加依赖如下：
      <dependency>
        <groupId>com.zrui</groupId>
        <artifactId>AgileSql</artifactId>
        <version>1.0</version>
      </dependency>
  ```

  

### Using AgileSql in your IDE

----

#### JSON

支持接受json文件路径和json字符串，进行结构化数据解析接口。

**json 内容需要按照以下格式**：

```
{
    "tableName": "demo01",
    "metadata": [
        {
            "fieldName": "name",
            "type": "varchar",
            "mapping": "name"
        },
        {
            "fieldName": "id",
            "type": "int",
            "mapping": "id"
        }
    ],
    "data": [
        {
            "name": "jack",
            "id": 1
        },
        {
            "name": "jack",
            "id": 2
        }
    ],
    "sqlContent": "select count(*) from demo01;"
}
```

- tableName ：表明执行sql表名称
- metadata： json数据元数据
- data：具体json内容
- sqlContent：执行sql语句

**接口案例 ** <https://github.com/zhangruipython/AgileSql/blob/master/JsonSql/src/test/java/com/agile/json/JsonDataTest.java> 





