package com.engine.govern.biz;

import com.engine.govern.constant.Remind;
import com.engine.govern.constant.YesOrNo;
import com.weaver.general.Util;

/**
 * @Auther: 谢凯
 * @Date: 2018/8/31 17:38
 * @Description:
 */
public class CategoryTransMethod {
    //id转是否， 1：是 0：否
    public String isusedTrans(String isused){
        return YesOrNo.getValue(Util.getIntValue(isused,0));
    }

    //提醒类型
    public String noticeTypeTrans(String trrigerType){
        String noticeTypes = "";
        String [] array = trrigerType.split(",");
//        if(array.length ==0){
//            return "没有设置";
//        }
        for (String noticeType:array){
            String noticeType2 =Remind.getValue(Util.getIntValue(noticeType,0));
            if(noticeType != array[array.length-1]){
                noticeTypes = noticeTypes+noticeType2+",";
            }else {
                noticeTypes = noticeTypes+noticeType2;
            }
        }
        return noticeTypes;
    }
}
