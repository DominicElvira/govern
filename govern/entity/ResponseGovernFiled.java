package com.engine.govern.entity;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/4 13:33
 * @Description:
 */
public class ResponseGovernFiled {
    private String  id;
    private String name;
    private String fieldName;

    private String isrequired;

    public String getIsrequired() {
        return isrequired;
    }

    public void setIsrequired(String isrequired) {
        this.isrequired = isrequired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
