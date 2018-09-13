package com.engine.govern.cmd.category;




import com.api.browser.bean.SearchConditionGroup;
import com.api.browser.bean.SearchConditionItem;
import com.api.browser.util.ConditionFactory;
import com.api.browser.util.ConditionType;
import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.CategoryReadDao;

import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/27 16:00
 * @Description:获取添加督办类型的condition
 */
public class GetAddConditonCmd extends AbstractCommonCommand<Map<String,Object>> {

    public GetAddConditonCmd(User user, Map<String,Object> params) {
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
        //１．添加督办类型的condition
        List<SearchConditionGroup> addgroup =  new ArrayList<>();
        List<SearchConditionItem> addItem = new ArrayList<>();
        addgroup.add(new SearchConditionGroup("基本信息",true,addItem));

        SearchConditionItem name = (ConditionFactory.createCondition(ConditionType.INPUT,15795,"name"));

        setLayOut(name,6,11);
        name.setColSpan(1);
        addItem.add(name);
        SearchConditionItem isused =ConditionFactory.createCondition(ConditionType.SWITCH,18624,"isused");

        setLayOut(isused,6,11);
        isused.setColSpan(1);
        addItem.add(isused);
        SearchConditionItem supType = ConditionFactory.createCondition(ConditionType.INPUT,"596,21223,63","supType");
        supType.setViewAttr(1);

        setLayOut(supType,6,11);
        supType.setColSpan(1);
        //新建同级
        if("1".equals(Util.null2String(params.get("flag")))){
            supType.setValue(category.get("parentName"));
            Integer parentId = (Integer) category.get("parentid");
            //Integer pd = -1;
            apidatas.put("parentid",parentId);
           // apidatas.put("parentid",pd.equals(parentId)?null:category.get("parentid"));//当为null时，json没有parentid
        }
        //新建下级
        if("2".equals(Util.null2String(params.get("flag")))){
            supType.setValue(category.get("name"));
            apidatas.put("parentid",category.get("id"));
        }
        addItem.add(supType);
        SearchConditionItem describe = ConditionFactory.createCondition(ConditionType.TEXTAREA,"23883,433","describe");
        describe.setColSpan(1);
        setLayOut(describe,6,11);
        addItem.add(describe);

        apidatas.put("addGroup",addgroup);
        apidatas.put("addTitle", SystemEnv.getHtmlLabelNames("82,21223,63",user.getLanguage()));//弹出框的最上面的titel

        return apidatas;
    }

    public void setLayOut(SearchConditionItem conditionItem,int labelcol,int fieldcol){
        conditionItem.setLabelcol(labelcol);
        conditionItem.setFieldcol(fieldcol);
    }
}
