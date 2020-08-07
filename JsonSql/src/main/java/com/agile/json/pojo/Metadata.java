package com.agile.json.pojo;

/**
 * 链式编程
 * @author 张睿
 * @create 2020-08-07 16:09
 **/
public class Metadata {
    private final String name;
    private final String type;
    private final String mapping;

    private Metadata(MetadataBuilder builder){
        this.name = builder.name;
        this.type = builder.type;
        this.mapping = builder.mapping;
    }
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getMapping() {
        return mapping;
    }

    public static class MetadataBuilder{
        private String name;
        private String type;
        private String mapping;
        public MetadataBuilder name(String name){this.name =name; return this;}
        public MetadataBuilder type(String type){this.type=type;return this;}
        public MetadataBuilder mapping(String mapping){this.mapping = mapping;return this;}
        public Metadata build(){
            return new Metadata(this);
        }
    }
}

