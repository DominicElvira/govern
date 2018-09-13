package com.engine.govern.constant;

public enum ActionType {
    新建("0","新建"),
    汇报("1","汇报"),
    分解("2","分解"),
    催办("3","催办"),
    变更("4","变更");
    private String code;
    private String value;
    ActionType(String  code, String value) {
        this.code = code;
        this.value = value;
    }
    public static String getValue(String code){
        for(ActionType o:values()){
            if(o.code.equals(code)){
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
