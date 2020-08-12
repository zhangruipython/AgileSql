package com.agile.json;

import com.agile.json.sql.ExecuteSql;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @program: AgileSql
 * @description: 测试json结构化计算
 * @author: 张睿
 * @create: 2020-08-12 11:26
 **/
public class JsonDataTest {
    @Test
    public void shouldAnswerWithTrue() throws IOException, ClassNotFoundException {
        String path = "C:\\Users\\张睿\\Desktop\\data-1.json";
        System.out.println(ExecuteSql.operateJsonPath(path));
    }
}
