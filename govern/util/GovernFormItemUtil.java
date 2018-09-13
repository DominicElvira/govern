package com.engine.govern.util;

import com.api.browser.bean.SearchConditionOption;
import com.api.browser.util.BrowserConfigComInfo;
import com.api.prj.bean.PrjRightMenu;
import com.api.prj.bean.PrjRightMenuType;
import com.api.prj.service.BaseService;
import com.api.workflow.util.ServiceUtil;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.cpt.capital.CapitalComInfo;
import weaver.crm.Maint.CustomerInfoComInfo2;
import weaver.crm.investigate.ContacterComInfo;
import weaver.docs.docs.DocComInfo;
import weaver.docs.docs.DocImageManager;
import weaver.docs.senddoc.DocReceiveUnitComInfo;
import weaver.filter.XssUtil;
import weaver.formmode.tree.CustomTreeUtil;
import weaver.general.AttachFileUtil;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.browser.Browser;
import weaver.interfaces.workflow.browser.BrowserBean;
import weaver.proj.Maint.ProjectInfoComInfo;
import weaver.proj.Maint.ProjectStatusComInfo;
import weaver.proj.util.*;
import weaver.systeminfo.SystemEnv;
import weaver.workflow.field.BrowserComInfo;
import weaver.workflow.workflow.WorkflowAllComInfo;
import weaver.workflow.workflow.WorkflowRequestComInfo;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Auther: 谢凯
 * @Date: 2018/9/6 17:11
 * @Description:参考prjFormItemUtil
 */
public class GovernFormItemUtil {
    public static final String INPUT = "INPUT";
    public static final String SCOPE = "SCOPE";
    public static final String TEXTAREA = "TEXTAREA";
    public static final String HTMLTEXT = "RICHTEXT";
    public static final String BROWSER = "BROWSER";
    public static final String DATE = "DATE";
    public static final String CHECKBOX = "CHECKBOX";
    public static final String SWITCH = "SWITCH";
    public static final String SELECT = "SELECT";
    public static final String ATTACHEMENT = "ATTACHEMENT";
    public static final String HYPERLINK = "HYPERLINK";
    public static final String SELECT_LINKAGE = "SELECT_LINKAGE";
    public static final String DATEPICKER = "DATEPICKER";
    public static final String TIMEPICKER = "TIMEPICKER";
    public static final String PRJDATETIME = "PRJDATETIME";
    public static BaseBean baseBean = new BaseBean();
    public static String broswerTypes = ",17,18,37,57,135,152,162,194,257,";//多选
    public static String broswerTypes1 = ",274,25,";//没有高级搜索的
    private static final Pattern IMG_PATTERN = Pattern.compile(".*?\\.(png|gif|jpeg|jpg|bmp)",Pattern.CASE_INSENSITIVE);

    /**
     * 项目类型-编辑
     *
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getEditProjectTypeFormItems(User user, Map<String, Object> params){

        RecordSet rs = new RecordSet();
        String prjtypeid = Util.null2String(params.get("prjtypeid"));
        String fullname="";
        String wfid="";
        String description="";
        String protypecode="";
        String insertworkplan="";
        String dsporder="";
        String isprint="";

        if(!"".equals(prjtypeid)){
            rs.execute("select type.*,base.workflowname from Prj_ProjectType type left join Workflow_base base on type.wfid = base.id where type.id = "+prjtypeid);
            if(rs.next()){
                fullname= Util.null2String(rs.getString("fullname"));
                wfid= Util.null2String(rs.getString("wfid"));
                description= Util.null2String(rs.getString("description"));
                protypecode= Util.null2String(rs.getString("protypecode"));
                insertworkplan= Util.null2String(rs.getString("insertworkplan"));
                dsporder= Util.null2String(rs.getString("dsporder"));
                isprint = Util.null2String(rs.getString("isprint"));
            }
        }

        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = new ArrayList<Map<String, Object>>();
        Map<String, Object> formItemGroup = new HashMap<String, Object>();
        formItemGroup.put("title", SystemEnv.getHtmlLabelName(1361,user.getLanguage()));
        String fieldLabel = "";

        //项目类型名称
        fieldLabel = SystemEnv.getHtmlLabelName(15795,user.getLanguage());
        Map<String, Object> formItem = getFormItemForInput("typename",fieldLabel, fullname, 50, 3);
        formItems.add(formItem);

        //项目类型编码
        fieldLabel = SystemEnv.getHtmlLabelName(21942,user.getLanguage());
        formItem = getFormItemForInput("prjtypecode",fieldLabel, protypecode, 300, 2);
        formItems.add(formItem);

        //流程类型
        fieldLabel = SystemEnv.getHtmlLabelName(15057,user.getLanguage());
        Map<String,Object> dataParams = new HashMap<String,Object>();
        XssUtil xssUtil = new XssUtil();
        dataParams.put("sqlwhere", xssUtil.put(" and isbill=1 and ( formid=74 or exists(select 1 from prj_prjwfconf where prj_prjwfconf.isopen='1' and prj_prjwfconf.wftype=2 and prj_prjwfconf.wfid=a.id))"));
        formItem = getFormItemForBrowser("approvewfid", fieldLabel, "-99991", wfid, 2, "", null,dataParams);
        formItems.add(formItem);

        //是否工作计划
        fieldLabel = SystemEnv.getHtmlLabelName(33130,user.getLanguage());
        formItem = getFormItemForCheckbox("isWorkPlan", fieldLabel, insertworkplan, 2);
        formItems.add(formItem);

        //项目类型描述
        fieldLabel = SystemEnv.getHtmlLabelName(433,user.getLanguage());
        formItem = getFormItemForInput("description",fieldLabel, description, 150, 2);
        formItems.add(formItem);

        //项目类型序号
        fieldLabel = SystemEnv.getHtmlLabelName(15513,user.getLanguage());
        formItem = getFormItemForInput("dsporder", fieldLabel, dsporder, 20, 2 , 3, 2, null);
        formItems.add(formItem);

        //是否打印甘特图
        fieldLabel = SystemEnv.getHtmlLabelName(130260,user.getLanguage());
        formItem = getFormItemForCheckbox("isprint", fieldLabel, isprint, 2);
        formItems.add(formItem);

        formItemGroup.put("items", formItems);
        formItemGroup.put("defaultshow", true);
        formItemGroupList.add(formItemGroup);

        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("title", SystemEnv.getHtmlLabelName(83843, user.getLanguage()));
        return fieldinfo;
    }

    /**
     * 工作类型-编辑
     *
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getEditWorkTypeFormItems(User user, Map<String, Object> params){

        RecordSet rs = new RecordSet();
        String worktypeid = Util.null2String(params.get("worktypeid"));

        String fullname="";
        String description="";
        String worktypecode="";
        String dsporder="";

        if(!"".equals(worktypeid)){
            rs.executeProc("Prj_WorkType_SelectByID",worktypeid);
            if(rs.next()){
                fullname= Util.null2String(rs.getString("fullname"));
                description= Util.null2String(rs.getString("description"));
                worktypecode= Util.null2String(rs.getString("worktypecode"));
                dsporder= Util.null2String(rs.getString("dsporder"));
            }
        }

        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = new ArrayList<Map<String, Object>>();
        Map<String, Object> formItemGroup = new HashMap<String, Object>();
        formItemGroup.put("title", SystemEnv.getHtmlLabelName(1361,user.getLanguage()));
        String fieldLabel = "";

        //工作类型名称
        fieldLabel = SystemEnv.getHtmlLabelName(15795,user.getLanguage());
        Map<String, Object> formItem = getFormItemForInput("fullname",fieldLabel, fullname, 50, 3);
        formItems.add(formItem);

        //工作类型编码
        fieldLabel = SystemEnv.getHtmlLabelName(21942,user.getLanguage());
        formItem = getFormItemForInput("worktypecode",fieldLabel, worktypecode, 150, 2);
        formItems.add(formItem);

        //工作类型描述
        fieldLabel = SystemEnv.getHtmlLabelName(433,user.getLanguage());
        formItem = getFormItemForInput("description",fieldLabel, description, 150, 2);
        formItems.add(formItem);

        //工作类型序号
        fieldLabel = SystemEnv.getHtmlLabelName(15513,user.getLanguage());
        formItem = getFormItemForInput("dsporder", fieldLabel, dsporder, 20, 2 , 1, 0, null);
        formItems.add(formItem);

        formItemGroup.put("items", formItems);
        formItemGroup.put("defaultshow", true);
        formItemGroupList.add(formItemGroup);

        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("title", SystemEnv.getHtmlLabelName(83843, user.getLanguage()));
        return fieldinfo;
    }

    /**
     * 项目状态-编辑
     *
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getEditPrjStatusFormItems(User user, Map<String, Object> params){

        RecordSet rs = new RecordSet();
        String prjstatusid = Util.null2String(params.get("prjstatusid"));

        String description="";
        String dsporder="";
        String summary="";

        if(!"".equals(prjstatusid)){
            rs.executeProc("Prj_ProjectStatus_SelectByID",prjstatusid);
            if(rs.next()){
                description= Util.null2String(rs.getString("description"));
                summary= Util.null2String(rs.getString("summary"));
                dsporder= Util.null2String(rs.getString("dsporder"));
            }
        }else{
            rs.execute("select (max(dsporder)+1) as newdsporder  from Prj_ProjectStatus ");
            if(rs.next()){
                dsporder=""+Util.getDoubleValue(rs.getString("newdsporder"),0.0);
            }
        }

        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = new ArrayList<Map<String, Object>>();
        Map<String, Object> formItemGroup = new HashMap<String, Object>();
        formItemGroup.put("title", SystemEnv.getHtmlLabelName(1361,user.getLanguage()));
        String fieldLabel = "";

        //项目状态名称
        fieldLabel = SystemEnv.getHtmlLabelNames("602,195",user.getLanguage());
        Map<String, Object> formItem = getFormItemForInput("prjstatusname",fieldLabel, description, 50, 3);
        formItems.add(formItem);

        //项目状态描述
        fieldLabel = SystemEnv.getHtmlLabelName(433,user.getLanguage());
        formItem = getFormItemForInput("summary",fieldLabel, summary, 150, 2);
        formItems.add(formItem);

        //项目状态序号
        fieldLabel = SystemEnv.getHtmlLabelName(15513,user.getLanguage());
        formItem = getFormItemForInput("dsporder", fieldLabel, dsporder, 20, 2 , 1, 0, null);
        formItems.add(formItem);

        formItemGroup.put("items", formItems);
        formItemGroup.put("defaultshow", true);
        formItemGroupList.add(formItemGroup);

        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("title", SystemEnv.getHtmlLabelName(83843, user.getLanguage()));
        return fieldinfo;
    }

    /**
     * 项目模板-新建
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getAddPrjTempletFormItems(User user, Map<String, Object> params) throws Exception {

        PrjSettingsComInfo prjset = new PrjSettingsComInfo();
        int projTypeId = Util.getIntValue(Util.null2String(params.get("prjtypeid")),-1);

        PrjFieldComInfo prjFieldComInfo = new PrjFieldComInfo();
        TreeMap<String,TreeMap<String,JSONObject>> groupFieldMap=prjFieldComInfo.getGroupFieldMap(""+projTypeId);
        PrjCardGroupComInfo prjCardGroupComInfo = new PrjCardGroupComInfo();
        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = null;
        Map<String, Object> formItemGroup = null;
        //分组

        prjCardGroupComInfo.setTofirstRow();
        while (prjCardGroupComInfo.next()) {
            String groupId = prjCardGroupComInfo.getGroupid();
            TreeMap<String,JSONObject> openfieldMap= groupFieldMap.get(groupId);

            if(openfieldMap==null||openfieldMap.size()==0){
                continue;
            }

            int grouplabel=Util.getIntValue(prjCardGroupComInfo.getLabel(),-1);
            String groupTitle = SystemEnv.getHtmlLabelName(grouplabel, user.getLanguage());

            formItemGroup = new HashMap<String, Object>();
            formItemGroup.put("title", groupTitle);
            formItems = new ArrayList<Map<String, Object>>();

            Iterator it=openfieldMap.entrySet().iterator();
            while (it.hasNext()) {

                //设定输入框性质，1：查看，2：编辑，3：必填
                int viewAttr = 2;

                Map.Entry<String,JSONObject> entry=(Map.Entry<String,JSONObject>)it.next();
                JSONObject v= entry.getValue();

                String fieldId = v.getString("id");
                int fieldlabel= v.getInt("fieldlabel");
                Integer fieldHtmlType = v.getInt("fieldhtmltype");
                String type = v.getString("type");
                int ismand= v.getInt("ismand");
                String fieldName = v.getString("fieldname");
                String fielddbtype = v.getString("fielddbtype");
                int issystem=v.getInt("issystem");
                String fieldkind=v.getString("fieldkind");

                if(!"name".equalsIgnoreCase(fieldName)){
                    ismand = 0;
                }

                fieldName = issystem==1? fieldName:"field"+fieldId;
                if("2".equals(fieldkind)){
                    fieldName="customfield"+fieldId.replace("prjtype_", "");
                }

                //特殊处理字段值
                if("members".equals(fieldName)){//项目成员原有特殊逻辑,不能改原来展现的元素名
                    fieldName="hrmids02";
                }else if("department".equals(fieldName)){//部门字段不需要
                    continue;
                }else if("procode".equals(fieldName)){//项目编码字段不需要
                    continue;
                }else if("status".equals(fieldName)){//项目状态字段
                    continue;
                }else if("protemplateid".equals(fieldName)){//项目编码字段不需要
                    continue;
                }else if("passnoworktime".equals(fieldName)){//是否跳过工作日
                    continue;
                }

                int length = formatInputLength(fielddbtype, fieldHtmlType, type);
                String fieldLabel = SystemEnv.getHtmlLabelNames(""+fieldlabel, user.getLanguage());

                String fieldValue="";
                if("isblock".equals(fieldName)){
                    if("".equals(fieldValue)){
                        fieldValue = "0";
                    }
                }
                //必填
                if(ismand==1){
                    viewAttr = 3;
                }

                Map<String, Object> formItem = null;
                //特殊字段显示处理
                if("prjtype".equals(fieldName)){
                    if(projTypeId>0){
                        formItem = getFormItemForBrowser(fieldName, fieldLabel, type, ""+projTypeId, 1, "", null,null);
                    }else{
                        formItem = getFormItemForBrowser(fieldName, fieldLabel, type, "", 3, "", null,null);
                    }
                }else{
                    if (fieldHtmlType == 1) {
                        if ("2".equals(type) || "3".equals(type)||"4".equals(type)) {
                            // 整数小数位为0
                            int places = 0;
                            if ("3".equals(type)||"4".equals(type)) {
                                // 获得小数位
                                String placesStr = fielddbtype.substring(fielddbtype.indexOf(",") + 1, fielddbtype.length() - 1);
                                places = Integer.parseInt(placesStr);
                            }
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, 20, viewAttr,Util.getIntValue(type), places, null);
                        } else if ("5".equals(type)) {
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr,Util.getIntValue(type), 0, null);
                        } else {
                            // 文本
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 2) {
                        if("1".equals(type)){
                            // 多行文本
                            formItem = getFormItemForTextArea(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }else if("2".equals(type)){
                            // 富文本编辑
                            formItem = getFormItemForTextAreaHtml(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 3) {
                        if (type.equals("2") || type.equals("19")) {
                            // 日期按钮
//                            	List<Map<String,Object>> optionsList = getDateTypeOptions("",languageId);
//                                formItem = getFormItemForBrowserDate(fieldName, fieldLabel, fieldValue, viewAttr, optionsList);
                            if (type.equals("2")) {
                                formItem = getFormItemForDate(fieldName, fieldLabel, fieldValue, viewAttr);
                            } else if (type.equals("19")) {
                                formItem = getFormItemForTime(fieldName, fieldLabel, fieldValue, viewAttr);
                            }
                        } else {
                            // 浏览按钮
                            if("-1".equals(fieldValue)){
                                fieldValue = "";
                            }
                            // 浏览按钮
                            formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, viewAttr, fielddbtype, null,null);
                        }
                    } else if (fieldHtmlType == 4) {
                        // 复选按钮
                        formItem = getFormItemForCheckbox(fieldName, fieldLabel, fieldValue, viewAttr);
                    } else if (fieldHtmlType == 5) {
                        if("2".equals(fieldkind)){
                            // 下拉选择
                            formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prjtype",false,true);
                        }else{
                            // 下拉选择
                            formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prj",false,true);
                        }
                    } else if (fieldHtmlType == 6) {
                        // 附件上传
                        //formItem = getFormItemForAttachment(fieldName, fieldLabel, fieldValue, viewAttr);
                    }
                }

                formItems.add(formItem);

                //特殊字段后面添加字段显示
            }

            /*//相关附件
            if(groupcount==1 && prjset.getPrj_acc()){
            	Map<String, Object> formItem = getFormItemForAttachment("accessory", SystemEnv.getHtmlLabelNames("22194", user.getLanguage()), "", 2);
            	formItem.put("accsec", prjset.getPrj_accsec());
            	formItem.put("accsize", prjset.getPrj_accsize());
            	List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
            	formItem.put("datas", datas);
            	formItems.add(formItem);
            }*/
            formItemGroup.put("items", formItems);
            formItemGroup.put("defaultshow", true);
            formItemGroupList.add(formItemGroup);
        }
        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);

        //模板任务明细
        List<Map<String,String>> taskinfo = new ArrayList<Map<String,String>>();;

        fieldinfo.put("taskinfo", taskinfo);
        fieldinfo.put("userid", user.getUID());
        fieldinfo.put("prjid", "");
        fieldinfo.put("prjname", "");
        fieldinfo.put("templetName", "");
        fieldinfo.put("taskViewAttr", "2");
        return fieldinfo;
    }

    /**
     * 项目模板-显示
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getViewPrjTempletFormItems(User user, Map<String, Object> params) throws Exception {

        int templetId = Util.getIntValue(Util.null2String(params.get("templetId")),0);

        RecordSet rs = new RecordSet();
        String  templetName = "";
        int  projTypeId = -1;
        String strSql = "select * from Prj_Template where id="+templetId;
        rs.execute(strSql);
        if (rs.next()){
            templetName = Util.null2String(rs.getString("templetName"));
            projTypeId = Util.getIntValue(rs.getString("proTypeId"));
        }

        //项目类型自定义字段
        RecordSet rs_cus = new RecordSet();
        String sql_cus = "select * from prj_fielddata where id='"+templetId+"' and scope='ProjCustomField' and scopeid='"+projTypeId+"' ";
        rs_cus.execute(sql_cus);
        rs_cus.next();

        PrjFieldComInfo prjFieldComInfo = new PrjFieldComInfo();
        TreeMap<String,TreeMap<String,JSONObject>> groupFieldMap=prjFieldComInfo.getGroupFieldMap(""+projTypeId);
        PrjCardGroupComInfo prjCardGroupComInfo = new PrjCardGroupComInfo();
        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = null;
        Map<String, Object> formItemGroup = null;
        //分组
        int groupcount=0;//用来定位组

        prjCardGroupComInfo.setTofirstRow();
        while (prjCardGroupComInfo.next()) {
            String groupId = prjCardGroupComInfo.getGroupid();
            TreeMap<String,JSONObject> openfieldMap= groupFieldMap.get(groupId);

            if(openfieldMap==null||openfieldMap.size()==0){
                continue;
            }
            groupcount++;

            int grouplabel=Util.getIntValue(prjCardGroupComInfo.getLabel(),-1);
            String groupTitle = SystemEnv.getHtmlLabelName(grouplabel, user.getLanguage());

            formItemGroup = new HashMap<String, Object>();
            formItemGroup.put("title", groupTitle);
            formItems = new ArrayList<Map<String, Object>>();

            Iterator it=openfieldMap.entrySet().iterator();
            while (it.hasNext()) {

                //设定输入框性质，1：查看，2：编辑，3：必填
                int viewAttr = 1;

                Map.Entry<String,JSONObject> entry=(Map.Entry<String,JSONObject>)it.next();
                JSONObject v= entry.getValue();

                String fieldId = v.getString("id");
                int fieldlabel= v.getInt("fieldlabel");
                Integer fieldHtmlType = v.getInt("fieldhtmltype");
                String type = v.getString("type");
                String fieldName = v.getString("fieldname");
                String fielddbtype = v.getString("fielddbtype");
                int issystem=v.getInt("issystem");
                String fieldkind=v.getString("fieldkind");

                if("name".equals(fieldName)){//字段名称和项目卡片里的不一致,手动调整
                    fieldName="templetname";
                }else if("prjtype".equals(fieldName)){
                    fieldName="protypeid";
                }else if("worktype".equals(fieldName)){
                    fieldName="worktypeid";
                }else if("description".equals(fieldName)){
                    fieldName="procrm";
                }else if("managerview".equals(fieldName)){
                    fieldName="iscrmsee";
                }else if("parentid".equals(fieldName)){
                    fieldName="parentproid";
                }else if("members".equals(fieldName)||"hrmids02".equals(fieldName)){
                    fieldName="promember";
                }else if("isblock".equals(fieldName)){
                    fieldName="ismembersee";
                }else if("envaluedoc".equals(fieldName)){
                    fieldName="commentdoc";
                }else if("proposedoc".equals(fieldName)){
                    fieldName="advicedoc";
                }else if("protemplateid".equals(fieldName)){//模板字段不需要
                    continue;
                }else if("status".equals(fieldName)){//模板字段不需要
                    continue;
                }else if("procode".equals(fieldName)){//模板字段不需要
                    continue;
                }else if("passnoworktime".equals(fieldName)){//模板字段不需要
                    continue;
                }

                int length = formatInputLength(fielddbtype, fieldHtmlType, type);
                String fieldLabel = SystemEnv.getHtmlLabelNames(""+fieldlabel, user.getLanguage());

                String fieldValue="";

                if("2".equals(fieldkind)){
                    fieldValue = Util.null2String(rs_cus.getString(fieldName));
                }else{
                    fieldValue = Util.null2String(rs.getString(fieldName));
                }

                if("isblock".equals(fieldName)){
                    if("".equals(fieldValue)){
                        fieldValue = "0";
                    }
                }

                fieldName = issystem==1? fieldName:"field"+fieldId;
                if("2".equals(fieldkind)){
                    fieldName="customfield"+fieldId.replace("prjtype_", "");
                }

                Map<String, Object> formItem = null;
                //特殊字段显示处理
                if("prjtype".equals(fieldName)){
                    if(projTypeId>0){
                        formItem = getFormItemForBrowser(fieldName, fieldLabel, type, ""+projTypeId, 1, "", null,null);
                    }else{
                        formItem = getFormItemForBrowser(fieldName, fieldLabel, type, ""+projTypeId, 3, "", null,null);
                    }
                }else{
                    if (fieldHtmlType == 1) {
                        if ("2".equals(type) || "3".equals(type)||"4".equals(type)) {
                            // 整数小数位为0
                            int places = 0;
                            if ("3".equals(type)||"4".equals(type)) {
                                // 获得小数位
                                String placesStr = fielddbtype.substring(fielddbtype.indexOf(",") + 1, fielddbtype.length() - 1);
                                places = Integer.parseInt(placesStr);
                            }
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, 20, viewAttr,Util.getIntValue(type), places, null);
                        } else if ("5".equals(type)) {
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr,Util.getIntValue(type), 0, null);
                        } else {
                            // 文本
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 2) {
                        if("1".equals(type)){
                            // 多行文本
                            formItem = getFormItemForTextArea(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }else if("2".equals(type)){
                            // 富文本编辑
                            formItem = getFormItemForTextAreaHtml(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 3) {
                        if (type.equals("2") || type.equals("19")) {
                            // 日期按钮
//                            	List<Map<String,Object>> optionsList = getDateTypeOptions("",languageId);
//                                formItem = getFormItemForBrowserDate(fieldName, fieldLabel, fieldValue, viewAttr, optionsList);
                            if (type.equals("2")) {
                                formItem = getFormItemForDate(fieldName, fieldLabel, fieldValue, viewAttr);
                            } else if (type.equals("19")) {
                                formItem = getFormItemForTime(fieldName, fieldLabel, fieldValue, viewAttr);
                            }
                        } else {
                            // 浏览按钮
                            if("-1".equals(fieldValue)){
                                fieldValue = "";
                            }
                            // 浏览按钮
                            formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, viewAttr, fielddbtype, null,null);
                        }
                    } else if (fieldHtmlType == 4) {
                        // 复选按钮
                        formItem = getFormItemForCheckbox(fieldName, fieldLabel, fieldValue, viewAttr);
                    } else if (fieldHtmlType == 5) {
                        if("2".equals(fieldkind)){
                            // 下拉选择
                            formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prjtype",false,false);
                        }else{
                            // 下拉选择
                            formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prj",false,false);
                        }
                    } else if (fieldHtmlType == 6) {
                        // 附件上传
                        //formItem = getFormItemForAttachment(fieldName, fieldLabel, fieldValue, viewAttr);
                    }
                }

                formItems.add(formItem);

                //特殊字段后面添加字段显示
            }

            /*//相关附件
            if(groupcount==1 && prjset.getPrj_acc()){
            	Map<String, Object> formItem = getFormItemForAttachment("accessory", SystemEnv.getHtmlLabelNames("22194", user.getLanguage()), "", 2);
            	formItem.put("accsec", prjset.getPrj_accsec());
            	formItem.put("accsize", prjset.getPrj_accsize());
            	List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
            	formItem.put("datas", datas);
            	formItems.add(formItem);
            }*/
            formItemGroup.put("items", formItems);
            formItemGroup.put("defaultshow", true);
            formItemGroupList.add(formItemGroup);
        }
        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("templetId", templetId);
        fieldinfo.put("templetName", templetName);

        return fieldinfo;
    }

    /**
     * 项目模板-编辑
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getEditPrjTempletFormItems(User user, Map<String, Object> params) throws Exception {

        int templetId = Util.getIntValue(Util.null2String(params.get("templetId")),0);

        RecordSet rs = new RecordSet();
        String  templetName = "";
        int  projTypeId = -1;
        String strSql = "select * from Prj_Template where id="+templetId;
        rs.execute(strSql);
        if (rs.next()){
            templetName = Util.null2String(rs.getString("templetName"));
            projTypeId = Util.getIntValue(rs.getString("proTypeId"));
        }

        //项目类型自定义字段
        RecordSet rs_cus = new RecordSet();
        String sql_cus = "select * from prj_fielddata where id='"+templetId+"' and scope='ProjCustomField' and scopeid='"+projTypeId+"' ";
        rs_cus.execute(sql_cus);
        rs_cus.next();

        PrjFieldComInfo prjFieldComInfo = new PrjFieldComInfo();
        TreeMap<String,TreeMap<String,JSONObject>> groupFieldMap=prjFieldComInfo.getGroupFieldMap(""+projTypeId);
        PrjCardGroupComInfo prjCardGroupComInfo = new PrjCardGroupComInfo();
        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = null;
        Map<String, Object> formItemGroup = null;
        //分组
        int groupcount=0;//用来定位组

        prjCardGroupComInfo.setTofirstRow();
        while (prjCardGroupComInfo.next()) {
            String groupId = prjCardGroupComInfo.getGroupid();
            TreeMap<String,JSONObject> openfieldMap= groupFieldMap.get(groupId);

            if(openfieldMap==null||openfieldMap.size()==0){
                continue;
            }
            groupcount++;

            int grouplabel=Util.getIntValue(prjCardGroupComInfo.getLabel(),-1);
            String groupTitle = SystemEnv.getHtmlLabelName(grouplabel, user.getLanguage());

            formItemGroup = new HashMap<String, Object>();
            formItemGroup.put("title", groupTitle);
            formItems = new ArrayList<Map<String, Object>>();

            Iterator it=openfieldMap.entrySet().iterator();
            while (it.hasNext()) {

                //设定输入框性质，1：查看，2：编辑，3：必填
                int viewAttr = 2;

                Map.Entry<String,JSONObject> entry=(Map.Entry<String,JSONObject>)it.next();
                JSONObject v= entry.getValue();

                String fieldId = v.getString("id");
                int fieldlabel= v.getInt("fieldlabel");
                Integer fieldHtmlType = v.getInt("fieldhtmltype");
                String type = v.getString("type");
                int ismand= v.getInt("ismand");
                String fieldName = v.getString("fieldname");
                String fielddbtype = v.getString("fielddbtype");
                int issystem=v.getInt("issystem");
                String fieldkind=v.getString("fieldkind");

                if(!"name".equalsIgnoreCase(fieldName)){
                    ismand = 0;
                }

                if("name".equals(fieldName)){//字段名称和项目卡片里的不一致,手动调整
                    fieldName="templetname";
                }else if("worktype".equals(fieldName)){
                    fieldName="worktypeid";
                }else if("description".equals(fieldName)){
                    fieldName="procrm";
                }else if("managerview".equals(fieldName)){
                    fieldName="iscrmsee";
                }else if("parentid".equals(fieldName)){
                    fieldName="parentproid";
                }else if("members".equals(fieldName)||"hrmids02".equals(fieldName)){
                    fieldName="promember";
                }else if("isblock".equals(fieldName)){
                    fieldName="ismembersee";
                }else if("envaluedoc".equals(fieldName)){
                    fieldName="commentdoc";
                }else if("proposedoc".equals(fieldName)){
                    fieldName="advicedoc";
                }else if("protemplateid".equals(fieldName)){//模板字段不需要
                    continue;
                }else if("status".equals(fieldName)){//模板字段不需要
                    continue;
                }else if("procode".equals(fieldName)){//模板字段不需要
                    continue;
                }else if("passnoworktime".equals(fieldName)){//模板字段不需要
                    continue;
                }

                int length = formatInputLength(fielddbtype, fieldHtmlType, type);
                String fieldLabel = SystemEnv.getHtmlLabelNames(""+fieldlabel, user.getLanguage());

                String fieldValue="";
                if("2".equals(fieldkind)){
                    fieldValue = Util.null2String(rs_cus.getString(fieldName));
                }else{
                    fieldValue = Util.null2String(rs.getString(fieldName));
                }
                if("ismembersee".equals(fieldName)){
                    if("".equals(fieldValue)){
                        fieldValue = "0";
                    }
                }

                fieldName = issystem==1? fieldName:"field"+fieldId;
                if("2".equals(fieldkind)){
                    fieldName="customfield"+fieldId.replace("prjtype_", "");
                }

                //必填
                if(ismand==1){
                    viewAttr = 3;
                }

                Map<String, Object> formItem = null;
                //特殊字段显示处理
                if("prjtype".equals(fieldName)){
                    fieldValue = Util.null2String(rs.getString("protypeid"));
                    if(projTypeId>0){
                        formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, 1, "", null,null);
                    }else{
                        formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, 3, "", null,null);
                    }
                }else{
                    if (fieldHtmlType == 1) {
                        if ("2".equals(type) || "3".equals(type)||"4".equals(type)) {
                            // 整数小数位为0
                            int places = 0;
                            if ("3".equals(type)||"4".equals(type)) {
                                // 获得小数位
                                String placesStr = fielddbtype.substring(fielddbtype.indexOf(",") + 1, fielddbtype.length() - 1);
                                places = Integer.parseInt(placesStr);
                            }
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, 20, viewAttr,Util.getIntValue(type), places, null);
                        } else if ("5".equals(type)) {
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr,Util.getIntValue(type), 0, null);
                        } else {
                            // 文本
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 2) {
                        if("1".equals(type)){
                            // 多行文本
                            formItem = getFormItemForTextArea(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }else if("2".equals(type)){
                            // 富文本编辑
                            formItem = getFormItemForTextAreaHtml(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 3) {
                        if (type.equals("2") || type.equals("19")) {
                            // 日期按钮
//                            	List<Map<String,Object>> optionsList = getDateTypeOptions("",languageId);
//                                formItem = getFormItemForBrowserDate(fieldName, fieldLabel, fieldValue, viewAttr, optionsList);
                            if (type.equals("2")) {
                                formItem = getFormItemForDate(fieldName, fieldLabel, fieldValue, viewAttr);
                            } else if (type.equals("19")) {
                                formItem = getFormItemForTime(fieldName, fieldLabel, fieldValue, viewAttr);
                            }
                        } else {
                            // 浏览按钮
                            if("-1".equals(fieldValue)){
                                fieldValue = "";
                            }
                            // 浏览按钮
                            formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, viewAttr, fielddbtype, null,null);
                        }
                    } else if (fieldHtmlType == 4) {
                        // 复选按钮
                        formItem = getFormItemForCheckbox(fieldName, fieldLabel, fieldValue, viewAttr);
                    } else if (fieldHtmlType == 5) {
                        if("2".equals(fieldkind)){
                            // 下拉选择
                            formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prjtype",false,false);
                        }else{
                            // 下拉选择
                            formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prj",false,false);
                        }
                    } else if (fieldHtmlType == 6) {
                        // 附件上传
                        //formItem = getFormItemForAttachment(fieldName, fieldLabel, fieldValue, viewAttr);
                    }
                }

                formItems.add(formItem);

                //特殊字段后面添加字段显示
            }

            /*//相关附件
            if(groupcount==1 && prjset.getPrj_acc()){
            	Map<String, Object> formItem = getFormItemForAttachment("accessory", SystemEnv.getHtmlLabelNames("22194", user.getLanguage()), "", 2);
            	formItem.put("accsec", prjset.getPrj_accsec());
            	formItem.put("accsize", prjset.getPrj_accsize());
            	List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
            	formItem.put("datas", datas);
            	formItems.add(formItem);
            }*/
            formItemGroup.put("items", formItems);
            formItemGroup.put("defaultshow", true);
            formItemGroupList.add(formItemGroup);
        }
        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("templetId", templetId);
        fieldinfo.put("templetName", templetName);

        return fieldinfo;
    }


    /**
     * 项目模板-任务显示
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getViewTaskTempletFormItems(User user, Map<String, Object> params) throws Exception {

        int templetTaskId = Util.getIntValue(Util.null2String(params.get("templetTaskId")),0);

        RecordSet rs = new RecordSet();
        String taskid = "";
        String templetId = "";
        String taskName = "";
        String taskManager = "";
        String taskBudget = "";
        String taskBefTaskID = "";
        String taskDesc = "";
        String project_accessory = "";//相关附件
        String templateStatus = "";//模板状态
        String sqlSelectTaskByID = "select t1.*,t2.status as tstatus from Prj_TemplateTask t1 left outer join Prj_Template t2 on t2.id=t1.templetId WHERE t1.id="+templetTaskId;
        rs.execute(sqlSelectTaskByID);

        if(rs.next()){
            taskid = rs.getString("id");
            templetId = rs.getString("templetId");
            taskName = rs.getString("taskName");
            taskManager = rs.getString("taskManager");
            taskBudget = rs.getString("budget");
            taskBefTaskID = rs.getString("befTaskId");
            taskDesc = rs.getString("taskDesc");
            project_accessory = Util.null2String(rs.getString("accessory"));
            templateStatus=Util.null2String(rs.getString("tstatus"));
        }

        PrjTskFieldComInfo prjTskFieldComInfo = new PrjTskFieldComInfo();
        TreeMap<String,TreeMap<String,JSONObject>> groupFieldMap= prjTskFieldComInfo.getGroupFieldMap();

        PrjCardGroupComInfo prjCardGroupComInfo = new PrjCardGroupComInfo();
        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = null;
        Map<String, Object> formItemGroup = null;
        //分组
        int groupcount=0;//用来定位组
        prjCardGroupComInfo.setTofirstRow();
        while (prjCardGroupComInfo.next()) {
            String groupId = prjCardGroupComInfo.getGroupid();
            TreeMap<String,JSONObject> openfieldMap= groupFieldMap.get(groupId);

            if(openfieldMap==null||openfieldMap.size()==0){
                continue;
            }
            groupcount++;

            int grouplabel=Util.getIntValue(prjCardGroupComInfo.getLabel(),-1);
            String groupTitle = SystemEnv.getHtmlLabelName(grouplabel, user.getLanguage());

            formItemGroup = new HashMap<String, Object>();
            formItemGroup.put("title", groupTitle);
            formItems = new ArrayList<Map<String, Object>>();

            Iterator it=openfieldMap.entrySet().iterator();
            while (it.hasNext()) {

                //设定输入框性质，1：查看，2：编辑，3：必填
                int viewAttr = 1;

                Map.Entry<String,JSONObject> entry=(Map.Entry<String,JSONObject>)it.next();
                JSONObject v= entry.getValue();

                String fieldId = v.getString("id");
                int fieldlabel= v.getInt("fieldlabel");
                Integer fieldHtmlType = v.getInt("fieldhtmltype");
                String type = v.getString("type");
                String fieldName = v.getString("fieldname");
                String fielddbtype = v.getString("fielddbtype");
                int issystem=v.getInt("issystem");
                String fieldkind=v.getString("fieldkind");

                if("actualbegindate".equalsIgnoreCase(fieldName)||"actualenddate".equalsIgnoreCase(fieldName)
                        ||"realmandays".equalsIgnoreCase(fieldName)||"finish".equalsIgnoreCase(fieldName)
                        ||"islandmark".equalsIgnoreCase(fieldName)||"prjid".equalsIgnoreCase(fieldName)){
                    continue;
                }
                if("fixedcost".equalsIgnoreCase(fieldName) && "2".equals( user.getLogintype())){
                    continue;
                }
                if("accessory".equalsIgnoreCase(fieldName)){
                    continue;
                }
                if("parentid".equalsIgnoreCase( fieldName)&& rs.getInt("parenttaskid")<=0){
                    continue;
                }

                int length = formatInputLength(fielddbtype, fieldHtmlType, type);
                String fieldLabel = SystemEnv.getHtmlLabelNames(""+fieldlabel, user.getLanguage());

                String fieldValue="";
                if("subject".equalsIgnoreCase( fieldName)){
                    fieldValue = taskName;
                }else if("fixedcost".equalsIgnoreCase( fieldName)){
                    fieldValue=taskBudget;
                }else if("hrmid".equalsIgnoreCase( fieldName)){
                    fieldValue= taskManager ;
                }else if("content".equalsIgnoreCase( fieldName)){
                    fieldValue = Util.toScreen(taskDesc, user.getLanguage()) ;
                }else if("parentid".equalsIgnoreCase( fieldName)){
                    fieldValue = Util.null2String(rs.getInt("parenttaskid"));
                }else if("prefinish".equalsIgnoreCase(fieldName)){
                    fieldValue = taskBefTaskID;
                }else{
                    fieldValue = Util.null2String(rs.getString(fieldName));
                }

                Map<String, Object> formItem = null;
                //特殊字段显示处理
                if("prefinish".equalsIgnoreCase(fieldName)||"parentid".equalsIgnoreCase(fieldName)){
                    Map<String,Object> dataParams = new HashMap<String,Object>();
                    dataParams.put("prjtasktype", "templet");
                    dataParams.put("prjid", templetId);
                    dataParams.put("taskid", taskid);
                    formItem = getFormItemForBrowser(fieldName, fieldLabel, "prjtsk", fieldValue, viewAttr, "", null,dataParams);
                }else{
                    if (fieldHtmlType == 1) {
                        if ("2".equals(type) || "3".equals(type)||"4".equals(type)) {
                            // 整数小数位为0
                            int places = 0;
                            if ("3".equals(type)||"4".equals(type)) {
                                // 获得小数位
                                String placesStr = fielddbtype.substring(fielddbtype.indexOf(",") + 1, fielddbtype.length() - 1);
                                places = Integer.parseInt(placesStr);
                            }
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, 20, viewAttr,Util.getIntValue(type), places, null);
                        } else if ("5".equals(type)) {
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr,Util.getIntValue(type), 0, null);
                        } else {
                            // 文本
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 2) {
                        if("1".equals(type)){
                            // 多行文本
                            formItem = getFormItemForTextArea(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }else if("2".equals(type)){
                            // 富文本编辑
                            formItem = getFormItemForTextAreaHtml(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 3) {
                        if (type.equals("2") || type.equals("19")) {
                            // 日期按钮
//                                	List<Map<String,Object>> optionsList = getDateTypeOptions("",languageId);
//                                    formItem = getFormItemForBrowserDate(fieldName, fieldLabel, fieldValue, viewAttr, optionsList);
                            if (type.equals("2")) {
                                formItem = getFormItemForDate(fieldName, fieldLabel, fieldValue, viewAttr);
                            } else if (type.equals("19")) {
                                formItem = getFormItemForTime(fieldName, fieldLabel, fieldValue, viewAttr);
                            }
                        } else {
                            // 浏览按钮
                            if("-1".equals(fieldValue)){
                                fieldValue = "";
                            }
                            // 浏览按钮
                            formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, viewAttr, fielddbtype, null,null);
                        }
                    } else if (fieldHtmlType == 4) {
                        // 复选按钮
                        formItem = getFormItemForCheckbox(fieldName, fieldLabel, fieldValue, viewAttr);
                    } else if (fieldHtmlType == 5) {
                        // 下拉选择
                        formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prjtsk",false,false);
                    } else if (fieldHtmlType == 6) {
                        // 附件上传
                        //formItem = getFormItemForAttachment(fieldName, fieldLabel, fieldValue, viewAttr);
                    }
                }


                formItems.add(formItem);
            }

            /*//相关附件
            if(groupcount==1 && prjset.getPrj_acc()){
            	Map<String, Object> formItem = getFormItemForAttachment("accessory", SystemEnv.getHtmlLabelNames("22194", user.getLanguage()), "", 2);
            	formItem.put("accsec", prjset.getPrj_accsec());
            	formItem.put("accsize", prjset.getPrj_accsize());
            	List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
            	formItem.put("datas", datas);
            	formItems.add(formItem);
            }*/
            formItemGroup.put("items", formItems);
            formItemGroup.put("defaultshow", true);
            formItemGroupList.add(formItemGroup);
        }
        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("taskid", taskid);
        fieldinfo.put("prjid", templetId);
        fieldinfo.put("taskname", taskName);

        List<PrjRightMenu> rightMenus = new ArrayList<PrjRightMenu>();
        boolean canMaint = false ;
        if (HrmUserVarify.checkUserRight("ProjTemplet:Maintenance", user)) {
            canMaint = true ;
        }
        if (canMaint&&!"2".equals(templateStatus)){
            rightMenus.add(new PrjRightMenu(user.getLanguage(), PrjRightMenuType.BTN_EDIT,"", true));//编辑
            //rightMenus.add(new PrjRightMenu(user.getLanguage(),PrjRightMenuType.BTN_DELETE,"", true));//删除
        }
        fieldinfo.put("rightMenus", rightMenus);
        return fieldinfo;
    }

    /**
     * 项目模板-任务编辑
     * @param languageId
     * @param operation
     * @param params
     * @return
     * @throws JSONException
     */
    public static Map<String,Object> getEditTaskTempletFormItems(User user, Map<String, Object> params) throws Exception {

        int templetTaskId = Util.getIntValue(Util.null2String(params.get("templetTaskId")),0);

        RecordSet rs = new RecordSet();
        String taskid = "";
        String templetId = "";
        String taskName = "";
        String taskManager = "";
        String taskBudget = "";
        String taskBefTaskID = "";
        String taskDesc = "";
        String project_accessory = "";//相关附件
        String sqlSelectTaskByID = " select t1.*,t2.proMember from Prj_TemplateTask t1 join Prj_Template t2 on t2.id=t1.templetId where t1.id="+templetTaskId;
        rs.execute(sqlSelectTaskByID);

        if(rs.next()){
            taskid = rs.getString("id");
            templetId = rs.getString("templetId");
            taskName = rs.getString("taskName");
            taskManager = rs.getString("taskManager");
            taskBudget = rs.getString("budget");
            taskBefTaskID = rs.getString("befTaskId");
            taskDesc = rs.getString("taskDesc");
            project_accessory = Util.null2String(rs.getString("accessory"));
        }

        PrjTskFieldComInfo prjTskFieldComInfo = new PrjTskFieldComInfo();
        TreeMap<String,TreeMap<String,JSONObject>> groupFieldMap= prjTskFieldComInfo.getGroupFieldMap();

        PrjCardGroupComInfo prjCardGroupComInfo = new PrjCardGroupComInfo();
        List<Map<String, Object>> formItemGroupList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> formItems = null;
        Map<String, Object> formItemGroup = null;
        //分组
        int groupcount=0;//用来定位组
        prjCardGroupComInfo.setTofirstRow();
        while (prjCardGroupComInfo.next()) {
            String groupId = prjCardGroupComInfo.getGroupid();
            TreeMap<String,JSONObject> openfieldMap= groupFieldMap.get(groupId);

            if(openfieldMap==null||openfieldMap.size()==0){
                continue;
            }
            groupcount++;

            int grouplabel=Util.getIntValue(prjCardGroupComInfo.getLabel(),-1);
            String groupTitle = SystemEnv.getHtmlLabelName(grouplabel, user.getLanguage());

            formItemGroup = new HashMap<String, Object>();
            formItemGroup.put("title", groupTitle);
            formItems = new ArrayList<Map<String, Object>>();

            Iterator it=openfieldMap.entrySet().iterator();
            while (it.hasNext()) {

                //设定输入框性质，1：查看，2：编辑，3：必填
                int viewAttr = 2;

                Map.Entry<String,JSONObject> entry=(Map.Entry<String,JSONObject>)it.next();
                JSONObject v= entry.getValue();

                String fieldId = v.getString("id");
                int fieldlabel= v.getInt("fieldlabel");
                Integer fieldHtmlType = v.getInt("fieldhtmltype");
                String type = v.getString("type");
                String fieldName = v.getString("fieldname");
                String fielddbtype = v.getString("fielddbtype");
                int issystem=v.getInt("issystem");

                if("actualbegindate".equalsIgnoreCase(fieldName)||"actualenddate".equalsIgnoreCase(fieldName)
                        ||"realmandays".equalsIgnoreCase(fieldName)||"finish".equalsIgnoreCase(fieldName)
                        ||"islandmark".equalsIgnoreCase(fieldName)||"prjid".equalsIgnoreCase(fieldName)){
                    continue;
                }
                if("accessory".equalsIgnoreCase(fieldName)){
                    continue;
                }
                if("parentid".equalsIgnoreCase( fieldName)&& rs.getInt("parenttaskid")<=0){
                    continue;
                }


                int length = formatInputLength(fielddbtype, fieldHtmlType, type);
                String fieldLabel = SystemEnv.getHtmlLabelNames(""+fieldlabel, user.getLanguage());

                String fieldValue="";
                if("subject".equalsIgnoreCase( fieldName)){
                    fieldValue = taskName;
                }else if("fixedcost".equalsIgnoreCase( fieldName)){
                    fieldValue=taskBudget;
                }else if("hrmid".equalsIgnoreCase( fieldName)){
                    fieldValue= taskManager ;
                }else if("content".equalsIgnoreCase( fieldName)){
                    fieldValue = Util.toScreen(taskDesc, user.getLanguage()) ;
                }else if("parentid".equalsIgnoreCase( fieldName)){
                    fieldValue = Util.null2String(rs.getInt("parenttaskid"));
                }else if("prefinish".equalsIgnoreCase(fieldName)){
                    fieldValue = taskBefTaskID;
                }else{
                    fieldValue = Util.null2String(rs.getString(fieldName));
                }

                Map<String, Object> formItem = null;
                fieldName = issystem==1? fieldName:"field"+fieldId;
                //特殊字段显示处理
                if("parentid".equalsIgnoreCase(fieldName)){
                    Map<String,Object> dataParams = new HashMap<String,Object>();
                    dataParams.put("prjtasktype", "templet");
                    dataParams.put("prjid", templetId);
                    formItem = getFormItemForBrowser(fieldName, fieldLabel, "prjtsk", fieldValue, 1, "", null,dataParams);
                }else if("prefinish".equalsIgnoreCase(fieldName)){
                    Map<String,Object> dataParams = new HashMap<String,Object>();
                    dataParams.put("prjtasktype", "templet");
                    dataParams.put("prjid", templetId);
                    dataParams.put("taskid", taskid);
                    formItem = getFormItemForBrowser(fieldName, fieldLabel, "prjtsk", fieldValue, viewAttr, "", null,dataParams);
                }else{

                    if (fieldHtmlType == 1) {
                        if ("2".equals(type) || "3".equals(type)||"4".equals(type)) {
                            // 整数小数位为0
                            int places = 0;
                            if ("3".equals(type)||"4".equals(type)) {
                                // 获得小数位
                                String placesStr = fielddbtype.substring(fielddbtype.indexOf(",") + 1, fielddbtype.length() - 1);
                                places = Integer.parseInt(placesStr);
                            }
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, 20, viewAttr,Util.getIntValue(type), places, null);
                        } else if ("5".equals(type)) {
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr,Util.getIntValue(type), 0, null);
                        } else {
                            // 文本
                            formItem = getFormItemForInput(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 2) {
                        if("1".equals(type)){
                            // 多行文本
                            formItem = getFormItemForTextArea(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }else if("2".equals(type)){
                            // 富文本编辑
                            formItem = getFormItemForTextAreaHtml(fieldName, fieldLabel, fieldValue, length, viewAttr);
                        }
                    } else if (fieldHtmlType == 3) {
                        if (type.equals("2") || type.equals("19")) {
                            // 日期按钮
//                                	List<Map<String,Object>> optionsList = getDateTypeOptions("",languageId);
//                                    formItem = getFormItemForBrowserDate(fieldName, fieldLabel, fieldValue, viewAttr, optionsList);
                            if (type.equals("2")) {
                                formItem = getFormItemForDate(fieldName, fieldLabel, fieldValue, viewAttr);
                            } else if (type.equals("19")) {
                                formItem = getFormItemForTime(fieldName, fieldLabel, fieldValue, viewAttr);
                            }
                        } else {
                            // 浏览按钮
                            if("-1".equals(fieldValue)){
                                fieldValue = "";
                            }
                            // 浏览按钮
                            formItem = getFormItemForBrowser(fieldName, fieldLabel, type, fieldValue, viewAttr, fielddbtype, null,null);
                        }
                    } else if (fieldHtmlType == 4) {
                        // 复选按钮
                        formItem = getFormItemForCheckbox(fieldName, fieldLabel, fieldValue, viewAttr);
                    } else if (fieldHtmlType == 5) {
                        // 下拉选择
                        formItem = getFormItemForSelect(fieldName, fieldLabel, fieldId, fieldValue,viewAttr,"prjtsk",false,false);
                    } else if (fieldHtmlType == 6) {
                        // 附件上传
                        //formItem = getFormItemForAttachment(fieldName, fieldLabel, fieldValue, viewAttr);
                    }
                }

                formItems.add(formItem);
            }

            /*//相关附件
            if(groupcount==1 && prjset.getPrj_acc()){
            	Map<String, Object> formItem = getFormItemForAttachment("accessory", SystemEnv.getHtmlLabelNames("22194", user.getLanguage()), "", 2);
            	formItem.put("accsec", prjset.getPrj_accsec());
            	formItem.put("accsize", prjset.getPrj_accsize());
            	List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
            	formItem.put("datas", datas);
            	formItems.add(formItem);
            }*/
            formItemGroup.put("items", formItems);
            formItemGroup.put("defaultshow", true);
            formItemGroupList.add(formItemGroup);
        }
        Map<String,Object> fieldinfo = new HashMap<String,Object>();
        fieldinfo.put("fieldinfo", formItemGroupList);
        fieldinfo.put("taskid", taskid);
        fieldinfo.put("prjid", templetId);
        fieldinfo.put("taskname", taskName);

        List<PrjRightMenu> rightMenus = new ArrayList<PrjRightMenu>();
        boolean canMaint = false ;
        if (HrmUserVarify.checkUserRight("ProjTemplet:Maintenance", user)) {
            canMaint = true ;
        }
        if (canMaint) {
            rightMenus.add(new PrjRightMenu(user.getLanguage(),PrjRightMenuType.BTN_SAVE,"", true));//保存
        }
        rightMenus.add(new PrjRightMenu(user.getLanguage(),PrjRightMenuType.BTN_BACK,"", false));//返回
        fieldinfo.put("rightMenus", rightMenus);
        return fieldinfo;
    }

    //单行文本
    public static Map<String, Object> getFormItemForInput(String fieldName, String fieldLabel, String value, int length, int viewAttr) {
        return getFormItemForInput(fieldName, fieldLabel, value, length, viewAttr, 1, 0, null);
    }

    //单行文本(特殊类型处理)
    public static Map<String, Object> getFormItemForInput(String fieldName, String fieldLabel, String value, int length, int viewAttr, int detailtype, int qfws, Map<String, Object> style) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("formItemType", INPUT);
        formItem.put("conditionType", INPUT);
        formItem.put("value", value == null ? "" : value);
        formItem.put("length", length);
        formItem.put("viewAttr", viewAttr);
        Map<String, Object> otherParams = new HashMap<String, Object>();
        if(detailtype == 1){
            otherParams.put("inputType", "");
        }else{
            otherParams.put("inputType", "form");
        }
        otherParams.put("detailtype", detailtype);
        otherParams.put("qfws", qfws);
        otherParams.put("format", new HashMap<String, Object>());
        if (null == style)
            style = new HashMap<String, Object>();
        otherParams.put("style", style);
        formItem.put("otherParams", otherParams);
        return formItem;
    }

    //数值范围
    public static Map<String, Object> getFormItemForScope(String startFieldName, String endFieldName, String fieldLabel, String startFieldValue, String endFieldValue, int places, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        formItem.put("conditionType", SCOPE);
        formItem.put("labelcol", 5);
        formItem.put("colSpan", 2);
        formItem.put("startValue", startFieldValue);
        formItem.put("endValue", endFieldValue);
        List<Object> minList = new ArrayList<Object>(2);
        List<Object> maxList = new ArrayList<Object>(2);
        if (places == 0) {
            int min = 1;
            int max = 999999;
            // 整数
            minList.add(min);
            minList.add(max);
            maxList.add(min);
            maxList.add(max);
        } else {
            // 浮点数
            float min = 0;
            float max = 999999;
            switch (places) {
                case 1:
                    min = 0.1f;
                    break;
                case 2:
                    min = 0.01f;
                    break;
                case 3:
                    min = 0.001f;
                    break;
                case 4:
                    min = 0.0001f;
                    break;
            }
            minList.add(min);
            minList.add(max);
            maxList.add(min);
            maxList.add(max);
        }
        formItem.put("min", minList);
        formItem.put("max", maxList);
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(startFieldName);
        domKeyList.add(endFieldName);
        formItem.put("domkey", domKeyList);
        formItem.put("fieldcol", 14);
        formItem.put("label", fieldLabel);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //多行文本
    public static Map<String, Object> getFormItemForTextArea(String fieldName, String fieldLabel, String value, int length, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", TEXTAREA);
        formItem.put("value", value);
        formItem.put("length", length);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //多行文本-富文本编辑
    public static Map<String, Object> getFormItemForTextAreaHtml(String fieldName, String fieldLabel, String value, int length, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", HTMLTEXT);
        formItem.put("value", value);
        formItem.put("length", length);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //浏览按钮
    public static Map<String, Object> getFormItemForBrowser(String fieldName, String fieldLabel, String browserType, String value, int viewAttr, String dmlUrl, String linkUrl, Map<String, Object> dataParams) {
        RecordSet rsTemp = new RecordSet();
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        //浏览框URL
        BrowserConfigComInfo browserConfigComInfo = new BrowserConfigComInfo();
        BrowserComInfo browserComInfo = new BrowserComInfo();
        linkUrl = Util.null2s(linkUrl,"");
        if ("".equals(linkUrl)) {
            linkUrl = Util.null2String(browserConfigComInfo.getLinkurl(browserType));
            if (Util.getIntValue(browserType, 0) > 0) {
                if ("".equals(linkUrl)) {
                    linkUrl = Util.null2String(browserComInfo.getLinkurl(browserType));
                }
            }
        }

        Map<String, Object> browserConditionParam = new HashMap<String, Object>();
        // browser value
        List<Map<String, String>> replaceDatas = new ArrayList<Map<String, String>>();
        List<Map<String, String>> replaceDatas1 = new ArrayList<Map<String, String>>();
        value = Util.null2String(value);
        if (!"".equals(value)) {
            String showName = "";
            if("prjtsk".equals(browserType)){
                String prjtasktype = Util.null2String(dataParams.get("prjtasktype"));
                String prjid = Util.null2String(dataParams.get("prjid"));
                if("templet".equals(prjtasktype)){
                    rsTemp.execute("select taskname from Prj_TemplateTask where templetId='" + prjid+"' and templetTaskId="+value);
                    if (rsTemp.next()) {
                        showName = rsTemp.getString("taskname");
                    }
                }else{
                    rsTemp.execute("select subject from Prj_TaskProcess where id=" + value);
                    if (rsTemp.next()) {
                        showName = rsTemp.getString("subject");
                    }
                }
                Map<String, String> browserOptionMap = new HashMap<String, String>(2);
                Map<String, String> browserOptionMap1 = new HashMap<String, String>(2);
                browserOptionMap.put("id", value);
                browserOptionMap.put("name", showName);
                replaceDatas.add(browserOptionMap)
                ;
                browserOptionMap1.put("id", value);
                browserOptionMap1.put("name", showName);
                replaceDatas1.add(browserOptionMap1);
                browserConditionParam.put("replaceDatas", replaceDatas);
            }else{
                showName = getBrowserShowName(browserType, value, dmlUrl);
                if (broswerTypes.indexOf("," + browserType + ",") > -1){
                    String ids[] = value.split(",");
                    String showNames[] = showName.split(",");
                    for(int i = 0;i<ids.length;i++){
                        Map<String, String> browserOptionMap = new HashMap<String, String>(2);
                        Map<String, String> browserOptionMap1 = new HashMap<String, String>(2);
                        browserOptionMap.put("id", ids[i]);
                        browserOptionMap1.put("id", ids[i]);
                        if(showNames.length>i){
                            browserOptionMap.put("name", showNames[i]);
                            browserOptionMap1.put("name", showNames[i]);
                        }else{
                            browserOptionMap.put("name", "");
                            browserOptionMap1.put("name", "");
                        }
                        replaceDatas.add(browserOptionMap);
                        replaceDatas1.add(browserOptionMap1);
                    }
                }else{
                    Map<String, String> browserOptionMap = new HashMap<String, String>(2);
                    Map<String, String> browserOptionMap1 = new HashMap<String, String>(2);
                    browserOptionMap.put("id", value);
                    browserOptionMap.put("name", showName);
                    replaceDatas.add(browserOptionMap);

                    browserOptionMap1.put("id", value);
                    browserOptionMap1.put("name", showName);
                    replaceDatas1.add(browserOptionMap1);
                }
                browserConditionParam.put("replaceDatas", replaceDatas);
            }

        }
        if (null == dataParams) {
            dataParams = new HashMap<String, Object>();
        }

        //资产资料，不显示链接
        if("179".equals(browserType)){
            dataParams.put("sqlwhere", " isdata='1' ");
        }
        //快捷搜索，流程路径
        if (browserType.equals("-99991")) {
            dataParams.put("search", "customQuery");
        }

        browserConditionParam.put("dataParams", dataParams);
        browserConditionParam.put("hasAdd", false);
        if (broswerTypes1.indexOf("," + browserType + ",") > -1) {
            browserConditionParam.put("hasAdvanceSerach", false);
        } else {
            browserConditionParam.put("hasAdvanceSerach", true);
        }
        browserConditionParam.put("isAutoComplete", 1);
        browserConditionParam.put("isDetail", 0);
        if(browserType.equals("162")||browserType.equals("257")){
            browserConditionParam.put("isMultCheckbox", true);
        }else{
            browserConditionParam.put("isMultCheckbox", false);
        }

        browserConditionParam.put("isSingle", true);
        browserConditionParam.put("linkUrl", linkUrl);
        browserConditionParam.put("title", fieldLabel);
        browserConditionParam.put("type", browserType);
        browserConditionParam.put("viewAttr", viewAttr);
        // 如果是部门或者分部，增加tab参数
        if (browserType.equals("4") || browserType.equals("164")) {
            List<Map<String, Object>> tabList = new ArrayList<Map<String, Object>>();
            Map<String, Object> tabMap1 = new HashMap<String, Object>();
            Map<String, Object> tabMap1Sub = new HashMap<String, Object>();
            tabMap1Sub.put("list", "1");
            tabMap1.put("dataParams", tabMap1Sub);
            tabMap1.put("key", "1");
            tabMap1.put("name", "按列表");
            tabMap1.put("selected", false);
            tabList.add(tabMap1);
            Map<String, Object> tabMap2 = new HashMap<String, Object>();
            tabMap2.put("key", "2");
            tabMap2.put("name", "按组织结构");
            tabMap2.put("selected", false);
            tabList.add(tabMap2);
            browserConditionParam.put("tabs", tabList);
        }
        if (browserType.equals("161") || browserType.equals("162")||browserType.equals("256") || browserType.equals("257")) {
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", "");
            browserConditionParam.put("dataParams", dataParams);
            dataParams = new HashMap<String, Object>();
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", "");
            browserConditionParam.put("conditionDataParams", dataParams);
            dataParams = new HashMap<String, Object>();
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", value);
            browserConditionParam.put("destDataParams", dataParams);
            dataParams = new HashMap<String, Object>();
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", "");
            browserConditionParam.put("completeParams", dataParams);
        }
        if (browserType.equals("prjtsk")) {
            String prjid = Util.null2String(dataParams.get("prjid"));
            String prjtasktype = Util.null2String(dataParams.get("prjtasktype"));
            Map<String,String> m = new HashMap<String,String>();
            m.put("prjid", prjid);
            m.put("prjtasktype", prjtasktype);
            browserConditionParam.put("conditionDataParams", m);
            m = new HashMap<String,String>();
            m.put("prjid", prjid);
            m.put("prjtasktype", prjtasktype);
            browserConditionParam.put("completeParams", m);
        }

        if (browserType.equals("-99991")||browserType.equals("179")||browserType.equals("23") || browserType.equals("26") || browserType.equals("3")) {
            Map<String, Object> conditionDataParams = new HashMap<String, Object>();
            Map<String, Object> completeParams = new HashMap<String, Object>();
            Map<String, Object> destDataParams = new HashMap<String, Object>();
            for(String key : dataParams.keySet()){
                conditionDataParams.put(key, dataParams.get(key));
                completeParams.put(key, dataParams.get(key));
                destDataParams.put(key, dataParams.get(key));
            }
            browserConditionParam.put("conditionDataParams", conditionDataParams);
            browserConditionParam.put("destDataParams", destDataParams);
            browserConditionParam.put("completeParams", completeParams);
        }

        if (broswerTypes.indexOf("," + browserType + ",") > -1) {
            browserConditionParam.put("isSingle", false);
        } else {
            browserConditionParam.put("isSingle", true);
        }
        formItem.put("browserConditionParam", browserConditionParam);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", BROWSER);
        formItem.put("value", value);
        formItem.put("browserType", browserType);

        Map<String, Object> otherParams = new HashMap<String, Object>();
        otherParams.put("replaceDatas", replaceDatas1);
//        formItem.put("otherParams", otherParams);
        return formItem;
    }

    //浏览按钮
    public static Map<String, Object> getFormItemForBrowser(String fieldName, String fieldLabel, String browserType, String value, int viewAttr, String dmlUrl, String linkUrl, Map<String, Object> dataParams,User user) {
        RecordSet rsTemp = new RecordSet();
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        //浏览框URL
        BrowserConfigComInfo browserConfigComInfo = new BrowserConfigComInfo();
        BrowserComInfo browserComInfo = new BrowserComInfo();
        linkUrl = Util.null2s(linkUrl,"");
        if ("".equals(linkUrl)) {
            linkUrl = Util.null2String(browserConfigComInfo.getLinkurl(browserType));
            if (Util.getIntValue(browserType, 0) > 0) {
                if ("".equals(linkUrl)) {
                    linkUrl = Util.null2String(browserComInfo.getLinkurl(browserType));
                }
            }
        }

        //资产资料，不显示链接
        if("179".equals(browserType)){
            dataParams.put("sqlwhere", " isdata='1' ");
        }
        Map<String, Object> browserConditionParam = new HashMap<String, Object>();
        // browser value
        List<Map<String, String>> replaceDatas = new ArrayList<Map<String, String>>();
        value = Util.null2String(value);
        if (!"".equals(value)) {
            String showName = "";
            if("prjtsk".equals(browserType)){
                String prjtasktype = Util.null2String(dataParams.get("prjtasktype"));
                String prjtas = Util.null2String(dataParams.get("prjtasktype"));
                if("templet".equals(prjtasktype)){
                    rsTemp.execute("select taskname from Prj_TemplateTask where id=" + value);
                    if (rsTemp.next()) {
                        showName = rsTemp.getString("taskname");
                    }
                }else{
                    rsTemp.execute("select subject from Prj_TaskProcess where id=" + value);
                    if (rsTemp.next()) {
                        showName = rsTemp.getString("subject");
                    }
                }
            }else{
                showName = getBrowserShowName(browserType, value, dmlUrl,user);
            }


            Map<String, String> browserOptionMap = new HashMap<String, String>(2);
            browserOptionMap.put("id", value);
            browserOptionMap.put("name", showName);
            replaceDatas.add(browserOptionMap);
            browserConditionParam.put("replaceDatas", replaceDatas);
        }
        if (null == dataParams) {
            dataParams = new HashMap<String, Object>();
        }

        browserConditionParam.put("dataParams", dataParams);
        browserConditionParam.put("hasAdd", false);
        if (broswerTypes1.indexOf("," + browserType + ",") > -1) {
            browserConditionParam.put("hasAdvanceSerach", false);
        } else {
            browserConditionParam.put("hasAdvanceSerach", true);
        }
        browserConditionParam.put("isAutoComplete", 1);
        browserConditionParam.put("isDetail", 0);
        if(browserType.equals("162")||browserType.equals("257")){
            browserConditionParam.put("isMultCheckbox", true);
        }else{
            browserConditionParam.put("isMultCheckbox", false);
        }

        browserConditionParam.put("isSingle", true);
        browserConditionParam.put("linkUrl", linkUrl);
        browserConditionParam.put("title", fieldLabel);
        browserConditionParam.put("type", browserType);
        browserConditionParam.put("viewAttr", viewAttr);
        // 如果是部门或者分部，增加tab参数
        if (browserType.equals("4") || browserType.equals("164")) {
            List<Map<String, Object>> tabList = new ArrayList<Map<String, Object>>();
            Map<String, Object> tabMap1 = new HashMap<String, Object>();
            Map<String, Object> tabMap1Sub = new HashMap<String, Object>();
            tabMap1Sub.put("list", "1");
            tabMap1.put("dataParams", tabMap1Sub);
            tabMap1.put("key", "1");
            tabMap1.put("name", "按列表");
            tabMap1.put("selected", false);
            tabList.add(tabMap1);
            Map<String, Object> tabMap2 = new HashMap<String, Object>();
            tabMap2.put("key", "2");
            tabMap2.put("name", "按组织结构");
            tabMap2.put("selected", false);
            tabList.add(tabMap2);
            browserConditionParam.put("tabs", tabList);
        }
        if (browserType.equals("161") || browserType.equals("162")||browserType.equals("256") || browserType.equals("257")) {
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", "");
            browserConditionParam.put("dataParams", dataParams);
            dataParams = new HashMap<String, Object>();
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", "");
            browserConditionParam.put("conditionDataParams", dataParams);
            dataParams = new HashMap<String, Object>();
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", value);
            browserConditionParam.put("destDataParams", dataParams);
            dataParams = new HashMap<String, Object>();
            dataParams.put("type", dmlUrl);
            dataParams.put("mouldID", "prj");
            dataParams.put("selectedids", "");
            browserConditionParam.put("completeParams", dataParams);
        }
        if (browserType.equals("prjtsk")) {
            String prjid = Util.null2String(dataParams.get("prjid"));
            String prjtasktype = Util.null2String(dataParams.get("prjtasktype"));
            Map<String,String> m = new HashMap<String,String>();
            m.put("prjid", prjid);
            m.put("prjtasktype", prjtasktype);
            browserConditionParam.put("conditionDataParams", m);
            m = new HashMap<String,String>();
            m.put("prjid", prjid);
            m.put("prjtasktype", prjtasktype);
            browserConditionParam.put("completeParams", m);
        }

        //快捷搜索，流程路径
        if (browserType.equals("-99991")) {
            Map<String, Object> conditionDataParams = new HashMap<String, Object>();
            Map<String, Object> completeParams = new HashMap<String, Object>();
            for(String key : dataParams.keySet()){
                conditionDataParams.put(key, dataParams.get(key));
                completeParams.put(key, dataParams.get(key));
            }
            browserConditionParam.put("conditionDataParams", conditionDataParams);
            browserConditionParam.put("completeParams", completeParams);
        }

        if (broswerTypes.indexOf("," + browserType + ",") > -1) {
            browserConditionParam.put("isSingle", false);
        } else {
            browserConditionParam.put("isSingle", true);
        }
        formItem.put("browserConditionParam", browserConditionParam);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", BROWSER);
        formItem.put("value", value);
        formItem.put("browserType", browserType);
        return formItem;
    }

    //日期按钮
    public static Map<String, Object> getFormItemForBrowserDate(String fieldName, String fieldLabel, String value, int viewAttr, List<Map<String, Object>> options, Map<String, Object> colMap) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        // value==null代表作为高级搜索
        if ("".equals(Util.null2String(value))) {
            domKeyList.add(fieldName + "_selectType");
            domKeyList.add(fieldName + "_fromDate");
            domKeyList.add(fieldName + "_toDate");
            Map<String, String> map = new HashMap<String, String>();
            map.put(fieldName + "_selectType", "");
            formItem.put("value", map);
        } else {
            domKeyList.add(fieldName);
            formItem.put("value", value);
        }
        int colSpan = 2;
        int labelcol = 5;
        int fieldcol = 14;
        if (null != colMap) {
            if (!"".equals(Util.null2String(colMap.get("colSpan")))) {
                colSpan = Util.getIntValue((String) colMap.get("colSpan"));
            }
            if (!"".equals(Util.null2String(colMap.get("labelcol")))) {
                labelcol = Util.getIntValue((String) colMap.get("labelcol"));
            }
            if (!"".equals(Util.null2String(colMap.get("fieldcol")))) {
                fieldcol = Util.getIntValue((String) colMap.get("fieldcol"));
            }

        }

        formItem.put("label", fieldLabel);
        formItem.put("colSpan", colSpan);
        formItem.put("labelcol", labelcol);
        formItem.put("fieldcol", fieldcol);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", DATE);
        formItem.put("viewAttr", viewAttr);
        formItem.put("options", options);//下拉框选项

        return formItem;
    }

    //复选按钮
    public static Map<String, Object> getFormItemForCheckbox(String fieldName, String fieldLabel, String value, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("value", value);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", CHECKBOX);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //选择
    public static Map<String, Object> getFormItemForSwitch(String fieldName, String fieldLabel, String value, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("value", value);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("formItemType", SWITCH);
        formItem.put("conditionType", SWITCH);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //下拉选择
    public static Map<String, Object> getFormItemForSelect(String fieldName, String fieldLabel, String fieldId, String value, int viewAttr,String seltype, boolean isSearch,boolean isadd) {
        RecordSet rsTempOption = new RecordSet();
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        List<SearchConditionOption> optionList = new ArrayList<SearchConditionOption>();
        boolean findSelected = false;

        char flag = Util.getSeparator();
        if("prj".equalsIgnoreCase(seltype)){
            rsTempOption.executeProc("prj_selectitembyid_new",""+fieldId+flag+1);
        }else if("prjtype".equalsIgnoreCase(seltype)){
            rsTempOption.execute("select * from cus_selectitem where fieldid='"+fieldId.replace("prjtype_", "")+"' and cancel='0' order by fieldorder ");
        }else if("prjtsk".equalsIgnoreCase(seltype)){
            rsTempOption.executeProc("prjtsk_selectitembyid_new",""+fieldId+flag+1);
        }

        while (rsTempOption.next()) {
            String key = Util.null2String(rsTempOption.getString("selectvalue"));
            String showname = Util.toScreen(rsTempOption.getString("selectname"),7);
            String tmpselectlabel = Util.null2String(rsTempOption.getString("selectlabel"));
            if(!"".equals(tmpselectlabel)){
                showname=SystemEnv.getHtmlLabelNames(tmpselectlabel, 7);
            }
            String isdefault ="prjtype".equals(seltype)? Util.null2String(rsTempOption.getString("prj_isdefault")):Util.null2String(rsTempOption.getString("isdefault"));

            boolean selected = false;
            if (!findSelected&&!isSearch) {
                if(!"".equals(value)){
                    if (key.equals(value)) {
                        findSelected = true;
                        selected = true;
                    }
                }else{
                    if(isadd&&"y".equals(isdefault)){
                        findSelected = true;
                        selected = true;
                        value = key;
                    }
                }
            }
            optionList.add(new SearchConditionOption(key, showname, selected));
        }
        boolean defaultOptionSelectd = false;
        if (!findSelected) {
            defaultOptionSelectd = true;
        }
        if (isSearch) {
            optionList.add(0, new SearchConditionOption("", "全部", defaultOptionSelectd));
            Map<String, String> map = new HashMap<String, String>();
            map.put("selectItem", "");
            formItem.put("value", map);
        } else {
            optionList.add(0, new SearchConditionOption("", "", defaultOptionSelectd));
            formItem.put("value", value);
        }
        formItem.put("options", optionList);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", SELECT);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //动作选择-下拉选择
    public static Map<String, Object> getWfSetActionSelect(String fieldName,String fieldLabel,String value, int viewAttr,User user ,String wftype) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        List<SearchConditionOption> optionList = new ArrayList<SearchConditionOption>();
        ProjectStatusComInfo projectStatusComInfo = new ProjectStatusComInfo();
        if("2".equals(wftype)){
            while(projectStatusComInfo.next()){
                String statusid= projectStatusComInfo.getProjectStatusid();
                String statuslabel= SystemEnv.getHtmlLabelNames(projectStatusComInfo.getProjectStatusname(),user.getLanguage());
                optionList.add(new SearchConditionOption(statusid, statuslabel, false));
            }
        }else if("3".equals(wftype)){
            String statuslabel= SystemEnv.getHtmlLabelName(19561,user.getLanguage())+"\""+SystemEnv.getHtmlLabelNames("220",user.getLanguage())+"\""+SystemEnv.getHtmlLabelName(602,user.getLanguage());
            optionList.add(new SearchConditionOption("0", statuslabel, false));
            statuslabel= SystemEnv.getHtmlLabelName(19561,user.getLanguage())+"\""+SystemEnv.getHtmlLabelNames("2242",user.getLanguage())+"\""+SystemEnv.getHtmlLabelName(602,user.getLanguage());
            optionList.add(new SearchConditionOption("2", statuslabel, false));
            statuslabel= SystemEnv.getHtmlLabelName(19561,user.getLanguage())+"\""+SystemEnv.getHtmlLabelNames("225",user.getLanguage())+"\""+SystemEnv.getHtmlLabelName(602,user.getLanguage());
            optionList.add(new SearchConditionOption("1", statuslabel, false));
        }
        formItem.put("options", optionList);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", SELECT);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //节点选择-下拉选择
    public static Map<String, Object> getWfSetNodeSelect(String fieldName,String fieldLabel,String value, int viewAttr,User user) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        List<SearchConditionOption> optionList = new ArrayList<SearchConditionOption>();

        String statuslabel= SystemEnv.getHtmlLabelName(18009,user.getLanguage());
        optionList.add(new SearchConditionOption("1", statuslabel, false));
        statuslabel= SystemEnv.getHtmlLabelName(18010,user.getLanguage());//节点后
        optionList.add(new SearchConditionOption("0", statuslabel, false));
//		statuslabel= SystemEnv.getHtmlLabelNames("15587,15610",user.getLanguage());
//		optionList.add(new SearchConditionOption("0", statuslabel, false));

        formItem.put("options", optionList);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", SELECT);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //字段选择-下拉选择
    public static Map<String, Object> getWfSetFiledSelect(JSONObject v, User user,String formid,String flag,String viewtype,String fieldName, String fieldLabel, String value) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(flag+"_"+fieldName);
        List<SearchConditionOption> optionList = new ArrayList<SearchConditionOption>();
        optionList.add(new SearchConditionOption("", "", false));
        int viewAttr = 2;
        try {
            String fieldhtmltype = v.getString("fieldhtmltype");
            String type= v.getString("type");
            int ismand= v.getInt("ismand");

            String sql="select * from workflow_billfield where billid="+formid+" and type='"+type+"' and fieldhtmltype='"+fieldhtmltype+"' ";
            if(v.has("fielddbtype")){
                String dbtype=Util.null2String( v.getString("fielddbtype"));
                if(dbtype.equals("varchar2(4000)")){
                    sql+=" and ( fielddbtype='clob' or fielddbtype like 'varchar%' ) ";
                }else if(dbtype.contains("varchar")){
                    sql+=" and fielddbtype like 'varchar%' ";
                }else{
                    sql+=" and fielddbtype='"+v.getString("fielddbtype")+"' ";
                }
            }
            if(Util.getIntValue(viewtype)!=-1){
                sql+=" and viewtype='"+viewtype+"' ";
            }
            RecordSet rs=new RecordSet();
            rs.execute(sql);
            while (rs.next()) {
                String key = Util.null2String( rs.getString("id"));
                String showname = Util.null2String(rs.getString("fieldlabel"));
                showname = SystemEnv.getHtmlLabelNames(showname,user.getLanguage());
                boolean selected = false;
                if (value != null) {
                    if (key.equals(value)) {
                        selected = true;
                    }
                }
                optionList.add(new SearchConditionOption(key, showname, selected));
            }
            if(ismand==1){
                viewAttr = 3;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        formItem.put("options", optionList);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", SELECT);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //附件按钮
    public static Map<String, Object> getFormItemForAttachment(String fieldName, String fieldLabel, String value, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("value", value);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", ATTACHEMENT);
        formItem.put("viewAttr", viewAttr);
        return formItem;
    }

    //超级链接
    public static Map<String, Object> getFormItemForHyperLink(String fieldName, String fieldLabel, String linkUrl, String value) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", HYPERLINK);
        formItem.put("value", value);
        formItem.put("linkUrl", linkUrl);
        return formItem;
    }

    /**
     * 日期组件（yyyy-MM-dd）
     *
     * @param fieldName
     * @param fieldLabel
     * @param value
     * @return
     */
    public static Map<String, Object> getFormItemForDate(String fieldName, String fieldLabel, String value, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", DATEPICKER);
        formItem.put("value", value);
        formItem.put("viewAttr", viewAttr);
        formItem.put("formatPattern", 2);
        return formItem;
    }

    /**
     * 时间组件（HH:mm:ss）
     *
     * @param fieldName
     * @param fieldLabel
     * @param linkUrl
     * @param value
     * @return
     */
    public static Map<String, Object> getFormItemForTime(String fieldName, String fieldLabel, String value, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        List<String> domKeyList = new ArrayList<String>();
        domKeyList.add(fieldName);
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", domKeyList);
        formItem.put("conditionType", TIMEPICKER);
        formItem.put("value", value);
        formItem.put("viewAttr", viewAttr);
        formItem.put("formatPattern", 1);
        return formItem;
    }

    /**
     * 日期时间组件（yyyy-MM-dd HH:MM）
     *
     * @param fieldName
     * @param fieldLabel
     * @param value
     * @return
     */
    public static Map<String, Object> getFormItemForDateTime(List<String> fieldNames, String fieldLabel, List<String> value, int viewAttr) {
        Map<String, Object> formItem = new HashMap<String, Object>();
        formItem.put("label", fieldLabel);
        formItem.put("colSpan", 2);
        formItem.put("labelcol", 5);
        formItem.put("fieldcol", 14);
        formItem.put("domkey", fieldNames);
        formItem.put("conditionType", PRJDATETIME);
        formItem.put("value", value);
        formItem.put("viewAttr", viewAttr);
        formItem.put("formatPattern", 2);
        Map<String, Object> otherParams = new HashMap<String, Object>();
        otherParams.put("noInput", true);
        formItem.put("otherParams", otherParams);
        return formItem;
    }

    /**
     * 根据数据库中字段长度显示页面可输入长度
     *
     * @param fieldDbType
     * @param fieldHtmlType
     * @param broswerType
     * @return
     */
    public static int formatInputLength(String fieldDbType, int fieldHtmlType, String broswerType) {
        //fieldDbType    数据库字段类型
        //fieldHtmlType  页面显示类型
        //broswerType    浏览按钮类型
        //具体算法稍后写
        int size = Util.getIntValue(Util.null2String(fieldDbType.substring(fieldDbType.indexOf("(") + 1, fieldDbType.length() - 1), "0"));
        RecordSet recordSet = new RecordSet();
        String dbType = recordSet.getDBType();
        boolean isOracle = "oracle".equals(dbType);
        boolean isSqlServer = "sqlserver".equals(dbType);
        boolean isMysql = "mysql".equals(dbType);
        String sql = "";
        if (isOracle)
            sql = "select lengthb('啊') lengthb from dual";
        if (isSqlServer)
            sql = "select datalength('啊') lengthb";
        if (isMysql)
            sql = "select length('啊') lengthb";
        recordSet.executeSql(sql);
        recordSet.first();
        size = size / (recordSet.getInt("lengthb"));
        return size;
    }

    /**
     * 获取浏览框中的值
     *
     * @return
     */
    public static String getBrowserShowName(String fieldType, String fieldValue, String dmlurl) {
        RecordSet rs = new RecordSet();
        String showname = "";
        try {
            ArrayList tempshowidlist = Util.TokenizerString(fieldValue, ",");
            if (fieldType.equals("1") || fieldType.equals("17")) { // 人员，多人员
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new ResourceComInfo()
                            .getResourcename((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("2") || fieldType.equals("19")) { // 日期,时间
                // showname += preAdditionalValue;
                showname += fieldValue;
            } else if (fieldType.equals("4") || fieldType.equals("57")) { // 部门，多部门
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DepartmentComInfo()
                            .getDepartmentname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("8") || fieldType.equals("135")) { // 项目，多项目
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new ProjectInfoComInfo()
                            .getProjectInfoname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("7") || fieldType.equals("18")) { // 客户，多客户
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new CustomerInfoComInfo2()
                            .getCustomerInfoname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("164")) { // 分部
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new SubCompanyComInfo()
                            .getSubCompanyname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("9")) { // 单文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DocComInfo()
                            .getDocname((String) tempshowidlist.get(k));
                }
            } else if (fieldType.equals("37")) { // 多文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DocComInfo()
                            .getDocname((String) tempshowidlist.get(k)) + ",";
                }
            } else if (fieldType.equals("23")) { // 资产
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new CapitalComInfo()
                            .getCapitalname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("16") || fieldType.equals("152")) { // 相关请求
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new WorkflowRequestComInfo()
                            .getRequestName((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("67")) { // 客户联系人
                ContacterComInfo contacterComInfo = new ContacterComInfo();
                showname = contacterComInfo.getContacterName(fieldValue);
            } else if (fieldType.equals("sellchance")) { // 商机
                rs.execute("select subject from CRM_SellChance where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("subject");
                }
            }else if (fieldType.equals("product")) { // 产品
                rs.execute("select assetname from LgcAssetCountry where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("assetname");
                }
            }else if (fieldType.equals("prjtsk")) { // 项目任务
                rs.execute("select subject from Prj_TaskProcess where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("subject");
                }
            }else if (fieldType.equals("142")) {// 收发文单位
                DocReceiveUnitComInfo docReceiveUnitComInfo = new DocReceiveUnitComInfo();
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += docReceiveUnitComInfo
                            .getReceiveUnitName((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("226") || fieldType.equals("227")) {// -zzl系统集成浏览按钮
                showname += fieldValue;
            } else if (fieldType.equals("161") || fieldType.equals("162")) {// -fsp 自定义浏览框
                if (Util.null2String(dmlurl).length() == 0) return "";
                try {
                    Browser browser = (Browser) StaticObj.getServiceByFullname(dmlurl, Browser.class);
                    for (int k = 0; k < tempshowidlist.size(); k++) {
                        BrowserBean bb = browser.searchById((String) tempshowidlist.get(k));
                        String name = Util.null2String(bb.getName());
                        if (showname.equals("")) {
                            showname += name;
                        } else {
                            showname += "," + name;
                        }
                    }
                } catch (Exception e) {
                    baseBean.writeLog(e);
                }
            }else if (fieldType.equals("256") || fieldType.equals("257")) {// 自定义树形
                CustomTreeUtil customTreeUtil = new CustomTreeUtil();
                showname = customTreeUtil.getTreeFieldShowName(fieldValue,dmlurl);
            }else if (fieldType.equals("doccategory")) {// 文档目录
                ProjectTransUtil ptu = new ProjectTransUtil();
                showname += ptu.getDocCategoryFullname(""+fieldValue);
            }else if(fieldType.equals("-99991")){
                WorkflowAllComInfo wfComInfo = new WorkflowAllComInfo();
                showname = wfComInfo.getWorkflowname(""+fieldValue);
            }else if(fieldType.equals("workflowNode")){
                rs.execute("select nodename from workflow_nodebase where id="+fieldValue);
                if(rs.next()){
                    showname = ""+rs.getString("nodename");
                }
            }else {
                String sql = "";
                String tablename = new BrowserComInfo().getBrowsertablename(""
                        + fieldType);
                String columname = new BrowserComInfo().getBrowsercolumname(""
                        + fieldType);
                String keycolumname = new BrowserComInfo()
                        .getBrowserkeycolumname("" + fieldType);
                if (columname.equals("") || tablename.equals("")
                        || keycolumname.equals("") || fieldValue.equals("")) {
                } else {
                    sql = "select " + columname + " from " + tablename
                            + " where " + keycolumname + " in(" + fieldValue
                            + ")";
                    rs.executeSql(sql);
                    while (rs.next()) {
                        showname += rs.getString(1) + ",";
                    }
                }
            }
            if (showname.endsWith(",")) {
                showname = showname.substring(0, showname.length() - 1);
            }
        } catch (Exception e) {
            baseBean.writeLog(e);
        }
        return showname;
    }

    /**
     * 获取浏览框中的值
     *
     * @return
     */
    public static String getBrowserShowName(String fieldType, String fieldValue, String dmlurl,User user) {
        RecordSet rs = new RecordSet();
        String showname = "";
        try {
            ArrayList tempshowidlist = Util.TokenizerString(fieldValue, ",");
            if (fieldType.equals("1") || fieldType.equals("17")) { // 人员，多人员
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new ResourceComInfo()
                            .getResourcename((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("2") || fieldType.equals("19")) { // 日期,时间
                // showname += preAdditionalValue;
                showname += fieldValue;
            } else if (fieldType.equals("4") || fieldType.equals("57")) { // 部门，多部门
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DepartmentComInfo()
                            .getDepartmentname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("8") || fieldType.equals("135")) { // 项目，多项目
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new ProjectInfoComInfo()
                            .getProjectInfoname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("7") || fieldType.equals("18")) { // 客户，多客户
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new CustomerInfoComInfo2()
                            .getCustomerInfoname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("164")) { // 分部
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new SubCompanyComInfo()
                            .getSubCompanyname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("9")) { // 单文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DocComInfo()
                            .getDocname((String) tempshowidlist.get(k));
                }
            } else if (fieldType.equals("37")) { // 多文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DocComInfo()
                            .getDocname((String) tempshowidlist.get(k)) + ",";
                }
            } else if (fieldType.equals("23")) { // 资产
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new CapitalComInfo()
                            .getCapitalname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("16") || fieldType.equals("152")) { // 相关请求
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new WorkflowRequestComInfo()
                            .getRequestName((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("67")) { // 客户联系人
                ContacterComInfo contacterComInfo = new ContacterComInfo();
                showname = contacterComInfo.getContacterName(fieldValue);
            } else if (fieldType.equals("sellchance")) { // 商机
                rs.execute("select subject from CRM_SellChance where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("subject");
                }
            }else if (fieldType.equals("product")) { // 产品
                rs.execute("select assetname from LgcAssetCountry where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("assetname");
                }
            }else if (fieldType.equals("prjtsk")) { // 项目任务
                rs.execute("select subject from Prj_TaskProcess where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("subject");
                }
            }else if (fieldType.equals("142")) {// 收发文单位
                DocReceiveUnitComInfo docReceiveUnitComInfo = new DocReceiveUnitComInfo();
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += docReceiveUnitComInfo
                            .getReceiveUnitName((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType.equals("226") || fieldType.equals("227")) {// -zzl系统集成浏览按钮
                showname += fieldValue;
            } else if (fieldType.equals("161") || fieldType.equals("162")) {// -fsp 自定义浏览框
                if (Util.null2String(dmlurl).length() == 0) return "";
                try {
                    Browser browser = (Browser) StaticObj.getServiceByFullname(dmlurl, Browser.class);
                    for (int k = 0; k < tempshowidlist.size(); k++) {
                        BrowserBean bb = browser.searchById((String) tempshowidlist.get(k));
                        String name = Util.null2String(bb.getName());
                        if (showname.equals("")) {
                            showname += name;
                        } else {
                            showname += "," + name;
                        }
                    }
                } catch (Exception e) {
                    baseBean.writeLog(e);
                }
            }else if (fieldType.equals("256") || fieldType.equals("257")) {// 自定义树形
                CustomTreeUtil customTreeUtil = new CustomTreeUtil();
                showname = customTreeUtil.getTreeFieldShowName(fieldValue,dmlurl);
            }else if (fieldType.equals("doccategory")) {// 文档目录
                ProjectTransUtil ptu = new ProjectTransUtil();
                showname += ptu.getDocCategoryFullname(""+fieldValue);
            }else if(fieldType.equals("-99991")){
                WorkflowAllComInfo wfComInfo = new WorkflowAllComInfo();
                showname = wfComInfo.getWorkflowname(""+fieldValue);
            }else if(fieldType.equals("wfFormBrowser")){
                rs.execute("select namelabel from workflow_bill where id="+fieldValue);
                if(rs.next()){
                    showname = SystemEnv.getHtmlLabelNames(""+rs.getString("namelabel"),user.getLanguage()) ;
                }
            }else {
                String sql = "";
                String tablename = new BrowserComInfo().getBrowsertablename(""
                        + fieldType);
                String columname = new BrowserComInfo().getBrowsercolumname(""
                        + fieldType);
                String keycolumname = new BrowserComInfo()
                        .getBrowserkeycolumname("" + fieldType);
                if (columname.equals("") || tablename.equals("")
                        || keycolumname.equals("") || fieldValue.equals("")) {
                } else {
                    sql = "select " + columname + " from " + tablename
                            + " where " + keycolumname + " in(" + fieldValue
                            + ")";
                    rs.executeSql(sql);
                    while (rs.next()) {
                        showname += rs.getString(1) + ",";
                    }
                }
            }
            if (showname.endsWith(",")) {
                showname = showname.substring(0, showname.length() - 1);
            }
        } catch (Exception e) {
            baseBean.writeLog(e);
        }
        return showname;
    }

    /**
     * 获取浏览框中的值
     *
     * @return
     */
    public static List<Map<String,String>> getBrowserObject(String fieldType,String fieldName, String fieldValue, String dmlurl) {
        List<Map<String,String>> obj = new ArrayList<Map<String,String>>();
        RecordSet rs = new RecordSet();
        String showname = "";
        ArrayList tempshowidlist = Util.TokenizerString(fieldValue, ",");
        try {

            Map<String,String> m = null;
            if (fieldType.equals("1") || fieldType.equals("17")) { // 人员，多人员
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new ResourceComInfo().getResourcename((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("2") || fieldType.equals("19")) { // 日期,时间
                m = new HashMap<String,String>();
                m.put("id", fieldValue);
                m.put("name", fieldValue);
                obj.add(m);
            } else if (fieldType.equals("4") || fieldType.equals("57")) { // 部门，多部门
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new DepartmentComInfo() .getDepartmentname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("8") || fieldType.equals("135")) { // 项目，多项目
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new ProjectInfoComInfo().getProjectInfoname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("7") || fieldType.equals("18")) { // 客户，多客户
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new CustomerInfoComInfo2().getCustomerInfoname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("164")) { // 分部
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new SubCompanyComInfo().getSubCompanyname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("9")) { // 单文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new DocComInfo().getDocname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("37")) { // 多文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new DocComInfo().getDocname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("23")) { // 资产
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new CapitalComInfo().getCapitalname((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("16") || fieldType.equals("152")) { // 相关请求
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put("name", new WorkflowRequestComInfo().getRequestName((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("67")) { // 客户联系人
                ContacterComInfo contacterComInfo = new ContacterComInfo();
                showname = contacterComInfo.getContacterName(fieldValue);
                m = new HashMap<String,String>();
                m.put("id", fieldValue);
                m.put("name", showname);
                obj.add(m);
            } else if (fieldType.equals("sellchance")) { // 商机
                rs.executeSql("select subject from CRM_SellChance where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("subject");
                }
                m = new HashMap<String,String>();
                m.put("id", fieldValue);
                m.put("name", showname);
                obj.add(m);
            }else if (fieldType.equals("product")) { // 产品
                rs.executeSql("select assetname from LgcAssetCountry where id=" + fieldValue);
                if (rs.next()) {
                    showname = rs.getString("assetname");
                }
                m = new HashMap<String,String>();
                m.put("id", fieldValue);
                m.put("name", showname);
                obj.add(m);
            }else if (fieldType.equals("142")) {// 收发文单位
                DocReceiveUnitComInfo docReceiveUnitComInfo = new DocReceiveUnitComInfo();
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    m = new HashMap<String,String>();
                    m.put("id", (String) tempshowidlist.get(k));
                    m.put(fieldName, docReceiveUnitComInfo.getReceiveUnitName((String) tempshowidlist.get(k)));
                    obj.add(m);
                }
            } else if (fieldType.equals("226") || fieldType.equals("227")) {// -zzl系统集成浏览按钮
                m = new HashMap<String,String>();
                m.put("id", fieldValue);
                m.put(fieldName, showname);
                obj.add(m);
            } else if (fieldType.equals("161") || fieldType.equals("162")) {// -fsp 自定义浏览框
                if (Util.null2String(dmlurl).length() == 0) return obj;
                try {
                    Browser browser = (Browser) StaticObj.getServiceByFullname(dmlurl, Browser.class);
                    for (int k = 0; k < tempshowidlist.size(); k++) {
                        BrowserBean bb = browser.searchById((String) tempshowidlist.get(k));
                        String name = Util.null2String(bb.getName());
                        m = new HashMap<String,String>();
                        m.put("id", (String)tempshowidlist.get(k));
                        m.put("name", name);
                        obj.add(m);

                    }
                } catch (Exception e) {
                    baseBean.writeLog(e);
                }
            } else if (fieldType.equals("256") || fieldType.equals("257")) {// 自定义树形
                CustomTreeUtil customTreeUtil = new CustomTreeUtil();
                showname = customTreeUtil.getTreeFieldShowName(fieldValue,dmlurl);
                m = new HashMap<String,String>();
                m.put("id", fieldValue);
                m.put("name", showname);
            } else {
                String sql = "";
                String tablename = new BrowserComInfo().getBrowsertablename(""
                        + fieldType);
                String columname = new BrowserComInfo().getBrowsercolumname(""
                        + fieldType);
                String keycolumname = new BrowserComInfo()
                        .getBrowserkeycolumname("" + fieldType);
                if (columname.equals("") || tablename.equals("")
                        || keycolumname.equals("") || fieldValue.equals("")) {
                } else {
                    sql = "select " + columname + " from " + tablename
                            + " where " + keycolumname + " in(" + fieldValue
                            + ")";
                    rs.executeSql(sql);
                    while (rs.next()) {
                        m = new HashMap<String,String>();
                        m.put("id", fieldValue);
                        m.put("name", rs.getString(1));
                        obj.add(m);
                    }
                }
            }

        } catch (Exception e) {
            baseBean.writeLog(e);
        }
        return obj;
    }

    public static String getBrowserFieldvalue(User user,String fieldValue,int fieldType, String dmlurl,boolean isViewPage){
        RecordSet rs=new RecordSet();
        String showname = "";
        try {
            ArrayList tempshowidlist = Util.TokenizerString(fieldValue, ",");
            if (fieldType == 1 || fieldType == 17) { // 人员，多人员
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new ResourceComInfo()
                            .getResourcename((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 2 || fieldType == 19) { // 日期,时间
                // showname += preAdditionalValue;
                showname += fieldValue;
            } else if (fieldType == 4 || fieldType == 57) { // 部门，多部门
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DepartmentComInfo()
                            .getDepartmentname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 8 || fieldType == 135) { // 项目，多项目
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new ProjectInfoComInfo()
                            .getProjectInfoname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 7 || fieldType == 18) { // 客户，多客户
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new CustomerInfoComInfo2()
                            .getCustomerInfoname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 164) { // 分部
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new SubCompanyComInfo()
                            .getSubCompanyname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 9) { // 单文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DocComInfo()
                            .getDocname((String) tempshowidlist.get(k));
                }
            } else if (fieldType == 37) { // 多文档
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new DocComInfo()
                            .getDocname((String) tempshowidlist.get(k)) + ",";
                }
            } else if (fieldType == 23) { // 资产
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new CapitalComInfo()
                            .getCapitalname((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 16 || fieldType == 152) { // 相关请求
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += new WorkflowRequestComInfo()
                            .getRequestName((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 142) {// 收发文单位
                DocReceiveUnitComInfo docReceiveUnitComInfo = new DocReceiveUnitComInfo();
                for (int k = 0; k < tempshowidlist.size(); k++) {
                    showname += docReceiveUnitComInfo
                            .getReceiveUnitName((String) tempshowidlist.get(k))
                            + ",";
                }
            } else if (fieldType == 226 || fieldType == 227) {// -zzl系统集成浏览按钮
                showname += fieldValue;
            }else if(fieldType == 161){//自定义单选
                showname = "";									//新建时候默认值显示的名称
                String showid = fieldValue;							//新建时候默认值
                String flagstr = "&nbsp;";
                if(!isViewPage){
                    flagstr = ",";
                }
                try{
                    Browser browser = (Browser)StaticObj.getServiceByFullname(dmlurl, Browser.class);
                    BrowserBean bb = browser.searchById(showid);
                    String desc = Util.null2String(bb.getDescription());
                    String name = Util.null2String(bb.getName());
                    name = name.replaceAll("<", "&lt;");
                    name = name.replaceAll(">", "&gt;");
                    String id=Util.null2String(bb.getId());
                    String href=Util.null2String(bb.getHref());
                    if(desc.indexOf("'")!=-1){
                        desc = desc.replace("'", "%27");
                    }
                    if(href.equals("")&&(!id.equals(""))){
                        showname = "<a title='"+desc+"'>"+name+"</a>"+flagstr;
                    }else if(!id.equals("")){
                        String customid = Util.null2String(browser.getCustomid());
                        href =getHrefByBrowser(customid, href, showid, rs);
                        if(isChineseCharacter(href)){
                            showname+="<a title='"+desc+"' href='javascript:openHrefWithChinese(\""+href+"\");'>"+name+"</a>"+flagstr;
                        }else{
                            showname+="<a title='"+desc+"' href='"+href+"' target='_blank'>"+name+"</a>"+flagstr;
                        }
                    }
                }catch(Exception e){
                }
                if (showname.endsWith(",")) {
                    showname = showname.substring(0, showname.length() - 1);
                }
            }else if(fieldType == 162){//自定义多选
                showname = "";									// 新建时候默认值显示的名称
                String showid = fieldValue;							// 新建时候默认值
                String flagstr = "&nbsp;&nbsp;";
                if(!isViewPage){
                    flagstr = ",";
                }
                try{
                    Browser browser=(Browser)StaticObj.getServiceByFullname(dmlurl, Browser.class);
                    ArrayList l = Util.TokenizerString(showid,",");
                    for(int j=0;j<l.size();j++){
                        String curid=(String)l.get(j);
                        BrowserBean bb=browser.searchById(curid);
                        String name=Util.null2String(bb.getName());
                        name = name.replaceAll("<", "&lt;");
                        name = name.replaceAll(">", "&gt;");
                        String desc=Util.null2String(bb.getDescription());
                        String id=Util.null2String(bb.getId());
                        String href=Util.null2String(bb.getHref());
                        if(desc.indexOf("'")!=-1){
                            desc = desc.replace("'", "%27");
                        }
                        if(href.equals("")&&(!id.equals(""))){
                            showname+="<a title='"+desc+"'>"+name+"</a>"+flagstr;
                        }else if(!id.equals("")){
                            String customid = Util.null2String(browser.getCustomid());
                            href =getHrefByBrowser(customid, href, curid, rs);
                            if(isChineseCharacter(href)){
                                showname+="<a title='"+desc+"' href='javascript:openHrefWithChinese(\""+href+"\");'>"+name+"</a>"+flagstr;
                            }else{
                                showname+="<a title='"+desc+"' href='"+href+"' target='_blank'>"+name+"</a>"+flagstr;
                            }
                        }
                    }
                }catch(Exception e){
                }
                if (showname.endsWith(",")) {
                    showname = showname.substring(0, showname.length() - 1);
                }
            }else if(fieldType == 256||fieldType == 257){
                CustomTreeUtil customTreeUtil = new CustomTreeUtil();
                showname = customTreeUtil.getTreeFieldShowName(fieldValue,dmlurl);
                try {
                    showname = showname.replaceAll("</a>&nbsp", "</a>,");
                    if (showname.lastIndexOf("</a>,") != -1 && showname.lastIndexOf("</a>,") == showname.length() - 5) {
                        showname = showname.substring(0, showname.length()-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                String sql = "";
                String tablename = new BrowserComInfo().getBrowsertablename(""
                        + fieldType);
                String columname = new BrowserComInfo().getBrowsercolumname(""
                        + fieldType);
                String keycolumname = new BrowserComInfo()
                        .getBrowserkeycolumname("" + fieldType);
                if (columname.equals("") || tablename.equals("")
                        || keycolumname.equals("") || fieldValue.equals("")) {
                } else {
                    sql = "select " + columname + " from " + tablename
                            + " where " + keycolumname + " in(" + fieldValue
                            + ")";
                    rs.executeSql(sql);
                    while (rs.next()) {
                        showname += rs.getString(1) + ",";
                    }
                }
            }
            if (showname.endsWith(",")) {
                showname = showname.substring(0, showname.length() - 1);
            }
        } catch (Exception e) {
        }

        return showname;
    }

    public static String getHrefByBrowser(String customid , String href, String showid, RecordSet rs){
        if(!"".equals(customid)&&!"0".equals(customid)){
            rs.executeSql("select modeid,formid from mode_custombrowser where id="+customid);
            if(rs.next()){
                int tempmodeid = rs.getInt("modeid");
                int tempformid = rs.getInt("formid");
                if(tempmodeid<=0){
                    rs.executeSql("select tablename from workflow_bill where id = " + tempformid);
                    if (rs.next()){
                        String tableName = rs.getString("tablename");
                        rs.executeSql("select formmodeid from "+tableName+" where id="+showid);
                        if(rs.next()){
                            int tempFormmodeid = rs.getInt("formmodeid");
                            if(tempFormmodeid>0){
                                href=href.replaceAll("modeId=[0-9]+?&", "modeId="+tempFormmodeid+"&");
                            }
                        }
                    }
                }
            }
        }
        return href;
    }

    //判断url里面是否还有中文
    public static boolean isChineseCharacter(String href){
        char[] charArray = href.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                // 中文Unicode编码区间：0x4e00--0x9fbb
                return true;
            }
        }
        return false;
    }

    /**
     * 项目通用字段取真正的下拉框id
     * @param fieldid
     * @return
     */
    public static String getRealSelectFieldId4prj(String fieldid){
        RecordSet rs=new RecordSet();
        String sql="select id from prjDefineField where prjtype=-1 and fieldname in(select fieldname from prjDefineField where id='"+fieldid+"')";
        rs.execute(sql);
        rs.next();
        return Util.null2String( rs.getString(1));
    }

    /**
     * 获取文档datas
     * @param ids
     * @return
     * @throws Exception
     */
    public static List<Map<String,Object>> getAccessoryDatasList(String ids) throws Exception{
        BaseService baseService = new BaseService();
        String strs [] = Util.splitString(ids, ",");
        RecordSet rs = new RecordSet();
        List<Map<String,Object>> apiResult = new ArrayList<Map<String,Object>>();
        for(int i=0;i<strs.length;i++){
            Map<String, Object> filemap = new HashMap<String, Object>();
            String tempvalue = strs[i];
            if (!"".equals(tempvalue)) {
                DocImageManager docImageManager = new DocImageManager();
                rs.execute("select id,docsubject,accessorycount,SecCategory from docdetail where id in("+tempvalue+") order by id asc");
                if(rs.next()) {
                    String showid = Util.null2String(rs.getString(1));
                    int accessoryCount = rs.getInt(3);
                    docImageManager.resetParameter();
                    docImageManager.setDocid(Integer.parseInt(showid));
                    docImageManager.selectDocImageInfo();

                    String fileid = "";
                    long filesize = 0;
                    String filename = "";
                    String fileExtendName = "";
                    int versionId = 0;
                    if (docImageManager.next()) {
                        fileid = docImageManager.getImagefileid();
                        filesize = docImageManager.getImageFileSize(Util.getIntValue(fileid));
                        filename = docImageManager.getImagefilename();
                        fileExtendName = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                        versionId = docImageManager.getVersionId();
                    }
                    if (accessoryCount > 1)
                        fileExtendName = "htm";
                    String imgSrc  = "/images/filetypeicons/" + AttachFileUtil.getIconPathByExtendName(fileExtendName);
                    boolean showLoad = true;
                    //附件上传

                    //链接参考签字意见列表
                    String filelink = ServiceUtil.fileViewUrl+"?id=" + showid + "&imagefileId=" + fileid + "&isFromAccessory=true";
                    String loadlink = "/weaver/weaver.file.FileDownload?fileid=" + fileid + "&download=1";

                    filemap.put("fileid", showid);
                    filemap.put("filesize", baseService.convertSuitableFileSize(filesize));
                    filemap.put("filename", filename);
                    filemap.put("fileExtendName", fileExtendName);
                    filemap.put("filelink", filelink);
                    filemap.put("versionId", versionId);
                    filemap.put("imgSrc", imgSrc);
                    filemap.put("showLoad", showLoad);
                    filemap.put("loadlink", loadlink);
                    filemap.put("showDelete", true);
                    filemap.put("isImg", IMG_PATTERN.matcher(Util.null2String(filename)).find());

                    apiResult.add(filemap);
                }
            }
        }
        return apiResult;
    }
}
