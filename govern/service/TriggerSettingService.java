package com.engine.govern.service;

import weaver.hrm.User;

import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/3 14:06
 * @Description:触发操作设置
 */
public interface TriggerSettingService {

    //获取设置触发操作的condition（废弃）
   // Map<String,Object> getTriggerCondition(Map<String,Object> params,User user);

    Map<String,Object> getTriggerCondition(Map<String,Object> params,User user);

    Map<String,Object> getBasicCondition(Map<String,Object> params,User user);

    Map<String,Object> getfiledCondition(Map<String,Object> params,User user);

    Map<String,Object> getTriggerType(Map<String,Object> params,User user);

    Map<String,Object> getActionConditon(Map<String,Object> params,User user);

    Map<String,Object> saveTriggerSetting(Map<String,Object> params,User user);

}
