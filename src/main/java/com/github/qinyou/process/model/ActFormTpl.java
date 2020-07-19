package com.github.qinyou.process.model;

import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.process.model.base.BaseActFormTpl;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated model
 * DB: act_form_tpl  表单模板
 * @author chuang
 * @since 2020-06-30 16:48:40
 */
@SuppressWarnings({"serial", "Duplicates"})
public class ActFormTpl extends BaseActFormTpl<ActFormTpl> {
    public static final ActFormTpl dao = new ActFormTpl().dao();

    /**
     * 分页查询
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<ActFormTpl>  page(int pageNumber,int pageSize,String where ){
        String sqlSelect = " select id,formKey,formVersion,remark,state,plugins,creater,createTime,updater,updateTime ";
        String sqlExceptSelect = " from act_form_tpl  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        sqlExceptSelect += " order by formKey asc, formVersion desc";
        return this.paginate(pageNumber,pageSize,sqlSelect,sqlExceptSelect);
    }

    /**
     * 根据formKey 查询最高版本表单模板
     * @param formKey
     * @return
     */
    public ActFormTpl findLatestByKey(String formKey){
        String sql = "select * from act_form_tpl where formKey = ? order by formVersion desc";
        return this.findFirst(sql,formKey);
    }

    /**
     * 根据 formKey 查询最高版本 在线【状态为1】的表单模板
     * @param formKey
     * @return
     */
    public ActFormTpl findLatestActiveByKey(String formKey){
        String sql = "select * from act_form_tpl where formKey = ? and state = '1' order by formVersion desc";
        return this.findFirst(sql,formKey);
    }
}
