package com.engine.govern.cmd.category;

import com.api.browser.bean.SearchConditionGroup;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.api.meeting.util.FieldUtil;
import com.api.system.createDB.bean.ConditionOption;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.CategoryReadDao;
import javassist.compiler.ast.CondExpr;
import org.apache.axis2.databinding.types.xsd.Integer;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/28 07:18
 * @Description:获取编辑督办类型的condition
 */
public class GetEditConditionCmd extends AbstractCommonCommand<Map<String,Object>> {

    public GetEditConditionCmd(User user, Map<String,Object> params) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> apidatas  = new HashMap<String, Object>();

        ConditionFactory ConditionFactory = new ConditionFactory(user);
        CategoryReadDao CategoryReadDao = new CategoryReadDao();
        Map<String,Object> data = CategoryReadDao.getCategory(Util.null2String(params.get("id")));
        Map<String,Object> category = (Map<String, Object>) data.get("data");
        List<String> parentIds = CategoryReadDao.getParentIds(Util.null2String(params.get("id")));
        List<SearchConditionGroup> editgroup =  new ArrayList<>();
        List<SearchConditionItem> editItem = new ArrayList<>();
        editgroup.add(new SearchConditionGroup("基本信息",true,editItem));

        SearchConditionItem name = ConditionFactory.createCondition(ConditionType.INPUT,15795,"name");
        SearchConditionItem parentName = ConditionFactory.createCondition(ConditionType.INPUT,"596,21223,63","parentName");
        SearchConditionItem describe = ConditionFactory.createCondition(ConditionType.TEXTAREA,433,"describe");
        SearchConditionItem isused = ConditionFactory.createCondition(ConditionType.SWITCH,18624,"isused");
        SearchConditionItem isauto = ConditionFactory.createCondition(ConditionType.SWITCH,-15946,"isauto");
        SearchConditionItem issign = ConditionFactory.createCondition(ConditionType.SWITCH,-15947,"issign");
        SearchConditionItem istrigger = ConditionFactory.createCondition(ConditionType.SWITCH,"83023,-15948,15148","istrigger");
        SearchConditionItem issplit = ConditionFactory.createCondition(ConditionType.SWITCH,"83023,115,18215","issplit");
       //获取提醒类型
        String noticeType = Util.null2String(category.get("triggerType"));
        List<SearchConditionOption> noticeTypeOps = new ArrayList<SearchConditionOption>();
        noticeTypeOps.add(new SearchConditionOption("1", SystemEnv.getHtmlLabelName(17586, user.getLanguage()),"1".equals(noticeType)?true:false));
        noticeTypeOps.add(new SearchConditionOption("2",SystemEnv.getHtmlLabelName(32812, user.getLanguage()),"2".equals(noticeType)?true:false));
        noticeTypeOps.add(new SearchConditionOption("3",SystemEnv.getHtmlLabelName(124902, user.getLanguage()),"3".equals(noticeType)?true:false));
        SearchConditionItem triggerType = ConditionFactory.createCondition(ConditionType.SELECT, 18713, "triggerType", noticeTypeOps);
//        Map otherParams = new HashMap();
//        otherParams.put("formItemType","SELECT");
//        otherParams.put("value","1,2,3");
        //triggerType.setFormItemType("SELECT");
        triggerType.setDetailtype(2);
        triggerType.setValue(noticeType);
        //赋默认值
        setLayOut(name,6,11);
        setLayOut(parentName,6,11);
        setLayOut(describe,6,11);
//        setLayOut(isused,6,11);
//        setLayOut(isauto,6,11);
//        setLayOut(issign,6,11);
//        setLayOut(istrigger,6,11);
//        setLayOut(issplit,6,11);
//        setLayOut(triggerType,6,11);
        //setColspan 没有用
        name.setColSpan(1);
        parentName.setColSpan(1);
        describe.setColSpan(1);
        isused.setColSpan(1);
        isauto.setColSpan(1);
        issign.setColSpan(1);
        istrigger.setColSpan(1);
        issplit.setColSpan(1);
        triggerType.setColSpan(1);
        name.setValue(Util.null2String(category.get("name")));
        isused.setValue(Util.null2String(category.get("isused")));
        isauto.setValue(Util.null2String(category.get("isauto")));
        issign.setValue(Util.null2String(category.get("issign")));
        istrigger.setValue(Util.null2String(category.get("istrigger")));
        issplit.setValue(Util.null2String(category.get("issplit")));
        parentName.setValue(Util.null2String(category.get("parentName")));
        parentName.setViewAttr(1);
        describe.setValue(Util.null2String(category.get("describe")));
        editItem.add(name);
        editItem.add(parentName);
        editItem.add(triggerType);
        editItem.add(describe);

        editItem.add(isused);
        editItem.add(isauto);
        editItem.add(issign);
        editItem.add(istrigger);
        editItem.add(issplit);

        parentIds.add(Util.null2String(params.get("id")));
        apidatas.put("id",Util.null2String(category.get("id")));
        apidatas.put("editGroup",editgroup);
        apidatas.put("parentid",category.get("parentid"));
        apidatas.put("parentIds",parentIds);
        return apidatas;
    }

    public void setLayOut(SearchConditionItem conditionItem,int labelcol,int fieldcol){
        conditionItem.setLabelcol(labelcol);
        conditionItem.setFieldcol(fieldcol);
    }
}
