package com.engine.govern.service.impl;

import com.engine.core.impl.Service;
import com.engine.govern.cmd.triggerSetting.*;
import com.engine.govern.service.TriggerSettingService;
import org.springframework.transaction.annotation.Transactional;
import weaver.hrm.User;

import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/3 14:42
 * @Description:
 */
public class TriggerSettingServiceImpl extends Service implements TriggerSettingService{

    @Override
    public Map<String, Object> getTriggerCondition(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetTriggerConditionCmd(params,user));
    }

    @Override
    public Map<String, Object> getBasicCondition(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetBasicConditionCmd(params,user));
    }

    @Override
    public Map<String, Object> getfiledCondition(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetfiledConditionCmd(params,user));
    }

    @Override
    public Map<String, Object> getTriggerType(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetTriggerTypeCmd(params,user));
    }

    @Override
    public Map<String, Object> getActionConditon(Map<String, Object> params, User user) {
        return commandExecutor.execute(new GetActionConditionCmd(params,user));
    }

    @Override
    public Map<String, Object> saveTriggerSetting(Map<String, Object> params, User user) {
        return commandExecutor.execute(new SaveTriggerCmd(params,user));
    }


}
