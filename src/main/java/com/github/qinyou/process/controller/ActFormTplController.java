package com.github.qinyou.process.controller;

import com.github.qinyou.common.activiti.ActConst;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.render.StringAsFileRender;
import com.github.qinyou.common.utils.FileUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.process.model.ActFormTpl;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.Date;

/**
 * act_form_tpl 控制器
 *
 * @author chuang
 * @since 2020-06-30 16:48:40
 */
@Slf4j
@SuppressWarnings("Duplicates")
@RequirePermission("actFormTpl")
public class ActFormTplController extends BaseController {

    /**
     * 列表页
     */
    public void index() {
        render("process/actFormTpl.ftl");
    }

    /**
     * 列表数据
     */
    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<ActFormTpl> actFormTplPage = ActFormTpl.dao.page(pageNumber, pageSize, where);
        renderDatagrid(actFormTplPage);
    }


    /**
     * 打开新增或者修改弹出框
     */
    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            ActFormTpl actFormTpl = ActFormTpl.dao.findById(id);
            setAttr("actFormTpl", actFormTpl);
        }
        render("process/actFormTpl_form.ftl");
    }


    /**
     * 新增 action
     */
    public void addAction() {
        UploadFile file = getFile();
        if (file == null) {
            renderFail("模板文件不可为空");
            return;
        }
        String extension = FilenameUtils.getExtension(file.getFileName());
        if (!"html".equalsIgnoreCase(extension)) {
            renderFail("模板文件必须是html文件");
            return;
        }
        String template = null;
        try {
            template = FileUtils.readFileToString(file.getFile(), "utf-8");
            FileUtils.deleteFile(file.getFile());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            renderFile("模板文件读取失败");
            return;
        }
        ActFormTpl actFormTpl = getBean(ActFormTpl.class, "");
        actFormTpl.setTemplate(template);
        ActFormTpl latestVersion = ActFormTpl.dao.findLatestByKey(actFormTpl.getFormKey());
        int version = 1;
        if (latestVersion != null) {
            version = latestVersion.getFormVersion() + 1;
        }
        actFormTpl.setId(actFormTpl.getFormKey() + ":" + version)
                .setFormVersion(version)
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        if (actFormTpl.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }

    /**
     * 修改 action
     */
    public void updateAction() {
        UploadFile file = getFile();
        String template = null;
        if (file != null) {
            String extension = FilenameUtils.getExtension(file.getFileName());
            if (!"html".equalsIgnoreCase(extension)) {
                renderFail("模板文件必须是html文件");
                return;
            }
            try {
                template = FileUtils.readFileToString(file.getFile(), "utf-8");
                FileUtils.deleteFile(file.getFile());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                renderFile("模板文件读取失败");
                return;
            }
        }
        ActFormTpl actFormTpl = ActFormTpl.dao.findById(get("id"));
        if (actFormTpl == null) {
            renderFile("ID参数错误");
            return;
        }

        // formKey,formVersion 不可更新
        if (template != null) {
            actFormTpl.setTemplate(template);
        }
        actFormTpl
                .setState(get("state"))
                .setRemark(get("remark"))
                .setPlugins(get("plugins"))
                .setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (actFormTpl.update()) {
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }

    /**
     * 删除 action
     */
    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        String sql = "select count(1) as c from act_hi_varinst where NAME_ = '"+ ActConst.APPLY_FORM_TPL_ID +"' and TEXT_  in ('"+ids+"')";
        Record record= Db.findFirst(sql);
        if(record.getLong("c")>0L){
            renderFail("删除失败，表单已产生历史单据,禁止删除!");
            return;
        }
        sql = "delete from act_form_tpl where id in ( '" + ids + "' ) ";
        Db.update(sql);
        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * 预览
     */
    @Before(IdRequired.class)
    public void viewModel() {
        String id = get("id");
        ActFormTpl formTpl = ActFormTpl.dao.findById(id);
        setAttr("tplRemark", formTpl.getRemark());
        setAttr("plugins", formTpl.getPlugins());
        setAttr("template", formTpl.getTemplate());
        render("process/actFormTpl_preview.ftl");
    }

    // 模板文件下载
    @Before(IdRequired.class)
    public void downloadHtml() {
        String id = get("id");
        ActFormTpl formTpl = ActFormTpl.dao.findById(id);
        if (formTpl == null) {
            renderFail("id 参数错误");
            return;
        }
        render(StringAsFileRender.me(formTpl.getTemplate())
                .fileName(formTpl.getFormKey() + "-v" + formTpl.getFormVersion() + ".html")
        );
    }

    // 表单模板开发使用
    public void dev(){
        setAttr("dev",true);
        render("process/actFormTpl_preview.ftl");
    }
}
