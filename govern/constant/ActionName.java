package com.engine.govern.constant;

public enum ActionName {

    GovernNewAction("0","GovernNewAction"),
    GovernReportAction("1","GovernReportAction"),
    GovernDecomposeAction("2","GovernDecomposeAction"),
    GovernPromptAction("3","GovernPromptAction"),
    GovernChangeAction("4","GovernChangeAction");


    private String code;
    private String value;

    ActionName(String  code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code){
        for(ActionName o:values()){
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
