package com.engine.govern.cmd.category;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.write.CategoryWriteDao;
import weaver.hrm.User;

import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/28 17:00
 * @Description:删除督办类型
 */
public class DeleteCategoryCmd extends AbstractCommonCommand<Map<String,Object>>{
    public DeleteCategoryCmd(User user, Map<String,Object> params) {
        this.user = user;
        this.params = params;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    public Map<String,Object> execute(CommandContext commandContext) {
        CategoryWriteDao categoryWriteDao = new CategoryWriteDao();
        return categoryWriteDao.deleteCategory(params);
    }
}
