package com.agile;

import static org.junit.Assert.assertTrue;

import com.agile.json.pojo.Metadata;
import com.agile.json.service.JsonDbHandle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
        String data = "";
//        String data = "{'data':[{'fieldName':'name','type':'varchar', 'mapping': 'name'},{'fieldName':'name','type':'varchar', 'mapping': 'name'}]}";
        JSONArray jsonData = (JSONArray) JSON.parseObject(data).get("data");
        List<Metadata> metadataList = new ArrayList<>();
        for (Object a:jsonData){
            JSONObject var = (JSONObject) a;
            Metadata var01 = JSON.parseObject(var.toJSONString(),new TypeReference<Metadata>(){});
            metadataList.add(var01);
            System.out.println(var01.getFieldName());
        }

//        Metadata m = JSON.parseObject(jsonData.get("data").toString(),new TypeReference<Metadata>(){});
//        System.out.println(m.getName());
    }
}
