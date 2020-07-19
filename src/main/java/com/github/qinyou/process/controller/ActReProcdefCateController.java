package com.github.qinyou.process.controller;

import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.process.model.ActReProcdefCate;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;

import java.util.Date;
import java.util.List;

/**
 * act_re_procdef_cate 控制器
 * @author chuang
 * @since 2020-07-09 16:14:29
 */
@SuppressWarnings("Duplicates")
@RequirePermission("actReProcdefCate")
public class ActReProcdefCateController extends BaseController{
    // 列表页
    public void index(){
      render("process/actReProcdefCate.ftl");
    }

    // 例表数据
    public void query(){
        List<ActReProcdefCate> list = ActReProcdefCate.dao.findAllSort();
        renderJson(list);
    }

    // 打开新增或者修改弹出框
    public void newModel(){
        String id= get("id");
        String pid;
        if(StringUtils.notEmpty(id)){
            // 编辑
            ActReProcdefCate actReProcdefCate=ActReProcdefCate.dao.findById(id);
            set("actReProcdefCate",actReProcdefCate);
            pid = actReProcdefCate.getPid();
        }else{
            // 新增
            pid = get("pid"); //必穿
        }
        set("pid",pid);   // 上级
        render("process/actReProcdefCate_form.ftl");
    }

    // 新增action
    public void addAction(){
        ActReProcdefCate actReProcdefCate=getBean(ActReProcdefCate.class,"");
        actReProcdefCate.setId(IdUtils.id())
            .setCreater(WebUtils.getSessionUsername(this))
            .setCreateTime(new Date());
        if(actReProcdefCate.save()){
            renderSuccess(ADD_SUCCESS);
        }else{
            renderFail(ADD_FAIL);
        }
    }

    // 修改 action
    public void updateAction(){
        ActReProcdefCate actReProcdefCate=getBean(ActReProcdefCate.class,"");
        actReProcdefCate.setUpdater(WebUtils.getSessionUsername(this))
            .setUpdateTime(new Date());
        if( actReProcdefCate.update()){
            renderSuccess(UPDATE_SUCCESS);
        }else{
            renderFail(UPDATE_FAIL);
        }
    }

    // 新增、编辑 父组 下拉框使用
    public void listGroup(){
        String sql = "select id,name from act_re_procdef_cate where pid = '0' order by sortNum";
        List<ActReProcdefCate> list = ActReProcdefCate.dao.find(sql);
        renderJson(list);
    }

    // 删除action
    @Before(IdRequired.class)
    public void deleteAction(){
        String id = getPara("id");
        String deleteSql = "delete from act_re_procdef_cate where id = ? or pid = ? ";
        Db.update(deleteSql,id,id);
        renderSuccess(DELETE_SUCCESS);
    }
}
