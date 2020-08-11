package com.agile.json.pojo;

/**
 * 链式编程
 * @author 张睿
 * @create 2020-08-07 16:09
 **/
public class Metadata {
    private final String fieldName;
    private final String type;
    private final String mapping;

    public Metadata(String fieldName, String type, String mapping) {
        this.fieldName = fieldName;
        this.type = type;
        this.mapping = mapping;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getType() {
        return type;
    }

    public String getMapping() {
        return mapping;
    }
}

