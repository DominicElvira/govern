package com.engine.govern.cmd.triggerSetting;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.TriggerReadDao;
import weaver.general.Util;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/5 17:48
 * @Description:判断触发方式
 */
public class GetTriggerTypeCmd extends AbstractCommonCommand<Map<String,Object>> {

    public GetTriggerTypeCmd(Map<String,Object> params,User user ) {
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
     * @date 2018/9/5 18:28
     * @param categoryId、type
     * @return
     */

    @Override
    public Map<String, Object> execute(CommandContext commandContext) {
        TriggerReadDao triggerReadDao = new TriggerReadDao();
        Map<String,Object>  actionSetting = triggerReadDao.getActionSetting(Util.null2String(params.get("categoryId")),Util.null2String(params.get("type")));//获得触发方式
        return actionSetting;
    }
}
