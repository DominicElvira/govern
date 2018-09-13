package com.engine.govern.entity;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/7 17:21
 * @Description:
 */
public class ResponseActionSetting {
    private String  acId;
    private  String categoryId;
    private  String actionType;
    private  String triggerType;
    private  String flowId;
    private  String detailtableid;

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getDetailtableid() {
        return detailtableid;
    }

    public void setDetailtableid(String detailtableid) {
        this.detailtableid = detailtableid;
    }

    public ResponseActionSetting(String acId, String categoryId, String actionType, String triggerType, String flowId, String detailtableid) {
        this.categoryId = categoryId;
        this.actionType = actionType;
        this.triggerType = triggerType;
        this.flowId = flowId;
        this.acId = acId;
        this.detailtableid = detailtableid;
    }

    public ResponseActionSetting() {
    }
}
