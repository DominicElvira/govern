package com.engine.govern.cmd.category;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.CategoryReadDao;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/29 11:11
 * @Description:
 */
public class GetChildNodesCmd extends AbstractCommonCommand<Map<String,Object>>{
    public GetChildNodesCmd(User user,Map<String,Object> params) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String,Object> reslut = new HashMap<>();
        CategoryReadDao  categoryReadDao = new CategoryReadDao();
        String ids = Util.null2String(params.get("ids"));
        int count = 0;
        for (String id : ids.split(",")){
            Map<String,Object> childNodes = categoryReadDao.getChildNode(id);
            int count2 = (int) childNodes.get("count");
            if(count2>0){
                count = count2+count;
            }
        }
        reslut.put("count",count);
        return  reslut;
    }
}
