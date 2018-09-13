package com.engine.govern.constant;

public enum Remind {

    没有设置(0,"没有设置"),
    短信提醒(1,"短信提醒"),
    微信提醒(2,"微信提醒"),
    message提醒(3,"message提醒");

    private int code;
    private String value;

    Remind(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(int code){
        for(Remind o:values()){
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
