package com.engine.govern.dao.write;



import com.engine.govern.dao.read.CategoryReadDao;
import org.springframework.transaction.annotation.Transactional;
import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/20 14:40
 * @Description:增删改督办类型Dao
 */
public class CategoryWriteDao {

    public Map<String,Object> addCategory(Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        TriggerWriteDao triggerWriteDao = new TriggerWriteDao();
        String parentid = (String) params.get("parentid");
        String name =(String)(params.get("name"));
        String isused = (String)(params.get("isused"));
        String isauto =(String)(params.get("isauto"));
        String issign =(String)(params.get("issign"));
        String istrigger =(String)(params.get("istrigger"));
        String issplit =(String)(params.get("issplit"));
        String triggerType =(String)(params.get("triggerType"));
        String describe = (String)(params.get("describe"));
        int id = -1;
        try{
            String sql = "insert into govern_category(name,superior,isused,isauto,issign,istrigger,issplit,triggerType,description) values(?,?,?,?,?,?,?,?,?) ";
            RecordSet rs = new RecordSet();
            rs.executeUpdate(sql,name,parentid,isused,isauto,issign,istrigger,issplit,triggerType,describe);
            String sql2 = "select IDENT_CURRENT('govern_category') as id  ";
            rs.executeQuery(sql2);
            if(rs.next()){
                id = rs.getInt("id");
                triggerWriteDao.initActionSetting(id);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(id!=-1){
            map.put("id",id);
            CategoryReadDao categoryReadDao = new CategoryReadDao();
            List<String> parentIds = categoryReadDao.getParentIds(String.valueOf(id));
           // parentIds.add(String.valueOf(id));
            map.put("parentIds",parentIds);//让前端添加的时候好默认展开左侧树
        }else {
            map.put("id","");
        }
        return map;
    }

    public  String toNull(Object s) {
        return "".equals(s) ?null : (String) s;

    }

    @Transactional
    public Map<String,Object> deleteCategory(Map<String,Object> params){
        Map<String,Object> apidatas = new HashMap<>();
        String ids = Util.null2String(params.get("ids"));
        RecordSet recordSet = new RecordSet();
        CategoryReadDao categoryReadDao = new CategoryReadDao();
        TriggerWriteDao triggerWriteDao = new TriggerWriteDao();
        boolean flag = false;
        List<String> parentIds = new ArrayList<>();
        String parentid = "";
        for(String id :ids.split(",")){
            //获取该节点的所有下级节点
            if(!"-1".equals(id)){
            if(!flag){
              parentIds = categoryReadDao.getParentIds(id);//获取id的所有父级节点，因为父级节点都一样所以只用调用一次
              parentid = categoryReadDao.getParentId(id,recordSet);
              flag = true;
            }
             int count = (int) categoryReadDao.getChildNode(id).get("count");
             if (count>0){
                 //删除子节点（没有加入事物控制）
                 List<String> childNodes = (List<String>) categoryReadDao.getChildNode(id).get("childNodes");
                 for(String cs: childNodes){
                     recordSet.executeUpdate("delete from govern_category where id =?",cs);
                     triggerWriteDao.deleteTriggerSettingInfo(cs);
                 }
             }
                 recordSet.executeUpdate("delete from govern_category where id =?",id);
                 triggerWriteDao.deleteTriggerSettingInfo(id);
             }

        }

        apidatas.put("success","true");
        apidatas.put("parentIds",parentIds);
        apidatas.put("parentid",parentid);
        return apidatas;
    }



    public Map<String,Object> updateCategory(Map<String,Object> params){
        Map<String,Object> map = new HashMap<>();
        //String parentid = Util.null2String( params.get("parentid"));
        String name = Util.null2String(params.get("name"));
        String isused = Util.null2String(params.get("isused"));
        String isauto =Util.null2String(params.get("isauto"));
        String issign =Util.null2String(params.get("issign"));
        String istrigger =Util.null2String(params.get("istrigger"));
        String issplit =Util.null2String(params.get("issplit"));
        String triggerType =Util.null2String(params.get("triggerType"));
        String id = Util.null2String(params.get("id"));
        String describe = (String)params.get("describe");
        String sql = "";
        if(StringUtil.isNotNull(id)){
            StringBuffer sqlParam = new StringBuffer();
            //sqlParam.append(!"".equals(parentid)?"parentid='"+parentid+"',":"");
            sqlParam.append(!"".equals(name)?"name='"+name+"',":"");
            sqlParam.append(!"".equals(isused)?"isused='"+isused+"',":"");
            sqlParam.append(!"".equals(isauto)?"isauto='"+isauto+"',":"");
            sqlParam.append(!"".equals(issign)?"issign='"+issign+"',":"");
            sqlParam.append(!"".equals(istrigger)?"istrigger='"+istrigger+"',":"");
            sqlParam.append(!"".equals(issplit)?"issplit='"+issplit+"',":"");
            sqlParam.append(!"".equals(triggerType)?"triggerType='"+triggerType+"',":"");
            sqlParam.append(!"".equals(describe)?"description='"+describe+"',":"");
            String updateSql = sqlParam.toString();
            if(updateSql.endsWith(","))
                updateSql = updateSql.substring(0,updateSql.length()-1);
            sql = "update govern_category set " + updateSql + " where id=?";


        }
        RecordSet rs = new RecordSet();
        try{

            rs.executeUpdate(sql,id);

        }catch (Exception e){
            e.printStackTrace();
            rs.writeLog("updateCategory",e.getMessage());
        }
        CategoryReadDao categoryReadDao = new CategoryReadDao();
        List<String> parentIds = categoryReadDao.getParentIds(id);
        map.put("id",id);
        map.put("parentIds",parentIds);
        return map;
    }

}
