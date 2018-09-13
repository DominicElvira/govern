package com.engine.govern.cmd.triggerSetting;

import com.api.prj.bean.PrjRightMenu;
import com.api.prj.bean.PrjRightMenuType;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.TriggerReadDao;
import com.engine.govern.util.GovernFormItemUtil;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.proj.util.PrjWfConfComInfo;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.workflow.WorkflowComInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/6 09:56
 * @Description:获取动作设置的condition
 * @Prams:categoryId、actionType、flowid
 */
public class GetActionConditionCmd extends AbstractCommonCommand<Map<String,Object>> {
    public GetActionConditionCmd(Map<String,Object> params,User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> apidatas = new HashMap<String,Object>();
        TriggerReadDao triggerReadDao = new TriggerReadDao();
        String wfid = Util.null2String(params.get("flowid"));//流程id

        String titlelabel= "33407";//动作列表

        //columns
        Map<String, Object> fieldinfo = new HashMap<String, Object>();
        fieldinfo.put("title", SystemEnv.getHtmlLabelNames(titlelabel,user.getLanguage()));

        List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
        Map<String, Object> column = new HashMap<String, Object>();
        Map<String, Object> formItem = new HashMap<String, Object>();
        column.put("title", SystemEnv.getHtmlLabelNames("19831",user.getLanguage()));
        column.put("dataIndex", "actionType");
        column.put("key", "actionType");
        column.put("colSpan", 1);
        column.put("width", "20%");
        List<Map<String, Object>> formItems = new ArrayList<Map<String, Object>>();

        formItem.put("editType", 1);
        formItem.put("viewAttr", 1);
        formItem.put("key", "actionType");
        formItem.put("type", "text");
        formItems.add(formItem);
        column.put("com", formItems);
        columns.add(column);

        column = new HashMap<String, Object>();
        column.put("title", SystemEnv.getHtmlLabelNames("33408",user.getLanguage()));
        column.put("dataIndex", "ispreoperator");
        column.put("key", "ispreoperator");
        column.put("colSpan", 1);
        column.put("width", "25%");
        formItems = new ArrayList<Map<String, Object>>();
        formItem = GovernFormItemUtil.getWfSetNodeSelect("ispreoperator","","",2,user);
        formItem.put("key", "ispreoperator");
        formItem.put("type", formItem.get("conditionType"));
        formItem.put("editType", 2);
        formItem.put("viewAttr", 2);
        formItems.add(formItem);
        column.put("com", formItems);
        columns.add(column);

        column = new HashMap<String, Object>();
        column.put("dataIndex", "nodeId");
        column.put("title", SystemEnv.getHtmlLabelNames("33410",user.getLanguage()));
        column.put("key", "nodeId");//出口
        column.put("colSpan", 1);
        column.put("width", "25%");
        formItems = new ArrayList<Map<String, Object>>();
        Map<String,Object> dataParams = new HashMap<String,Object>();
        dataParams.put("workflowid", wfid);//放入对应的流程id
        formItem = GovernFormItemUtil.getFormItemForBrowser("nodeId", "", "workflowNode", "", 3, "", null,dataParams);
        Map<String , String > browserConditionParam = (HashMap)formItem.get("browserConditionParam");
        browserConditionParam.put("title", SystemEnv.getHtmlLabelNames("33410",user.getLanguage()));
        formItem.put("browserConditionParam", browserConditionParam);
        formItem.put("key", "nodeId");
        formItem.put("type", formItem.get("conditionType"));
        formItem.put("editType", 3);
        formItem.put("viewAttr", 3);
       // formItem.put("")
        formItems.add(formItem);
        column.put("com", formItems);
        columns.add(column);

        column = new HashMap<String, Object>();
        column.put("title", SystemEnv.getHtmlLabelNames("33409",user.getLanguage()));
        column.put("dataIndex", "isTriggerReject");
        column.put("key", "isTriggerReject");
        column.put("colSpan", 1);
        column.put("width", "20%");
        formItems = new ArrayList<Map<String, Object>>();
        formItem = GovernFormItemUtil.getFormItemForCheckbox("isTriggerReject", "","0", 2);
        formItem.put("key", "isTriggerReject");
        formItem.put("type", formItem.get("conditionType"));
        formItem.put("editType", 2);
        formItem.put("viewAttr", 2);
        formItems.add(formItem);
        column.put("com", formItems);
        columns.add(column);
        fieldinfo.put("columns", columns);

        //data
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        datas =triggerReadDao.getData(params.get("categoryId"),params.get("actionType"),wfid);
        fieldinfo.put("datas", datas);
        apidatas.put("fieldinfo", fieldinfo);
        return apidatas;
    }
}
