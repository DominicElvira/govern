package com.engine.govern.cmd.triggerSetting;

import com.api.browser.bean.SearchConditionGroup;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.constant.ActionType;
import com.engine.govern.dao.read.TriggerReadDao;
import com.engine.govern.entity.ResponseBillFiled;
import com.engine.govern.entity.ResponseGovernFiled;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/5 15:56
 * @Description:
 */
public class GetfiledConditionCmd extends AbstractCommonCommand<Map<String,Object>> {
    public GetfiledConditionCmd(Map<String,Object> params,User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    /*
     *获取字段设置condition
     * @author 谢凯
     * @date 2018/9/5 16:04
     * @param billid、categoryId、type
     * @return
     */
    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> apidatas  = new HashMap<String, Object>();
        List<SearchConditionGroup> addgroup =  new ArrayList<>();
        List<SearchConditionItem> addItem = new ArrayList<>();
        TriggerReadDao triggerReadDao = new TriggerReadDao();
        List<String> filedIds = new ArrayList<>();
        List<ResponseBillFiled> billFileds = new ArrayList<>();
        List<ResponseBillFiled> detailTableFields = new ArrayList<>();
        String source ="";
        String billTableId = "";   //分解动作设置时明细表id

        Map<String,Object>  actionSetting = triggerReadDao.getActionSetting(Util.null2String(params.get("categoryId")),Util.null2String(params.get("type")));//获得触发方式
        int triType = (int) actionSetting.get("triggerType");//0卡片触发
        //获取对应关系
        Map<String ,String> triggerSetting = triggerReadDao.getTriggerSetting(Util.null2String(actionSetting.get("id")));

        //source = ActionType.getValue(Util.null2String(params.get("type")));
        //汇报
        if("1".equals(Util.null2String(params.get("type")))){
            source = "2";
        }
        //分解
        if("2".equals(Util.null2String(params.get("type")))){
            source = "1";
            billTableId = triggerReadDao.getBillTableId(Util.null2String(params.get("type")),Util.null2String(params.get("categoryId")));
            if(!"".equals(Util.null2String(params.get("billTableId"))) && params.get("billTableId") != null) {
                billTableId = Util.null2String(params.get("billTableId"));
            }
        }
        //催办
        if("3".equals(Util.null2String(params.get("type")))){
            source = "4";
        }
        //新建
        if("0".equals(Util.null2String(params.get("type")))){
            source = "1";
        }
        //变更
        if("4".equals(Util.null2String(params.get("type")))){
            source = "1";
        }
        List<ResponseGovernFiled> responseGovernFiledList = triggerReadDao.getGovernFiled(source);
        //获取前端传入的billid
        if (!"".equals(Util.null2String(params.get("billid")))) {
            billFileds = triggerReadDao.getBillFiled(Util.null2String(params.get("billid")),this.user,Util.null2String(params.get("type")));//
            if("2".equals(params.get("type"))) {
                detailTableFields = triggerReadDao.getDetailTableFields(Util.null2String(params.get("billid")), billTableId,this.user);
            }
        }
        if("".equals(Util.null2String(params.get("billid")))&&triType==1){
            billFileds = triggerReadDao.getBillFiled(Util.null2String(actionSetting.get("billid")),this.user,Util.null2String(params.get("type")));
            if("2".equals(params.get("type"))) {
                detailTableFields = triggerReadDao.getDetailTableFields(Util.null2String(actionSetting.get("billid")), billTableId,this.user);
            }
        }

        List<ResponseBillFiled> maintable = new ArrayList<>();
        List<ResponseBillFiled> detailTable = new ArrayList<>();
        List<ResponseBillFiled> cricelTable = new ArrayList<>();
        maintable.addAll(billFileds);
        detailTable.addAll(billFileds);
        detailTable.addAll(detailTableFields);
        String superiorId = triggerReadDao.getSuperiorId("1","superior");
        for(ResponseGovernFiled responseGovernFiled : responseGovernFiledList) {
            String filedName = responseGovernFiled.getFieldName();
            String name = responseGovernFiled.getName();
            String filedId = responseGovernFiled.getId();
            String isrequired = responseGovernFiled.getIsrequired();
            SearchConditionItem item = new SearchConditionItem(ConditionType.SELECT, name, new String[]{filedId});
            if ("1".equals(isrequired)){
                item.setViewAttr(3);
                item.setRules("required");
            }
            List<SearchConditionOption> billFiledOps = new ArrayList<SearchConditionOption>();
            billFiledOps.add(new SearchConditionOption("", ""));


            if("2".equals(params.get("type")) && !superiorId.equals(responseGovernFiled.getId())) {

                cricelTable = detailTable;
            }
            if("2".equals(params.get("type")) && superiorId.equals(responseGovernFiled.getId())) {
                cricelTable = maintable;
            }
            for (ResponseBillFiled responseBillFiled : cricelTable) {
                SearchConditionOption billFiled = new SearchConditionOption(responseBillFiled.getId(), responseBillFiled.getFieldlabel());
                if (triType == 1) {
                    //fieldid存在对应关系表中时应默认选择对应flowfiled
                    if (triggerSetting.get(filedId) != null) {
                        if (Util.null2String(triggerSetting.get(filedId)).equals(responseBillFiled.getId())) {
                            billFiled.setSelected(true);
                        }
                    }
                }

                billFiledOps.add(billFiled);
            }

            item.setOptions(billFiledOps);
            addItem.add(item);
            filedIds.add(filedId);
        }
        addgroup.add(new SearchConditionGroup(SystemEnv.getHtmlLabelName(21903, user.getLanguage()),true,addItem));
        apidatas.put("filedGroup",addgroup);
        apidatas.put("filedIds",filedIds);
        return apidatas;
    }
}
