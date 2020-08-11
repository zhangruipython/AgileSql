package com.agile.json.serviceimpl;

import com.agile.json.pojo.Metadata;
import com.agile.json.service.JsonDbHandle;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @program: AgileSql
 * @description: 处理json数据
 * @author: 张睿
 * @create: 2020-08-07 16:59
 **/
public class JsonDbHandleImpl implements JsonDbHandle,Runnable {
    private final PreparedStatement insertPrep;
    private final CountDownLatch countDownLatch;


    public JsonDbHandleImpl(PreparedStatement insertPrep, CountDownLatch countDownLatch, MetadataHandleImpl metadataHandleImpl) {
        this.insertPrep = insertPrep;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void JsonDb(String data) {
        JSONObject dataJson = JSON.parseObject(data);
        JSONArray metadataJson = (JSONArray) dataJson.get("metadata");
        JSONArray dataInfoJson = (JSONArray) dataJson.get("data");
        String tableName = (String) dataJson.get("tableName");
        List<Metadata> metadataList = new ArrayList<>();
        for (Object o:metadataJson){
            JSONObject var = (JSONObject) o;
            Metadata metadata =JSON.parseObject(var.toJSONString(),new TypeReference<Metadata>(){});
            metadataList.add(metadata);
        }
        MetadataHandleImpl metadataHandle = new MetadataHandleImpl.MetadataHandleBuild().setMetadataList(metadataList).setTableName(tableName).build();
        List<String> allSql = metadataHandle.generateSql();


    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
    }
}
