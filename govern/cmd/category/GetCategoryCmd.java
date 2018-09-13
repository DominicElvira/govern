package com.engine.govern.cmd.category;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.CategoryReadDao;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 12:31
 * @Description:获取督办类型
 */
public class GetCategoryCmd extends AbstractCommonCommand<Map<String , Object>>{

    public GetCategoryCmd(Map<String, Object> params, User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        CategoryReadDao CategoryReadDao = new CategoryReadDao();
        String id = Util.null2String(params.get("id"));//后台执行sql时不区分String还是int
        return CategoryReadDao.getCategory(id);
    }
}
