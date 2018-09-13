package com.engine.govern.cmd.triggerSetting;

import com.engine.common.biz.AbstractCommonCommand;
import com.engine.common.entity.BizLogContext;
import com.engine.core.interceptor.CommandContext;
import com.engine.govern.dao.read.TriggerReadDao;
import com.engine.govern.dao.write.TriggerWriteDao;
import com.engine.govern.entity.ResponseActionSetting;
import org.springframework.transaction.annotation.Transactional;
import weaver.hrm.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/5 10:58
 * @Description:保存触发设置
 */
public class SaveTriggerCmd extends AbstractCommonCommand<Map<String,Object>> {

    public SaveTriggerCmd(Map<String,Object> params,User user) {
        this.params = params;
        this.user = user;
    }

    @Override
    public BizLogContext getLogContext() {
        return null;
    }

    @Override
    @Transactional
    public Map<String,Object> execute(CommandContext commandContext) {
        Map<String,Object> result = new HashMap<>();
        Boolean flag1 = false;
        Boolean flag2 = false;
        Boolean flag3 = false;
        TriggerWriteDao triggerWriteDao= new TriggerWriteDao();
        TriggerReadDao triggerReadDao = new TriggerReadDao();
        ResponseActionSetting biscInfo =triggerReadDao.getBiscInfo(params);
        flag1 = (Boolean) triggerWriteDao.saveActionSetting(params).get("success");
        if ("0".equals(biscInfo.getTriggerType())){
            if(flag1){
                result.put("success",true);
            }else {
                result.put("success",false);
            }
        }
        if("1".equals(biscInfo.getTriggerType())){
            flag2 = (Boolean) triggerWriteDao.saveTriggerSetting(params).get("success");
            flag3 = (Boolean) triggerWriteDao.saveActionConfig(params).get("success");
            if(flag1&&flag2&&flag3){
                result.put("success",true);
            }else {
                result.put("success",false);
            }
        }

        return result;
    }
}
