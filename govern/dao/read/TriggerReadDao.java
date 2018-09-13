package com.engine.govern.dao.read;

import com.engine.govern.constant.ActionType;
import com.engine.govern.entity.ResponseActionSetting;
import com.engine.govern.entity.ResponseBillFiled;
import com.engine.govern.entity.ResponseGovernFiled;
import tebie.applib.api.O;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/3 14:47
 * @Description:
 */
public class TriggerReadDao {
    /*
     *  获取workflow_billfield表中的id和filedLabel
     * @author 谢凯
     * @date 2018/9/4 8:52
     * @param
     * @return
     */
    public List<ResponseBillFiled> getBillFiled(String billid,User user,String type){
        List<ResponseBillFiled> responseBillFileds = new ArrayList<>();
        RecordSet rs = new RecordSet();
        String sql = "";
        if("2".equals(type)){
            sql = "select id,fieldlabel from workflow_billfield where billid = ? and detailtable = '' or detailtable = null";
        }else {
            sql = "select id,fieldlabel from workflow_billfield where billid = ?";
        }
        rs.executeQuery(sql,billid);
        while (rs.next()){
            ResponseBillFiled responseBillFiled = new ResponseBillFiled();
            String id = Util.null2String(rs.getInt("id"));
            int fieldlabel = rs.getInt("fieldlabel");
            responseBillFiled.setId(id);
            responseBillFiled.setFieldlabel(SystemEnv.getHtmlLabelName(fieldlabel,user.getLanguage()));
            responseBillFileds.add(responseBillFiled);
        }
        return responseBillFileds;
    }

    /*
    *  分解动作设置时获取明细表中的字段
    * @author 武磊
    * @date 2018年9月12日10:58:05
    * @param
    * @return
    */
    public List<ResponseBillFiled> getDetailTableFields(String billid, String billTableId,User user){
        List<ResponseBillFiled> detailTableFields = new ArrayList<>();
        RecordSet rs = new RecordSet();
        String sql = "select id,fieldlabel from workflow_billfield where billid = ? and viewtype = 1 and detailtable = (select tablename from Workflow_billdetailtable where id = ?)";
        rs.executeQuery(sql,billid,billTableId);
        while (rs.next()){
            ResponseBillFiled responseBillFiled = new ResponseBillFiled();
            String id = Util.null2String(rs.getInt("id"));
            int fieldlabel =rs.getInt("fieldlabel");
            responseBillFiled.setId(id);
            responseBillFiled.setFieldlabel(SystemEnv.getHtmlLabelName(fieldlabel, user.getLanguage())+"("+SystemEnv.getHtmlLabelName(18550,user.getLanguage())+")");
            detailTableFields.add(responseBillFiled);
        }
        return detailTableFields;
    }

    /*
     *  获取触发方式
     * @author 谢凯
     * @date 2018/9/4 11:54
     * @param
     * @return
     */
    public  Map<String,Object>  getActionSetting(String categoryId,String type){
        Map<String,Object> result = new HashMap<>();
        String sql  = "select id,flowid,triggerType from govern_actionSetting where categoryid = ? and  actiontype = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,categoryId,type);
        if (rs.next()){
            int triggerType =  rs.getInt("triggerType");
            int flowid = rs.getInt("flowid");
            int id = rs.getInt("id");
            result.put("triggerType",triggerType);
            result.put("flowid",flowid);
            result.put("id",id);

                Object billid = this.getBillid(Util.null2String(flowid)).get("billid");
                result.put("billid",billid);


        }

        return result;
    }

    /*
     *  获取govern_field表中的数据
     * @author 谢凯
     * @date 2018/9/4 13:37
     * @param
     * @return
     */
    public List<ResponseGovernFiled> getGovernFiled(String source){
        List<ResponseGovernFiled> responseGovernFiledList = new ArrayList<>();
        String sql = "select id,fieldname,name,isrequired from govern_field where source = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,source);
        while (rs.next()){
            ResponseGovernFiled responseGovernFiled = new ResponseGovernFiled();
            String id = Util.null2String(rs.getInt("id"));
            String filedName = rs.getString("fieldname");
            String name = rs.getString("name");
            String isrequired =Util.null2String(rs.getInt("isrequired"));
            responseGovernFiled.setId(id);
            responseGovernFiled.setFieldName(filedName);
            responseGovernFiled.setName(name);
            responseGovernFiled.setIsrequired(isrequired);
            responseGovernFiledList.add(responseGovernFiled);
        }
        return responseGovernFiledList;
    }

    public String getSuperiorId(String  source,String name){
        RecordSet rs = new RecordSet();
        rs.executeQuery("select id from govern_field where source = ? and fieldname = ?",source,name);
        if(rs.next()){
            return Util.null2String(rs.getInt("id"));
        }
        return "";
    }

    public Map<String,String> getTriggerSetting(String triggerId){
        Map<String,String> result = new HashMap<>();
        String sql ="select governfieldid,flowfieldid from govern_triggerSetting where triggerid = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,triggerId);
        while (rs.next()){
            result.put(Util.null2String(rs.getInt("governfieldid")),Util.null2String(rs.getInt("flowfieldid")));
        }
        return result;
    }

    /*
     *  获取单据id
     * @author 谢凯
     * @date 2018/9/7 10:47
     * @param
     * @return
     */
    public Map<String,Object> getBillid(String flowid){
        Map<String,Object> result = new HashMap<>();
        String sql ="select formid from workflow_base where id = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,flowid);
        if (rs.next()){
            result.put("billid",Util.null2String(rs.getInt("formid")));
        }
        return result;
    }

    /*
     *  获取督办动作配置信息
     * @author 谢凯
     * @date 2018/9/7 11:26
     * @param
     * @return
     */
    public List<Map<String,Object>> getData(Object categoryId,Object actionType,String flowid){
        Map<String,Object> data = new HashMap<>();
        List<Map<String,Object>> datas = new ArrayList<>();

        String sql ="select id,ispreoperator,actionType,nodeId,isTriggerReject,workflowId from govern_actionConfig where categoryId= ? and actionType = ? order by id ";
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        rs.executeQuery(sql,categoryId,actionType);
        String id ="";
        String ispreoperator ="";
        String nodeId ="";
        String isTriggerReject ="";
        String nodeIdspan ="";
        int count = rs.getCounts();
        if (count == 0){
            data.put("id", id);
            data.put("actionType", ActionType.getValue(Util.null2String(actionType)));
            data.put("ispreoperator", ispreoperator);
            data.put("nodeId", nodeId);
            data.put("isTriggerReject",isTriggerReject);
            data.put("nodeIdspan", nodeIdspan);
            datas.add(data);
        }
            while (rs.next()){
            String workflowid = rs.getString("workflowId");
             if(flowid.equals(workflowid)){
                 id= Util.null2String(rs.getString("id"));
                 ispreoperator = Util.null2String(rs.getString("ispreoperator"));
                 nodeId = Util.null2String(rs.getString("nodeId"));
                 isTriggerReject = Util.null2String(rs.getString("isTriggerReject"));
                 data.put("id", id);
                 data.put("actionType", ActionType.getValue(Util.null2String(actionType)));
                 data.put("ispreoperator", ispreoperator);
                 data.put("nodeId", nodeId);
                 data.put("isTriggerReject",isTriggerReject);
                 rs1.executeQuery("select nodename from workflow_nodebase where id=?",nodeId);
                 if(rs1.next()){
                     nodeIdspan = Util.null2String(rs1.getString("nodename"));
                 }
                 data.put("nodeIdspan", nodeIdspan);
             }else {
                 data.put("id", id);
                 data.put("actionType", ActionType.getValue(Util.null2String(actionType)));
                 data.put("ispreoperator", ispreoperator);
                 data.put("nodeId", nodeId);
                 data.put("isTriggerReject",isTriggerReject);
                 nodeId = Util.null2String(rs.getString("nodeId"));
                 rs1.executeQuery("select nodename from workflow_nodebase where id=?",nodeId);
                 data.put("nodeIdspan", nodeIdspan);
                 }

             datas.add(data);
        }
        return datas;
    }

    public ResponseActionSetting getBiscInfo(Map<String,Object> params){
        String categoryId = Util.null2String(params.get("categoryId"));
        String actionType = Util.null2String(params.get("actionType"));
        String triggerType = Util.null2String(params.get("triggerType"));
        String flowId = Util.null2String(params.get("flowid"));
        String detailtableid = Util.null2String(params.get("billDetalTable"));
        String acId = Util.null2String(this.getActionSetting(categoryId,actionType).get("id"));
        ResponseActionSetting responseActionSetting = new ResponseActionSetting(acId,categoryId,actionType,triggerType,flowId,detailtableid);
        return responseActionSetting;
    }

    public String getWorkflowName(String id){
        RecordSet rs = new RecordSet();
        rs.executeQuery(" select workflowname from workflow_base where id = ?",id);
       if(rs.next()){
           String workflowName = rs.getString("workflowname");
           return  workflowName;
       }
        return "";
    }
    //-----
    /**
     * 获取分解动作设置时所需的选中流程后所需的明细表
     * @author 武磊
     * @date 2018年9月11日17:44:19
     * @param
     * @return
     */
    public List<Map<String,String>> getBillDetalTable(String flowid) {
        List<Map<String,String>> tables = new ArrayList<>();
        String billid = (String) this.getBillid(Util.null2String(flowid)).get("billid");
        String sql = "select id,billid,tablename from workflow_billdetailtable where billid = ?";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,billid);
        while(rs.next()) {
            Map<String,String> table = new HashMap<>();
            table.put("id",rs.getString("id"));
            table.put("billid",rs.getString("billid"));
            table.put("tablename",rs.getString("tablename"));
            tables.add(table);
        }
        return tables;
    }

    /**
     * 获取分解动作设置时保存的明细表id
     * @author 武磊
     * @date 2018年9月12日10:08:22
     * @param type
     * @param categoryId
     * @return
     */
    public String getBillTableId(String type, String categoryId) {
        String billTableId = "";
        String sql = "select detailtableid from govern_actionSetting where categoryid = ? and  actiontype = ? ";
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql,categoryId,type);
        if(rs.next()) {
            billTableId = rs.getString("detailtableid");
        }
        return billTableId;
    }
    //-----

}
