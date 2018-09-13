package com.engine.govern.cmd.triggerSetting;

import com.api.browser.bean.SearchConditionGroup;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.TriggerReadDao;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/5 15:28
 * @Description:获取基本信息
 */
public class GetBasicConditionCmd extends AbstractCommonCommand<Map<String,Object>> {
    public GetBasicConditionCmd(Map<String,Object> params,User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    /*
     *  获取基本信息condition
     * @author 谢凯
     * @date 2018/9/5 16:50
     * @param 
     * @return 
     */

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        ConditionFactory conditionFactory = new ConditionFactory(user);
        List<SearchConditionGroup> addgroup =  new ArrayList<>();
        List<SearchConditionItem> addItem = new ArrayList<>();
        Map<String,Object> apidata = new HashMap<>();
        List<Map<String, Object>> formInfo = new ArrayList<Map<String, Object>>();
        Map<String, Object> formInfoMap = new HashMap<String, Object>();
        TriggerReadDao triggerReadDao = new TriggerReadDao();

        addItem = new ArrayList<>();
        SearchConditionItem workFlowName = conditionFactory.createCondition(ConditionType.BROWSER,18104,"workFlowName","-99991");
        //workFlowName.getBrowserConditionParam().setOnChange("javascript:onchange()");没有用！！！！
        workFlowName.setColSpan(1);
        workFlowName.setValue(Util.null2String(params.get("flowid")));
        workFlowName.setViewAttr(3);
        workFlowName.setRules("required");
        addItem.add(workFlowName);

        String workflowName = triggerReadDao.getWorkflowName(Util.null2String(params.get("flowid")));
        formInfoMap.put("id", params.get("flowid"));
        formInfoMap.put("name", workflowName);
        formInfo.add(formInfoMap);
        workFlowName.getBrowserConditionParam().setReplaceDatas(formInfo);
        //-----表示当前操作是分解动作的设置
        if("2".equals(params.get("type"))) {
            List<SearchConditionOption> options = new ArrayList<SearchConditionOption>();
            List<Map<String, String>> billTables = triggerReadDao.getBillDetalTable(Util.null2String(params.get("flowid")));
            String billTableId = triggerReadDao.getBillTableId(Util.null2String(params.get("type")),Util.null2String(params.get("categoryId")));
            for (Map<String, String> billTable : billTables) {
                options.add(new SearchConditionOption(billTable.get("id"), billTable.get("tablename"), billTable.get("id").equals(billTableId) ? true : false));
            }
            addItem.add(conditionFactory.createCondition(ConditionType.SELECT, 19325, "billDetalTable", options));
        }
        //-----
        addgroup.add(new SearchConditionGroup("基本信息",true,addItem));
        apidata.put("basicConditionGroup",addgroup);
        return apidata;
    }
}
