package com.engine.govern.cmd.category;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.write.CategoryWriteDao;

import weaver.common.StringUtil;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 14:35
 * @Description:保存督办类型（新建和修改）
 */
public class SaveCategoryCmd extends AbstractCommonCommand<Map<String,Object>> {

    public SaveCategoryCmd(Map<String, Object> params, User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        CategoryWriteDao CategoryWriteDao = new CategoryWriteDao();
        String id = Util.null2String(params.get("id"));
        Map<String,Object> map = new HashMap<>();
        if(StringUtil.isNotNull(id)){
            map = CategoryWriteDao.updateCategory(params);//修改
        }else {
            map = CategoryWriteDao.addCategory(params);//新增
        }
        return map;
    }
}
