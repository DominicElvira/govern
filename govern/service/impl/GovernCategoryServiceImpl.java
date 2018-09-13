package com.engine.govern.service.impl;

import com.engine.core.impl.Service;
//import com.engine.govern.cmd.category.*;
import com.engine.govern.cmd.category.*;
import com.engine.govern.service.GovernCategoryService;
import weaver.hrm.User;

import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 12:28
 * @Description:督办类型接口实现类
 */
public class GovernCategoryServiceImpl  extends Service  implements GovernCategoryService{

    @Override
    public Map<String, Object> getCategory(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetCategoryCmd(params,user));
    }

    @Override
    public Map<String, Object> saveCategory(Map<String, Object> params, User user) {
        return commandExecutor.execute(new SaveCategoryCmd(params, user));
    }

    @Override
    public Map<String, Object> getAddConditon(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetAddConditonCmd(user,params));
    }

    @Override
    public Map<String, Object> getEditCondition(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetEditConditionCmd(user,params));
    }

    @Override
    public Map<String, Object> deleteCategory(Map<String, Object> params, User user) {
        return commandExecutor.execute(new DeleteCategoryCmd(user,params));
    }

    @Override
    public Map<String, Object> getChildNodes(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetChildNodesCmd(user,params));
    }

    @Override
    public Map<String,Object> getCategoryTree(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetCategoryTreeCmd(params, user));
    }

    @Override
    public Map<String, Object> getCategoryChild(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetCategoryChildCmd(params, user));
    }
}
