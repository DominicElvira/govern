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
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/5 17:05
 * @Description:
 */
public class GetTriggerConditionCmd  extends AbstractCommonCommand<Map<String,Object>> {
    public GetTriggerConditionCmd(Map<String,Object> params,User user ) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    /*
     *
     * @author 谢凯
     * @date 2018/9/5 17:11
     * @param
     * @return
     */
    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> apidatas  = new HashMap<String, Object>();
        ConditionFactory conditionFactory = new ConditionFactory(user);
        List<SearchConditionGroup> addgroup =  new ArrayList<>();
        List<SearchConditionItem> addItem = new ArrayList<>();
        TriggerReadDao triggerReadDao = new TriggerReadDao();

         List<SearchConditionOption> triggerTypeOps = new ArrayList<SearchConditionOption>();
        Map<String,Object>  actionSetting = triggerReadDao.getActionSetting(Util.null2String(params.get("categoryId")),Util.null2String(params.get("type")));//获得触发方式
        int triType = (int) actionSetting.get("triggerType");
        triggerTypeOps.add(new SearchConditionOption("0", SystemEnv.getHtmlLabelNames("-16007,21805",user.getLanguage()),triType == 0?true:false));
        triggerTypeOps.add(new SearchConditionOption("1",SystemEnv.getHtmlLabelNames("131412,21805",user.getLanguage()),triType == 1?true:false));
        SearchConditionItem triggerType = conditionFactory.createCondition(ConditionType.SELECT,"21805,599","triggerType",triggerTypeOps);
        addItem.add(triggerType);
        addgroup.add(new SearchConditionGroup("触发设置",true,addItem));
        apidatas.put("triggerGroup",addgroup);
        return apidatas;
    }
}
