package com.engine.govern.dao.read;

import com.api.browser.bean.Operate;
import com.api.browser.bean.SplitTableBean;
import com.api.browser.bean.SplitTableColBean;
import com.api.browser.bean.SplitTableOperateBean;
import com.api.browser.util.SplitTableUtil;
import com.api.workflow.util.PageUidFactory;
import com.cloudstore.dev.api.util.Util_TableMap;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.systeminfo.SystemEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 12:36
 * @Description:
 */
public class CategoryReadDao {

    public Map<String,Object> getCategory(String id){
        Map<String,Object> category = new HashMap<String,Object>();
        Map<String,Object> result = new HashMap<>();
        RecordSet rs = new RecordSet();
        try{
            String sql ="SELECT [id]" +
                    "      ,[name]" +
                    "      ,[isused]" +
                    "      ,[isauto]" +
                    "      ,[issign]" +
                    "      ,[istrigger]" +
                    "      ,[triggerType]" +
                    "      ,[issplit]" +
                    "      ,[superior]" +
                    "      ,[description]" +
                    ",(select name from govern_category where id= a.superior) as parentName "+
                    "  FROM govern_category a where 1=1";
//            if("-1".equals(Util.null2String(id))){
//                sql = sql +" and a.id = (select min(id) from govern_category)";
//                rs.executeQuery(sql);
//            }else {
                sql = sql +" and a.id = ?";
                rs.executeQuery(sql , id);
//            }

            if(rs.next()){
                String parentName = Util.null2String(rs.getString("parentName"));
                String name = Util.null2String(rs.getString("name"));
                String describe = Util.null2String(rs.getString("description"));
                int isused = convertZero(rs.getInt("isused"));
                int isauto = convertZero(rs.getInt("isauto"));
                int issign = convertZero(rs.getInt("issign"));
                int istrigger = convertZero(rs.getInt("istrigger"));
                int issplit = convertZero(rs.getInt("issplit"));
                String triggerType =rs.getString("triggerType");
                int superior = rs.getInt("superior");
                int cid = rs.getInt("id");
                category.put("id", cid);
                category.put("name", name);
                category.put("parentName", parentName);
                category.put("describe",describe);
                category.put("isused", isused);
                category.put("isauto", isauto);
                category.put("issign", issign);
                category.put("istrigger", istrigger);
                category.put("issplit", issplit);
                category.put("triggerType", triggerType);
                category.put("parentid",superior);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        result.put("data",category);
        return result;
    }

    public int convertZero(int One){
        if(One == -1){
            return 0;
        }
        return One;
    }
    /**
     * 获取督办类型树
     * @param params
     * @param user
     * @return
     */
    public List<Map<String,Object>> getCategoryTree(Map<String, Object> params, User user) {
        String sql = "select id,name,isused,isauto,issign,istrigger,triggerType,issplit,superior from govern_category where superior is null or superior = 0";
        List<Map<String, Object>> categotyData = getResultByList(sql,"1");//获取最上层节点
        String querySql = "SELECT id,name,isused,isauto,issign,istrigger,triggerType,issplit,superior from govern_category where  superior is not null and superior !=0";
        List<Map<String, Object>> childrenCategory = getResultByList(querySql,"0");//获取所有的子节点
        Map<String,List<Map<String, Object>>> id2Children = new HashMap<String,List<Map<String, Object>>>();//每个督办 对应的  下级督办

        for(Map<String, Object> obj :childrenCategory){
            //获取子节点的父节点
            String parentid = String.valueOf(obj.get("parentid"));
            if(id2Children.get(parentid)==null){
                List<Map<String, Object>> childObj = new ArrayList<>();//子节点列表childs
                childObj.add(obj);
                id2Children.put(parentid,childObj);
            }else{
                id2Children.get(parentid).add(obj);
            }
        }
        //
        for (Map<String,Object> obj :childrenCategory){
            String id = String.valueOf(obj.get("id"));
            if (id2Children.get(id)!=null) {
                obj.put("haschild", true);
                obj.put("childs", id2Children.get(id));
            }else {
                obj.put("haschild",false);
            }
        }

        for(Map<String, Object> obj :categotyData){
            String id = String.valueOf(obj.get("id"));
            if(id2Children.get(id)!=null){
                obj.put("haschild",true);
                obj.put("childs",id2Children.get(id));
            }
        }
        return categotyData;

    }


    /**
     * 按照组件库的左侧树格式返回json
     */
    public List<Map<String,Object>> getResultByList(String sql,String type) {
        List<Map<String, Object>> categotyData = new ArrayList<>();
        RecordSet rs = new RecordSet();
        rs.executeQuery(sql);
        while(rs.next()) {
            Map<String, Object> map = new HashMap<>();
            if("1".equals(type)) {
                map.put("domid","type_" + weaver.general.Util.null2String(rs.getString("id")));
            } else {
                map.put("domid","wf_" + weaver.general.Util.null2String(rs.getString("id")));
//                map.put("haschild",false);
                map.put("parentid", weaver.general.Util.null2String(rs.getString("superior")));
            }
            map.put("isopen",false);
            map.put("key", weaver.general.Util.null2String((rs.getString("id"))));
            map.put("name", weaver.general.Util.null2String(rs.getString("name")));
            map.put("id", weaver.general.Util.null2String(rs.getString("id")));
            categotyData.add(map);
        }
        return categotyData;
    }

    /**
     * 按照组件库的格式获取下级督办类型(为分页数据，sessionKey)
     * @param params
     * @param user
     * @return
     */
    public List<Map<String, Object>> getCategoryChild2(Map<String, Object> params, User user) {
        Map<String,Object> apidatas = new HashMap<String,Object>();

        String fromsql = "govern_category";
        String backfields = "id,name,isused ,isauto,issign,istrigger,triggerType,issplit,superior";
        String sqlwhere = " where 1 = 1 and superior = " + params.get("id");
        String orderby = " id asc";
        String pageUid  = PageUidFactory.getWfPageUid("reportCompetenceSet");

        SplitTableOperateBean operateEle = new SplitTableOperateBean();
        List<Operate> operates = new ArrayList<Operate>();
        Operate operate1 = new Operate(SystemEnv.getHtmlLabelName(91, user.getLanguage()),"javascript:delete()","0");
        operates.add(operate1);
        operateEle.setOperate(operates);

        List<SplitTableColBean> cols = new ArrayList<SplitTableColBean>();
        cols.add(new SplitTableColBean("true","id"));
        cols.add(new SplitTableColBean("25%","督办类型","name","id"));
        cols.add(new SplitTableColBean("25%","是否启用","isused","isused","com.engine.govern.biz.CategoryTransMethod.isusedTrans"));
        cols.add(new SplitTableColBean("25%","是否自动下发","isauto","isauto","com.engine.govern.biz.CategoryTransMethod.isusedTrans"));
        cols.add(new SplitTableColBean("25%","是否需要签收","issign","issign","com.engine.govern.biz.CategoryTransMethod.isusedTrans"));
        cols.add(new SplitTableColBean("25%","是否需要提醒","istrigger","istrigger","com.engine.govern.biz.CategoryTransMethod.isusedTrans"));
        cols.add(new SplitTableColBean("25%","提醒方式","triggerType","triggerType","com.engine.govern.biz.CategoryTransMethod.noticeTypeTrans"));
        cols.add(new SplitTableColBean("25%","是否允许分解","issplit","issplit","com.engine.govern.biz.CategoryTransMethod.isusedTrans"));
        cols.add(new SplitTableColBean("25%","父级ID","superior","superior"));

        SplitTableBean tableBean  =  new SplitTableBean(backfields,fromsql,sqlwhere,orderby,"id",cols);
        tableBean.setPageUID(pageUid);
        tableBean.setTableType("checkbox");
        tableBean.setSqlisdistinct("true");
        tableBean.setOperates(operateEle);

        String tableString = SplitTableUtil.getTableString(tableBean);
        String sessionkey = pageUid +"_"+ weaver.general.Util.getEncrypt(weaver.general.Util.getRandom());
        Util_TableMap.setVal(sessionkey, tableString);
        apidatas.put("sessionkey", sessionkey);
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(apidatas);
        return list;
    }

    /*
    * 判断该节点是否拥有下级节点，并返回对应所有下级节点id
    * */
    public Map<String,Object> getChildNode(String id){
        Map<String ,Object> result = new HashMap<>();
        List<String> childNodes2 = new ArrayList();
        RecordSet rs = new RecordSet();
        childNodes2 = circle(childNodes2,rs,id);
        int count = childNodes2.size();
        result.put("count",count);
        result.put("childNodes",childNodes2);
        return result;
    }

    public List<String> circle(List<String>childNodes2,RecordSet rs,String id){
        List<String> childNodes = new ArrayList();
        String sql = "select id from govern_category where superior = ?";
        rs.executeQuery(sql,id);
        while(rs.next()){
            childNodes.add(String.valueOf(rs.getInt("id")));
        }
        if(childNodes.size()>0){
            for(String cns :childNodes){
            //    childNodes2.add("-");//该节点结束记得删除注意一下
                childNodes2.add(cns);
                 circle(childNodes2,rs,cns);
//                childNodes2.add("-");
            }
        }
        return childNodes2;
    }

    /**
     * 返回该节点的所有上级节点
     */
    public List<String> getParentIds(String id){
        List<String> parentIds = new ArrayList<>();
        RecordSet rs = new RecordSet();
        //1.获取该节点的上级节点
        String parentId = getParentId(id,rs);
        parentIds.add(parentId);
        //2.获取所有节点id
        Map<String,String> ids = getIds(rs);
        //3.循环比较查看是否parentId在Id里
        while(ids.get(parentId)!=null){
            parentId = getParentId(parentId,rs);
            if(!"-1".equals(parentId)){
                parentIds.add(parentId);
            }else {
                break;
            }
        }
        parentIds.add("-1");
        return  parentIds;
    }

    //获取父节点
    public String getParentId(String id,RecordSet recordSet){
        String sql = "select superior from govern_category where id = ?";
        recordSet.executeQuery(sql,id);
        if(recordSet.next()){
           return String.valueOf(recordSet.getInt("superior"));
        }
        return "";
    }
    //获取所有节点id
    public Map<String,String> getIds(RecordSet recordSet){
        Map<String,String> ids = new HashMap<>();
        String sql = "select id from govern_category where  1=1";
        recordSet.executeQuery(sql);
        while(recordSet.next()){
            String id = String.valueOf(recordSet.getInt("id"));
            ids.put(id,id);
        }
        return ids;
    }


}
