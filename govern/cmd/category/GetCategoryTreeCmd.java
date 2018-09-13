package com.engine.govern.cmd.category;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.cube.biz.RightHelper;
import com.engine.govern.dao.read.CategoryReadDao;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 督办类型树获取接口cmd
 * @author: lei.wu
 * @create: 2018-08-20
 */
public class GetCategoryTreeCmd extends AbstractCommonCommand<Map<String, Object>> {

    public GetCategoryTreeCmd(Map<String, Object> params, User user){
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        Map<String, Object> result = new HashMap<String, Object>();
        CategoryReadDao governCategoryReadDao = new CategoryReadDao();
        // TODO Auto-generated method stub  权限控制
        if (!RightHelper.checkBackRight("FORMMODEAPP:ALL", user, result)) {
            return result;
        }
        List<Map<String, Object>> map = governCategoryReadDao.getCategoryTree(params,user);
        result.put("data", map);
        return result;
    }
}
