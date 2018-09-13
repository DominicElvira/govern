package com.engine.govern.web;

import com.alibaba.fastjson.JSONObject;
import com.engine.common.util.ParamUtil;
import com.engine.common.util.ServiceUtil;
import com.engine.govern.service.GovernCategoryService;
import com.engine.govern.service.TriggerSettingService;
import com.engine.govern.service.impl.GovernCategoryServiceImpl;
import com.engine.govern.service.impl.TriggerSettingServiceImpl;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 14:05
 * @Description:督办类型入口
 */
public class GovernCategoryAction {
    private GovernCategoryService getService(HttpServletRequest request, HttpServletResponse response){
        User user = HrmUserVarify.getUser(request, response);
        return (GovernCategoryServiceImpl) ServiceUtil.getService(GovernCategoryServiceImpl.class,user);
    }

    private TriggerSettingService triggerSettingService(HttpServletRequest request, HttpServletResponse response){
        User user = HrmUserVarify.getUser(request, response);
        return (TriggerSettingServiceImpl) ServiceUtil.getService(TriggerSettingServiceImpl.class,user);
    }
    /**
     * 根据数据id查询督办类型
     * @param request
     * @return
     */
    @GET
    @Path("/getGovernCategory")
    @Produces(MediaType.TEXT_PLAIN)
    public String getGovernCategory(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(request, response).getCategory(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("status", "0");
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 保存督办类型
     * @param request
     * @return
     */
    @POST
    @Path("/saveGovernCategory")
    @Produces(MediaType.TEXT_PLAIN)
    public String saveGovernCategory(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(request, response).saveCategory(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("status", "0");
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 添加时form的condition
     * @param request
     * @return
     */
    @POST
    @Path("/getAddCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAddCondition(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(request, response).getAddConditon(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("status", "0");
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 修改时form的condition
     * @param request
     * @return
     */
    @GET
    @Path("/getEditCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEditCondition(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(request, response).getEditCondition(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("status", "0");
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 删除督办类型
     * @param request
     * @return
     */
    @POST
    @Path("/deleteCategory")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteCategory(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(request, response).deleteCategory(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            e.printStackTrace();
            apidatas.put("status", "0");
            apidatas.put("api_errormsg", "catch exception : " + e.getMessage());
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     * 获取督办类型树
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/getCategoryTree")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCategoryTree(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            Map<String, Object> parmMap=ParamUtil.request2Map(request);
            apidatas.put("categotyData",getService(request,response).getCategoryTree(parmMap,user).get("data"));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     *获取下级督办类型
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/getCategoryChild")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCategoryChild(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser (request , response) ;
            Map<String, Object> parmMap=ParamUtil.request2Map(request);
            apidatas.put("categotyList",getService(request,response).getCategoryChild(parmMap,user).get("data"));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    /**
     *判断是否有下级督办类型
     * @param request
     * @param response
     * @return
     */
    @GET
    @Path("/getChildNodes")
    @Produces(MediaType.TEXT_PLAIN)
    public String getChildNodes(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(getService(request, response).getChildNodes(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    /*
     *  获取督办类型的触发condition
     * @author 谢凯
     * @date 2018/9/4 11:27
     * @param categoryId、type
     * @return
     */
    @GET
    @Path("/getTriggerCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTriggerCondition(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(triggerSettingService(request, response).getTriggerCondition(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    /*
     *  获取基本信息condition
     * @author 谢凯
     * @date 2018/9/5 19:20
     * @param
     * @return
     */

    @GET
    @Path("/getBasicCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBasicCondition(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(triggerSettingService(request, response).getBasicCondition(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }
    /*
     *  获取字段设置condition
     * @author 谢凯
     * @date 2018/9/5 19:04
     * @param billid、categoryId、type
     * @return
     */

    @GET
    @Path("/getfiledCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getfiledCondition(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(triggerSettingService(request, response).getfiledCondition(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    /*
     *获取动作设置
     * @author 谢凯
     * @date 2018/9/5 19:03
     * @param categoryId、type
     * @return
     */

    @GET
    @Path("/getActionCondition")
    @Produces(MediaType.TEXT_PLAIN)
    public String getActionCondition(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(triggerSettingService(request, response).getActionConditon(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    /*
     *判断触发方式
     * @author 谢凯
     * @date 2018/9/5 19:03
     * @param categoryId、type
     * @return
     */

    @GET
    @Path("/getTriggerType")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTriggerType(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            Map<String,Object> map = triggerSettingService(request, response).getTriggerType(ParamUtil.request2Map(request),user);
            if(map == null || map.size() == 0){
                apidatas.put("status", "-1");
            }else {
                apidatas.putAll(map);
                apidatas.put("status", "1");
            }
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

    @POST
    @Path("/SaveTrigger")
    @Produces(MediaType.TEXT_PLAIN)
    public String SaveTrigger(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        //response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        Map<String, Object> apidatas = new HashMap<String, Object>();
        try {
            User user = HrmUserVarify.getUser(request, response);
            apidatas.putAll(triggerSettingService(request, response).saveTriggerSetting(ParamUtil.request2Map(request),user));
            apidatas.put("status", "1");
        } catch (Exception e) {
            apidatas.put("status", "-1");
        }
        return JSONObject.toJSONString(apidatas);
    }

}
