package com.engine.govern.constant;

public enum YesOrNo {

    是(1,"是"),
    否(0,"否");

    private int code;
    private String value;

    YesOrNo(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(int code){
        for(YesOrNo o:values()){
            if(o.code==code){
                return o.getValue();
            }
        }
        return "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
