package com.agile;

import com.agile.localfile.sql.ExecuteSql;
import org.junit.Test;

import java.io.IOException;

/**
 * @program: AgileSql
 * @description: 测试json结构化计算
 * @author: 张睿
 * @create: 2020-08-12 11:26
 **/
public class LocalFileDataTest {
    @Test
    public void shouldAnswerWithTrue() throws IOException, ClassNotFoundException {
        String var01 = "{\n" +
                "    \"tableName\": \"demo01\",\n" +
                "    \"sqlContent\": \"select count(*) from demo01;\",\n" +
                "    \"localFilePath\":\"D:/data/data.txt\",\n" +
                "    \"metadata\": [\n" +
                "        {\n" +
                "            \"fieldName\": \"name\",\n" +
                "            \"type\": \"varchar\",\n" +
                "            \"mapping\": \"name\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"fieldName\": \"id\",\n" +
                "            \"type\": \"int\",\n" +
                "            \"mapping\": \"id\"\n" +
                "        }\n" +
                "    ],\n" +
                "}";
        System.out.println(ExecuteSql.operateLocalFile(var01));
    }
}
