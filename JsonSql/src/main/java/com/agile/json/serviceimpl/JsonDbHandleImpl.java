package com.agile.json.serviceimpl;

import com.agile.json.service.JsonDbHandle;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.PreparedStatement;
import java.util.concurrent.CountDownLatch;

/**
 * @program: AgileSql
 * @description:
 * @author: 张睿
 * @create: 2020-08-07 16:59
 **/
public class JsonDbHandleImpl implements JsonDbHandle,Runnable {
    private final PreparedStatement insertPrep;
    private final CountDownLatch countDownLatch;
    private final MetadataHandleImpl metadataHandleImpl;
    private final JSONArray jsonDataArray;

    public JsonDbHandleImpl(PreparedStatement insertPrep, CountDownLatch countDownLatch, MetadataHandleImpl metadataHandleImpl, JSONArray jsonDataArray) {
        this.insertPrep = insertPrep;
        this.countDownLatch = countDownLatch;
        this.metadataHandleImpl = metadataHandleImpl;
        this.jsonDataArray = jsonDataArray;
    }

    @Override
    public void JsonDb() {
        for (Object data:jsonDataArray){

        }
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
