package com.engine.govern.util;

import org.apache.commons.lang.StringEscapeUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.servicefiles.ActionXML;
import weaver.workflow.action.WorkflowActionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/7 14:55
 * @Description:
 */
public class GovernWorkFlowUtil {
    /**
     *
     * @param nodeIdsArray
     * @param workflowid
     * @param isnode  是否为节点附加条件还是出口附加条件：1：节点；0：出口；
     * @param ispreadd  是否节点前操作：1：是；0：否；
     * @param customervalue  自定义值：type为0时，值为普通的附加操作设置；type为1时，值为附件上传,单文档，多文档的文档状态设置，如2，5等；type为2时，值为外部接口，如action.WorkflowToDoc；
     */
    public List<Integer> saveActionSet(int[] nodeIdsArray, int[] workflowid, int[] isnode, int[] ispreadd, String[] customervalue, int[] isTriggerReject){
        List<Integer> actionIds = new ArrayList<>();
        if(nodeIdsArray==null||workflowid==null||isnode==null||ispreadd==null||customervalue==null||isTriggerReject==null){
            new BaseBean().writeLog("保存节点动作出错!");
            System.err.println("保存节点动作出错!");
            return null;
        }
        RecordSet rs1 = new RecordSet();
        ActionXML actionXML = new ActionXML();
        actionXML.initAction();
        clearActionSet(workflowid,customervalue);
        for (int i = 0; i < nodeIdsArray.length; i++) {

            int nodeid = nodeIdsArray[i];
            if(nodeid <= 0){
                continue;
            }
            int Nodelinkid = 0;
            if(isnode[i]==0){
                Nodelinkid = nodeid;
                nodeid = 0;
            }
            WorkflowActionManager workflowActionManager = new WorkflowActionManager();
            workflowActionManager.setIsTriggerReject(isTriggerReject[i]);
            workflowActionManager.setActionid(0);
            workflowActionManager.setWorkflowid(workflowid[i]);
            workflowActionManager.setNodeid(nodeid);//节点id
            workflowActionManager.setActionorder(99999);
            workflowActionManager.setNodelinkid(Nodelinkid);//出口id
            workflowActionManager.setIspreoperator(ispreadd[i]);
            workflowActionManager.setActionname(customervalue[i]);
            workflowActionManager.setInterfaceid(customervalue[i].replace("action.", ""));
            workflowActionManager.setInterfacetype(3);
            workflowActionManager.setIsused(1);
            int actionid = workflowActionManager.doSaveWsAction();
            actionIds.add(actionid);
        }

        actionXML.initAction();
        return actionIds;

    }

    /**
     *
     * @param nodeIdsArray
     * @param workflowid
     * @param isnode  是否为节点附加条件还是出口附加条件：1：节点；0：出口；
     * @param ispreadd  是否节点前操作：1：是；0：否；
     * @param customervalue  自定义值：type为0时，值为普通的附加操作设置；type为1时，值为附件上传,单文档，多文档的文档状态设置，如2，5等；type为2时，值为外部接口，如action.WorkflowToDoc；
     */
    public List saveActionSet2(int[] nodeIdsArray, int[] workflowid, int[] isnode, int[] ispreadd, String[] customervalue, int[] isTriggerReject,int[] actionsetIds){
        List actionIds = new ArrayList<>();
        if(nodeIdsArray==null||workflowid==null||isnode==null||ispreadd==null||customervalue==null||isTriggerReject==null){
            new BaseBean().writeLog("保存节点动作出错!");
            System.err.println("保存节点动作出错!");
            return null;
        }
        RecordSet rs1 = new RecordSet();
        ActionXML actionXML = new ActionXML();
        actionXML.initAction();
        clearActionSet2(actionsetIds);
        for (int i = 0; i < nodeIdsArray.length; i++) {

            int nodeid = nodeIdsArray[i];
            if(nodeid <= 0){
                continue;
            }
            int Nodelinkid = 0;
            if(isnode[i]==0){
                Nodelinkid = nodeid;
                nodeid = 0;
            }
            WorkflowActionManager workflowActionManager = new WorkflowActionManager();
            workflowActionManager.setIsTriggerReject(isTriggerReject[i]);
            workflowActionManager.setActionid(0);
            workflowActionManager.setWorkflowid(workflowid[i]);
            workflowActionManager.setNodeid(nodeid);//节点id
            workflowActionManager.setActionorder(99999);
            workflowActionManager.setNodelinkid(Nodelinkid);//出口id
            workflowActionManager.setIspreoperator(ispreadd[i]);
            workflowActionManager.setActionname(customervalue[i]);
            workflowActionManager.setInterfaceid(customervalue[i].replace("action.", ""));
            workflowActionManager.setInterfacetype(3);
            workflowActionManager.setIsused(1);
            int actionid = workflowActionManager.doSaveWsAction();
            actionIds.add(actionid);
        }

        actionXML.initAction();
        return actionIds;

    }

    /**
     * 清除流程的action
     * @param workflowid
     * @param customervalue
     * @return
     */
    public boolean clearActionSet(int[] workflowid, String[] customervalue){
        if(workflowid==null||customervalue==null){
            new BaseBean().writeLog("清除节点动作出错!");
            System.err.println("清除节点动作出错!");
            return false;
        }
        RecordSet rs1 = new RecordSet();
        for (int i = 0; i < workflowid.length; i++) {
            rs1.executeSql("select * from workflowactionset a " +
                    " where a.interfaceid = '"+ StringEscapeUtils.escapeSql(customervalue[i].replace("action.", ""))+"' " +
                    " and a.workflowid = "+workflowid[i]);
            while(rs1.next()){
                int _actionid = Util.getIntValue(rs1.getString("id"), 0);
                int _nodeid = Util.getIntValue(rs1.getString("nodeid"), 0);
                int _Actionorder = Util.getIntValue(rs1.getString("Actionorder"), 99999);
                int _Nodelinkid = Util.getIntValue(rs1.getString("Nodelinkid"), 0);
                int _ispreadd = Util.getIntValue(rs1.getString("Ispreoperator"), 0);
                String _customervalue = Util.null2String(rs1.getString("Actionname"));
                String _interfaceid = Util.getIntValue(rs1.getString("Interfaceid"), 0)+"";
                int _isused = Util.getIntValue(rs1.getString("isused"), 0);

                WorkflowActionManager workflowActionManager = new WorkflowActionManager();

                workflowActionManager.setActionid(_actionid);
                workflowActionManager.setWorkflowid(workflowid[i]);
                workflowActionManager.setNodeid(_nodeid);
                workflowActionManager.setActionorder(_Actionorder);
                workflowActionManager.setNodelinkid(_Nodelinkid);
                workflowActionManager.setIspreoperator(_ispreadd);
                workflowActionManager.setActionname(_customervalue);
                workflowActionManager.setInterfaceid(_interfaceid);
                workflowActionManager.setInterfacetype(3);
                workflowActionManager.setIsused(_isused);

                workflowActionManager.doDeleteWsAction();
            }
        }
        return true;
    }

    /**
     * 清除流程的action
     * @param actionSetId
     * @return
     */
    public boolean clearActionSet2(int[] actionSetId){
        RecordSet rs1 = new RecordSet();
        for (int i = 0; i < actionSetId.length; i++) {
            if(actionSetId[i]!=-1){
                rs1.executeQuery("select * from workflowactionset where id = ?",actionSetId[i]);
                while(rs1.next()){
                    int _actionid = Util.getIntValue(rs1.getString("id"), 0);
                    int _nodeid = Util.getIntValue(rs1.getString("nodeid"), 0);
                    int _Actionorder = Util.getIntValue(rs1.getString("Actionorder"), 99999);
                    int _Nodelinkid = Util.getIntValue(rs1.getString("Nodelinkid"), 0);
                    int _ispreadd = Util.getIntValue(rs1.getString("Ispreoperator"), 0);
                    String _customervalue = Util.null2String(rs1.getString("Actionname"));
                    String _interfaceid = Util.getIntValue(rs1.getString("Interfaceid"), 0)+"";
                    int _workflowid = rs1.getInt("workflowid");
                    int _isused = Util.getIntValue(rs1.getString("isused"), 0);

                    WorkflowActionManager workflowActionManager = new WorkflowActionManager();

                    workflowActionManager.setActionid(_actionid);
                    workflowActionManager.setWorkflowid(_workflowid);
                    workflowActionManager.setNodeid(_nodeid);
                    workflowActionManager.setActionorder(_Actionorder);
                    workflowActionManager.setNodelinkid(_Nodelinkid);
                    workflowActionManager.setIspreoperator(_ispreadd);
                    workflowActionManager.setActionname(_customervalue);
                    workflowActionManager.setInterfaceid(_interfaceid);
                    workflowActionManager.setInterfacetype(3);
                    workflowActionManager.setIsused(_isused);

                    workflowActionManager.doDeleteWsAction();
                }
            }
        }
        return true;
    }
}
