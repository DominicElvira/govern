package com.engine.govern.service;

import weaver.hrm.User;

import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 12:01
 * @Description: 督办类型接口
 */
public interface GovernCategoryService {
    //根据数据id获取相应的类型
    Map<String,Object> getCategory(Map<String, Object> params, User user);

    //保存督办类型
    Map<String,Object> saveCategory(Map<String,Object> params, User user);

    //获取添加督办类型condition
    Map<String,Object> getAddConditon(Map<String,Object> params,User user);

    //获取编辑督办类型condition
    Map<String,Object> getEditCondition(Map<String,Object> params,User user);

    //删除督办类型
    Map<String,Object> deleteCategory(Map<String,Object> params,User user);

    //删除时判断是否有下级督办类型
    Map<String,Object> getChildNodes(Map<String,Object> params,User user);

    Map<String,Object> getCategoryTree(Map<String, Object> params, User user);

    Map<String,Object> getCategoryChild(Map<String, Object> params, User user);

}
