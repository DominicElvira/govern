package com.engine.govern.dao.write;


import com.engine.govern.constant.ActionName;
import com.engine.govern.constant.ActionType;
import com.engine.govern.dao.read.TriggerReadDao;
import com.engine.govern.entity.ResponseActionSetting;
import com.engine.govern.util.GovernWorkFlowUtil;
import net.sf.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import weaver.conn.ConnStatement;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/7 13:52
 * @Description: 增删改动作设置信息
 */
public class TriggerWriteDao {

    /*
     * 保存督办动作设置
     * @author 谢凯
     * @date 2018/9/7 14:19
     * @param categoryId、actionType、keepgroupids
     * @return
     */
    public Map<String,Object> saveActionConfig(Map<String,Object> params) {
        Map<String,Object> apidatas = new HashMap<>();
        RecordSet rs = new RecordSet();
        GovernWorkFlowUtil governWorkFlowUtil = new GovernWorkFlowUtil();
        String categoryId = Util.null2String(params.get("categoryId"));
        String customervalue = Util.null2String(params.get("actionType"));
        String workflowId = Util.null2String(params.get("flowid"));
        String dtinfo = Util.null2String(params.get("datas"));
        String keepgroupids = Util.null2String(params.get("keepgroupids"));
        if (keepgroupids.endsWith(",")) {
            keepgroupids = keepgroupids.substring(0, keepgroupids.length() - 1);
        }
        if ("".equals(keepgroupids)) {
            keepgroupids = "-1";
        }
        //rs.execute("delete from govern_actionConfig where id not in(" + keepgroupids + ") and categoryId = "+categoryId +"and actionType = "+customervalue);
         rs.executeUpdate("delete from govern_actionConfig where id not in(?) and categoryId = ? and actionType = ?",keepgroupids,categoryId,customervalue);
        net.sf.json.JSONArray dtJsonArray = net.sf.json.JSONArray.fromObject(dtinfo);

        if (dtJsonArray != null && dtJsonArray.size() > 0) {

            for (int i = 0; i < dtJsonArray.size(); i++) {
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(dtJsonArray.get(i));
                int objid = Util.getIntValue(Util.null2String(jsonObject.getString("nodeId")), 0);
               // int customervalue = Util.getIntValue(Util.null2String(jsonObject.getString("actionType")), 0);
                int isnode = Util.getIntValue(Util.null2String(jsonObject.getString("ispreoperator")), 0);
                int groupid = Util.getIntValue(jsonObject.has("id") ? Util.null2String(jsonObject.getString("id")) : "", 0);
                int isTriggerReject = Util.getIntValue(Util.null2String(jsonObject.getString("isTriggerReject")), 0);

                String sql = "";
                if (groupid > 0) {
//                    sql = "update govern_actionConfig set nodeId='" + objid + "',actionType='" + customervalue + "',ispreoperator='" + isnode + "',isTriggerReject='" + isTriggerReject + "' where id=" + groupid;
                    rs.executeUpdate("update govern_actionConfig set nodeId=?,actionType=?,ispreoperator=?,isTriggerReject=?,workflowId = ? where id = ?",objid,customervalue,isnode,isTriggerReject,workflowId,groupid);
                } else {
                    //sql = "insert into govern_actionConfig(categoryId,actionType,workflowId,isTriggerReject,ispreoperator,nodeId) values('" + categoryId + "','" + customervalue + "','" + workflowId + "','" + isTriggerReject + "','" + isnode+ "','" + objid  + "') ";
                    rs.executeUpdate("insert into govern_actionConfig(categoryId,actionType,workflowId,isTriggerReject,ispreoperator,nodeId) values(?,?,?,?,?,?)",categoryId,customervalue,workflowId,isTriggerReject,isnode,objid);
                }
               // rs.execute(sql);
            }
        }
        String actname = ActionName.getValue(customervalue);
        rs.executeQuery("select id,ispreoperator,actionType,nodeId,isTriggerReject,actionsetId from govern_actionConfig where categoryId =? and actionType = ? ",categoryId,customervalue);
        int counts = rs.getCounts();
        if (counts > 0) {
            int[] nodeIdsArray = new int[counts];
            int[] workflowid = new int[counts];
            int[] isnode = new int[counts];
            int[] ispreadd = new int[counts];
            String[] customervalues = new String[counts];
            int[] isTriggerReject = new int[counts];
            int[] actionsetIds = new int[counts];
            int i = 0;
            while (rs.next()) {
                actionsetIds[i] = rs.getInt("actionsetId");
                nodeIdsArray[i]=Util.getIntValue(rs.getString("nodeId"),0);
                isTriggerReject[i]=Util.getIntValue(rs.getString("isTriggerReject"),0);
                workflowid[i]=Util.getIntValue(workflowId ,0);
                isnode[i]=Util.getIntValue(rs.getString("ispreoperator"),0);
                ispreadd[i]=isnode[i];
                //isnode[i]=isnode[i]>0?1:0;
                isnode[i]=1;
                isTriggerReject[i]=isnode[i]==0?0:isTriggerReject[i];//???
                customervalues[i]=actname;
                i++;
            }
            //List<Integer> actionIds = governWorkFlowUtil.saveActionSet(nodeIdsArray, workflowid, isnode, ispreadd, customervalues, isTriggerReject);
            List actionIds = governWorkFlowUtil.saveActionSet2(nodeIdsArray, workflowid, isnode, ispreadd, customervalues, isTriggerReject,actionsetIds);
            //只适用每种督办类型只有一个触发动作
            rs.executeUpdate("update govern_actionConfig set actionsetId = ? where categoryId =? and actionType = ? ",actionIds.get(0),categoryId,customervalue);
            } else {
            governWorkFlowUtil.clearActionSet(new int[]{Util.getIntValue(workflowId ,0)}, new String[]{actname});//???
        }
        apidatas.put("success", true);
        return apidatas;
    }

    public Map<String,Object> saveActionSetting(Map<String,Object> params){
        Map<String,Object> result = new HashMap<>();
        RecordSet rs = new RecordSet();
        TriggerReadDao triggerReadDao = new TriggerReadDao();
        ResponseActionSetting biscInfo =triggerReadDao.getBiscInfo(params);
        //StringBuffer updatesql = new StringBuffer();
        if("1".equals(biscInfo.getTriggerType())){
            if("2".equals(biscInfo.getActionType())) {
                String sql = "update govern_actionSetting set flowid =? ,triggerType = ? ,detailtableid = ? where id = ?";
                rs.executeUpdate(sql,biscInfo.getFlowId(),biscInfo.getTriggerType(),biscInfo.getDetailtableid(),biscInfo.getAcId());
            } else {
                String sql = "update govern_actionSetting set flowid =? ,triggerType = ? where id = ?";
                rs.executeUpdate(sql,biscInfo.getFlowId(),biscInfo.getTriggerType(),biscInfo.getAcId());
            }
        }
        if("0".equals(biscInfo.getTriggerType())){
            String sql = "update govern_actionSetting set triggerType = ? where id = ?";
            rs.executeUpdate(sql,biscInfo.getTriggerType(),biscInfo.getAcId());
            //查看中间表是否有记录
            rs.executeQuery("select actionsetId from govern_actionConfig where categoryId = ? and actionType = ?",biscInfo.getCategoryId(),biscInfo.getActionType());
            if (rs.next()){
                GovernWorkFlowUtil governWorkFlowUtil = new GovernWorkFlowUtil();
                int actionsetId = rs.getInt("actionsetId");
                int[] actionIds =  {actionsetId};
                governWorkFlowUtil.clearActionSet2(actionIds);
                rs.executeUpdate("delete from govern_actionConfig where categoryId = ? and actionType = ?",biscInfo.getCategoryId(),biscInfo.getActionType());
            }
        }
        result.put("success",true);
        return result;
    }

    /*
     *  初始化ActionAsetting
     * @author 谢凯
     * @date 2018/9/11 15:37
     * @param
     * @return
     */

    public void initActionSetting(int id){
        ConnStatement stat = new ConnStatement();
        RecordSet rs = new RecordSet();
        try{
            String sql = "insert into govern_actionSetting(categoryid,actionType,triggerType) " +
                    "values(?,?,?)";
            stat.setStatementSql(sql);
          for(int i = 0;i<5;i++){
              stat.setInt(1,id);
              stat.setInt(2,i);
              stat.setInt(3,0);
              stat.executeUpdate();
          }
        }catch(Exception e){
            e.printStackTrace();
            new BaseBean().writeLog("initActionSetting:insert false");
        }finally{
            stat.close();
        }

    }

    /*
     *  保存对应关系
     * @author 谢凯
     * @date 2018/9/9 15:55
     * @param
     * @return
     */
    public Map<String,Object> saveTriggerSetting(Map<String,Object> params){
        Map<String,Object> result = new HashMap<>();
        TriggerReadDao triggerReadDao = new TriggerReadDao();
        RecordSet recordSet = new RecordSet();
        //获取对应值
        Map<String,Object> filedData = JSONObject.fromObject(params.get("filedData"));
        //Map<String,Object> filedData = params.get("filedData");
        ResponseActionSetting responseActionSetting = triggerReadDao.getBiscInfo(params);
        String triggerId = responseActionSetting.getAcId();
        if(!"".equals(triggerId)){
            recordSet.executeUpdate( "delete from govern_triggerSetting where triggerid = ?",triggerId);
            for(Map.Entry<String,Object> entry :filedData.entrySet()){
                String governfieldId =  entry.getKey();
                String flowfieldId = Util.null2String(entry.getValue());
                String sql = "insert into govern_triggerSetting(governfieldid,flowfieldid,triggerid) values (?,?,?)";
                recordSet.executeUpdate(sql,governfieldId,flowfieldId,triggerId);
            }
            result.put("success",true);
            return result;
        }else {
            result.put("success",false);
            return result;
        }
    }

    /*
     *  删除执行设置有关的信息
     * @author 谢凯
     * @date 2018/9/11 16:27
     * @param
     * @return
     */

    public void deleteTriggerSettingInfo(String categoryId){
        RecordSetTrans rst = new RecordSetTrans();
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        List<List<Object>> sqlParams = new ArrayList<List<Object>>();
        GovernWorkFlowUtil governWorkFlowUtil = new GovernWorkFlowUtil();
        try{
            rst.setAutoCommit(false);

            rs.executeQuery("select id from govern_actionSetting where categoryid = ?",categoryId);
            while (rs.next()){
                List<Object> params = new ArrayList<>();
                params.add(rs.getInt("id"));
                sqlParams.add(params);
            }
            rst.executeBatchSql("delete from govern_triggerSetting where triggerid = ?",sqlParams);

            rs1.executeQuery("select actionsetId from govern_actionConfig where categoryId = ?",categoryId);
            int counts = rs1.getCounts();
            int[] actionsetIds = new int[counts];
            int i =0;
            while (rs1.next()){
                actionsetIds[i] = rs1.getInt("actionsetId");
                i++;
            }
            governWorkFlowUtil.clearActionSet2(actionsetIds);

            rst.executeUpdate("delete from govern_actionConfig where categoryid = ?",categoryId);

            rst.executeUpdate("delete from govern_actionSetting where categoryid = ?",categoryId);

            rst.commit();
        }catch(Exception e){
            rst.rollback();
        }

    }

}
